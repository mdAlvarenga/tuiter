package com.challenge.tuiter.integracion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@TestConfiguration
public class RedisTestConfig {
  @Value("${spring.redis.host}")
  private String redisHost;

  @Value("${spring.redis.port}")
  private int redisPort;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    var conf = new RedisStandaloneConfiguration(redisHost, redisPort);
    return new LettuceConnectionFactory(conf);
  }

  @Bean
  public StringRedisTemplate redisTemplate(RedisConnectionFactory factory) {
    return new StringRedisTemplate(factory);
  }
}
