package com.mmall.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:spring/spring-context-redis.xml")
public class RedisPoolUtilTest {

    @Autowired
    private RedisPoolUtil redisPoolUtil;

    @Test
    public void testRedisPoolUtil(){
        System.out.println(redisPoolUtil);
    }

    @Test
    public void testOperation(){
        String returnSet = redisPoolUtil.set("keyTest","value");
        String returnGet = redisPoolUtil.get("keyTest");
    }
}