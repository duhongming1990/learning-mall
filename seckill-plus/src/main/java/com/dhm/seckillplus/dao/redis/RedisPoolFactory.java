package com.dhm.seckillplus.dao.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisPoolFactory {

    @Bean(name = "jedis.pool")
    public JedisPool jedisPool(@Qualifier("jedis.pool.config") JedisPoolConfig poolConfig,
                               @Value("${redis.host}") String host,
                               @Value("${redis.port}") int port,
                               @Value("${redis.password}") String password,
                               @Value("${redis.timeoutMillis}") int timeout){
        return new JedisPool(poolConfig,host,port,timeout,password);
    }

    @Bean(name = "jedis.pool.config")
    public JedisPoolConfig jedisPoolConfig(
            @Value("${redis.poolMaxTotal}") int maxTotal,
            @Value("${redis.poolMaxIdle}") int maxIdle,
            @Value("${redis.poolMaxWaitMillis}") int maxWaitMillis,
            @Value("${redis.testOnBorrow}") boolean testOnBorrow){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setTestOnBorrow(testOnBorrow);
        return config;
    }
}
