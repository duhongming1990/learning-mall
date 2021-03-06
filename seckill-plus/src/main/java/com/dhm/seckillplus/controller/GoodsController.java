package com.dhm.seckillplus.controller;

import com.dhm.seckillplus.common.prefix.KeyPrefixs;
import com.dhm.seckillplus.common.response.ResultBean;
import com.dhm.seckillplus.dao.redis.RedisDao;
import com.dhm.seckillplus.domain.SeckillUser;
import com.dhm.seckillplus.domain.User;
import com.dhm.seckillplus.service.GoodsService;
import com.dhm.seckillplus.service.UserService;
import com.dhm.seckillplus.vo.GoodsDetailVo;
import com.dhm.seckillplus.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/11/1 16:01
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
    UserService userService;

	@Autowired
    GoodsService goodsService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    RedisDao redisDao;
	
    @RequestMapping(value = "/to_list",produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model,
                       User user) {
//    	model.addAttribute("user", user);
//        //查询商品列表
//        List<GoodsVo> goodsList = goodsService.listGoodsVo();
//        model.addAttribute("goodsList", goodsList);
        //取缓存
        String html = (String) redisDao.get(KeyPrefixs.GoodsKey.GOODS_LIST.getBasePrefix(),"",String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
//    	 return "goods_list";
        SpringWebContext ctx = new SpringWebContext(request,response,
                request.getServletContext(),request.getLocale(), model.asMap(), applicationContext);
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisDao.set(KeyPrefixs.GoodsKey.GOODS_LIST.getBasePrefix(),"",html);
        }
        return html;
    }

    @RequestMapping(value = "/to_detail/{goodsId}",produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String detail(HttpServletRequest request, HttpServletResponse response,
                         Model model,User user,
                         @PathVariable("goodsId")long goodsId) {
        //取缓存
        String html = (String) redisDao.get(KeyPrefixs.GoodsKey.GOODS_DETIAL.getBasePrefix(),String.valueOf(goodsId),String.class);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }

        model.addAttribute("user", user);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);


        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        SpringWebContext ctx = new SpringWebContext(request,response,
                request.getServletContext(),request.getLocale(), model.asMap(), applicationContext);
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if(!StringUtils.isEmpty(html)) {
            redisDao.set(KeyPrefixs.GoodsKey.GOODS_DETIAL.getBasePrefix(),String.valueOf(goodsId),html);
        }
        return html;
    }

    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public ResultBean<GoodsDetailVo> detail(SeckillUser user,
                                            @PathVariable("goodsId")long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return new ResultBean<>(vo);
    }
}
