package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by geely
 */
@Slf4j
@Component
public class RedisPoolUtil {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 设置key的有效期，单位是秒
     * @param key
     * @param exTime
     * @return
     */
    public Long expire(String key,int exTime){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
            log.error("expire key:{} error",key,e);
            jedisPool.returnBrokenResource(jedis);
            return result;
        }
        jedisPool.returnResource(jedis);
        return result;
    }

    //exTime的单位是秒
    public String setEx(String key,String value,int exTime){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.setex(key,exTime,value);
        } catch (Exception e) {
            log.error("setex key:{} value:{} error",key,value,e);
            jedisPool.returnBrokenResource(jedis);
            return result;
        }
        jedisPool.returnResource(jedis);
        return result;
    }

    public String set(String key,String value){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = jedisPool.getResource();
            result = jedis.set(key,value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error",key,value,e);
            jedisPool.returnBrokenResource(jedis);
            return result;
        }
        jedisPool.returnResource(jedis);
        return result;
    }

    public String get(String key){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error",key,e);
            jedisPool.returnBrokenResource(jedis);
            return result;
        }
        jedisPool.returnResource(jedis);
        return result;
    }

    public Long del(String key){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error",key,e);
            jedisPool.returnBrokenResource(jedis);
            return result;
        }
        jedisPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {
//        Jedis jedis = jedisPool.getJedis();
////
////        RedisShardedPoolUtil.set("keyTest","value");
////
////        String value = RedisShardedPoolUtil.get("keyTest");
////
////        RedisShardedPoolUtil.setEx("keyex","valueex",60*10);
////
////        RedisShardedPoolUtil.expire("keyTest",60*20);
////
////        RedisShardedPoolUtil.del("keyTest");
////
////
////        String aaa = RedisShardedPoolUtil.get(null);
////        System.out.println(aaa);
////
////        System.out.println("end");


    }


}
