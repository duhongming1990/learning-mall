package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-redis.xml"})
public class RedisDaoTest<T> {

    @Autowired
    private RedisDao<String> redisDao;

    @Test
    public void set() {
        redisDao.setObject("a","a",10);
    }

    @Test
    public void get() {
        //redisDao.getObject("a");
    }
}