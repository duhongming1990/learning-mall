package com.dhm.seckillplus.dao.redis;

import com.alibaba.fastjson.JSON;
import com.dhm.seckillplus.common.prefix.BasePrefix;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis Cache 工具类
 * 1.优化缓存时间的表达式逻辑 2018-03-24
 * 2.通过 protostuff 缓存对象
 * @author dhm
 * @version 2018-03-28
 */
@Component
public class RedisDao<T>{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JedisPool jedisPool;

    /**
     * 获取当个对象
     * */
    public <T> T get(BasePrefix prefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            String  str = jedis.get(realKey);
            T t =  stringToBean(str, clazz);
            return t;
        }finally {
            jedis.close();
        }
    }

    /**
     * 设置对象
     * */
    public <T> boolean set(BasePrefix prefix, String key,  T value) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            String str = beanToString(value);
            if(str == null || str.length() <= 0) {
                return false;
            }
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            int seconds =  prefix.expireSeconds();
            if(seconds <= 0) {
                jedis.set(realKey, str);
            }else {
                jedis.setex(realKey, seconds, str);
            }
            return true;
        }finally {
            jedis.close();
        }
    }

    /**
     * 获取当个对象
     * */
    public void delete(BasePrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            jedis.del(realKey);
        }finally {
            jedis.close();
        }
    }

    public static <T> String beanToString(T value) {
        if(value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class) {
            return ""+value;
        }else if(clazz == String.class) {
            return (String)value;
        }else if(clazz == long.class || clazz == Long.class) {
            return ""+value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T stringToBean(String str, Class<T> clazz) {
        if(str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(str);
        }else if(clazz == String.class) {
            return (T)str;
        }else if(clazz == long.class || clazz == Long.class) {
            return  (T)Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    /**
     * string(字符串)
     * set key value [EX seconds] [PX milliseconds] [NX|XX]
     * @param key
     * @param value
     * @param cacheSeconds
     * @return
     */
    public String set(String key, String value, int cacheSeconds){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            if (cacheSeconds > 0) {
                result = jedis.setex(key,cacheSeconds,value);
            }else{
                result = jedis.set(key, value);
            }
            logger.debug("setString {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setString {} = {}", key, value, e);
        } finally {
            jedis.close();
        }
        return result;
    }

    /**
     * string(字符串)
     * get key
     * @param key
     * @return
     */
    public String get(String key) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                value = jedis.get(key);
//				value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
                logger.debug("getString {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getString {} = {}", key, value, e);
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     *
     * hash（哈希）
     * hmset key field value [field value ...]
     * @param key
     * @param value
     * @param cacheSeconds
     * @return
     */
    public String hmset(String key, Map<String, String> value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.hmset(key, value);
            if (cacheSeconds > 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setMap {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setMap {} = {}", key, value, e);
        } finally {
            jedis.close();
        }
        return result;
    }

    /**
     * hash（哈希）
     * hgetall key
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(String key) {
        Map<String, String> value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                value = jedis.hgetAll(key);
                logger.debug("getMap {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getMap {} = {}", key, value, e);
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * list（列表）
     * rpush key value [value ...]
     * @param key
     * @param value
     * @param cacheSeconds
     * @return
     */
    public long rpush(String key, List<String> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.rpush(key, value.toArray(new String[value.size()]));
            if (cacheSeconds > 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setList {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setList {} = {}", key, value, e);
        } finally {
            jedis.close();
        }
        return result;
    }

    /**
     * list（列表）
     * lrange key start stop
     * @param key
     * @return
     */
    public List<String> lrange(String key) {
        List<String> value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                value = jedis.lrange(key, 0, -1);
                logger.debug("getList {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getList {} = {}", key, value, e);
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * set（集合）
     * sadd key member [member ...]
     * @param key
     * @param value
     * @param cacheSeconds
     * @return
     */
    public long sadd(String key, Set<String> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.sadd(key, value.toArray(new String[value.size()]));
            if (cacheSeconds > 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setSet {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setSet {} = {}", key, value, e);
        } finally {
            jedis.close();
        }
        return result;
    }

    /**
     * set（集合）
     * smembers key
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        Set<String> value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                value = jedis.smembers(key);
                logger.debug("getSet {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getSet {} = {}", key, value, e);
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * zset(sorted set：有序集合）
     * zadd key [NX|XX] [CH] [INCR] score member [score member ...]
     * @param key
     * @param value
     * @param cacheSeconds
     * @return
     */
    public long zset(String key, Map<String,Double> value, int cacheSeconds){
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.zadd(key,value);
            if (cacheSeconds > 0) {
                jedis.expire(key, cacheSeconds);
            }
            logger.debug("setZset {} = {}", key, value);
        } catch (Exception e) {
            logger.warn("setZset {} = {}", key, value, e);
        } finally {
            jedis.close();
        }
        return result;
    }

    /**
     * zset(sorted set：有序集合）
     * zrange key min max [WITHSCORES] [LIMIT offset count]
     * @param key
     * @return
     */
    public Set<String> zrange(String key, long min, long max) {
        Set<String> value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                value = jedis.zrange(key,min,max);
                logger.debug("getZset {} = {}", key, value);
            }
        } catch (Exception e) {
            logger.warn("getZset {} = {}", key, value, e);
        } finally {
            jedis.close();
        }
        return value;
    }

    public void pipeline(Map<String,String> map){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            Pipeline pipeline = jedis.pipelined();
            for(Map.Entry<String,String> entry:map.entrySet()){
                pipeline.set(entry.getKey().getBytes(),entry.getValue().getBytes());
            }
            pipeline.sync();
        }catch (Exception e) {
            logger.warn("setPipeline {}", map, e);
        } finally {
            jedis.close();
        }
    }

    public String setObject(String key,T t,int cacheSeconds){
        RuntimeSchema<T> schema = (RuntimeSchema<T>) RuntimeSchema.createFrom(t.getClass());
        byte[] bytes = ProtostuffIOUtil.toByteArray(t,
                schema,
                LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));

        Jedis jedis = null;
        String result = null;
        try{
            jedis = jedisPool.getResource();
            if (cacheSeconds > 0) {
                result = jedis.setex(key.getBytes(),cacheSeconds,bytes);
            }else{
                result = jedis.set(key.getBytes(),bytes);
            }
            logger.debug("setObject {} = {}", key, t.getClass());
        } catch (Exception e) {
            logger.warn("setObject {} = {}", key, t.getClass(), e);
        } finally {
            jedis.close();
        }
        return result;
    }

    public <T> T getObject(String key,Class<T> t) {
        RuntimeSchema<T> schema = RuntimeSchema.createFrom(t);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] bytes = jedis.get(key.getBytes());
            //缓存重获取到
            if (bytes != null) {
                T tt = schema.newMessage();
                ProtostuffIOUtil.mergeFrom(bytes,tt, schema);
                //t被反序列化
                logger.debug("getObject {} = {}", key, t);
                return tt;
            }
        } catch (Exception e) {
            logger.warn("getObject {} = {}", key, t, e);
        } finally {
            jedis.close();
        }
        return null;
    }

    public <T> T getObject(String key,TypeReference<T> typeReference) {
        RuntimeSchema<T> schema = RuntimeSchema.createFrom((Class<T>) typeReference.getType().getClass());
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] bytes = jedis.get(key.getBytes());
            //缓存重获取到
            if (bytes != null) {
                T tt = schema.newMessage();
                ProtostuffIOUtil.mergeFrom(bytes,tt, schema);
                //t被反序列化
                //logger.debug("getObject {} = {}", key, t);
                return (T)tt;
            }
        } catch (Exception e) {
            //logger.warn("getObject {} = {}", key, t, e);
        } finally {
            jedis.close();
        }
        return null;
    }
}

