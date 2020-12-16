package com.syf.papermanager.config.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.config.redis
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/15 16:38
 */
@Configuration
@Data
public class JedisConfig {
    @Value("${spring.redis.database}")
    private Integer database;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private Integer port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private Integer timeout;
    @Value("${spring.redis.jedis.pool.max-active}")
    private Integer maxActive;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private Integer maxIdle;
    @Value("${spring.redis.jedis.pool.min-idle}")
    private Integer minIdle;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private Integer maxWait;


    @Bean
    public JedisPool JedisPoolFactory(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWait);
        config.setMaxTotal(maxActive);
        return new JedisPool(config, host, port, timeout, password, database, null);
    }
}
