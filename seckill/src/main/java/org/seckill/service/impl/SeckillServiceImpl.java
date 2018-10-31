package org.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
/**
 * 什么时候回滚事务
 * 抛出运行期异常（RuntimeException）
 * 小心不当的try-catch
 */
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(SeckillService.class);

    private static final String SECKILL = "seckill:";

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao<Seckill> seckillRedisDao;

    private static final String salt = "fdafsadds";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        //优化点:缓存优化:超时的基础上维护一致性
        //1.访问redis
        Seckill seckill = seckillRedisDao.getObject(SECKILL+seckillId,Seckill.class);
        if(seckill==null) {
            //2.访问mysql
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {//3.说明查不到这个秒杀产品的记录
                return new Exposer(false, seckillId);
            } else {//4.缓存到redis
                seckillRedisDao.setObject(SECKILL + seckillId, seckill, 60 * 60);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();

        //startTime<nowTime<endTime;
        if(startTime.getTime()>nowTime.getTime()
                ||nowTime.getTime()>endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        String md5 = getMD5(seckillId);
        return new Exposer(true,seckillId,md5);
    }

    private String getMD5(long seckillId){
        String base = seckillId+"";//salt
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Transactional(rollbackFor = Exception.class,isolation = Isolation.DEFAULT)
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null && !md5.equals(getMD5(seckillId))){
            throw  new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑：减库存 + 记录购买行为

//        try {
            //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                //重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                //减库存,因为有rowLock
                int updateCount = seckillDao.reduceNumber(seckillId, new Date());
                if (updateCount <= 0) {
                    //没有更新到记录，秒杀结束
                    throw new SeckillCloseException("seckill is closed");
                }else{
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
//        }catch (Exception e){
//            //所有编译器异常转化为运行期异常
//            throw new SeckillException("seckill inner error:"+e.getMessage());
//        }

    }

    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        if(md5 == null && !md5.equals(getMD5(seckillId))){
            throw  new SeckillException("seckill data rewrite");
        }
        Date killTime = new Date();
        Map<String,Object> map = new HashMap();
        map.put("seckillId",seckillId);
        map.put("userPhone",userPhone);
        map.put("killTime",killTime);
        map.put("result",null);
        try{
           seckillDao.killByProceduce(map);
           int result = MapUtils.getInteger(map,"result",-1);
           if(result==1){
               //秒杀成功
               SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
               return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
           }else if(result==0){
               return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
           }
        }catch (Exception e){
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }
        return null;
    }


}
