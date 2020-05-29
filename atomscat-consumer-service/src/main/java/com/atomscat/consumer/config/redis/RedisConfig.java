package com.atomscat.consumer.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;

/**
 * @author Howell.Yang
 */
@Configuration
public class RedisConfig {

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        if (redisProperties.getSentinel() != null && redisProperties.getSentinel().getNodes().size() > 0) {
            // todo
        } else {
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
            config.setPassword(redisProperties.getPassword());
            config.setDatabase(redisProperties.getDatabase());
            return new JedisConnectionFactory(config);
        }
        return null;
    }



    /**
     * 非哨兵模式
     *
     * @return
     */
    @Bean
    public JedisPool jedisPool() {
        if (redisProperties.getSentinel() != null && redisProperties.getSentinel().getNodes().size() > 0) {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(jedisConnectionFactory.getSentinelConfiguration().getMaster().getName(),
                    new HashSet<>(redisProperties.getSentinel().getNodes()),
                    jedisConnectionFactory.getPoolConfig(), jedisConnectionFactory.getTimeout(), jedisConnectionFactory.getPassword(), jedisConnectionFactory.getDatabase());
            return new JedisPool(jedisPoolConfig,
                    jedisSentinelPool.getCurrentHostMaster().getHost(), jedisSentinelPool.getCurrentHostMaster().getPort(),
                    jedisConnectionFactory.getTimeout(), jedisConnectionFactory.getPassword());
        } else {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(1000);
            return new JedisPool(jedisPoolConfig,
                    jedisConnectionFactory.getHostName(), jedisConnectionFactory.getPort(),
                    jedisConnectionFactory.getTimeout(), jedisConnectionFactory.getPassword());
        }
    }

    /**
     * 哨兵模式
     *
     * @return
     */
    @Bean
    public JedisSentinelPool jedisSentinelPool() {
        if (redisProperties.getSentinel() != null && redisProperties.getSentinel().getNodes().size() > 0) {
            JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(jedisConnectionFactory.getSentinelConfiguration().getMaster().getName(),
                    new HashSet<>(redisProperties.getSentinel().getNodes()),
                    jedisConnectionFactory.getPoolConfig(), jedisConnectionFactory.getTimeout(), jedisConnectionFactory.getPassword(), jedisConnectionFactory.getDatabase());
            return jedisSentinelPool;
        } else {
            return null;
        }
    }


}