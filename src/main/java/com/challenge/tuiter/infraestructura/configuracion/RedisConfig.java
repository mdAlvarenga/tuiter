package com.challenge.tuiter.infraestructura.configuracion;

import com.challenge.tuiter.dominio.tuit.RepositorioDeBusquedaTuits;
import com.challenge.tuiter.infraestructura.timeline.redis.cache.CacheadorDeTuits;
import com.challenge.tuiter.infraestructura.timeline.redis.TimelineRedisAdapter;
import com.challenge.tuiter.infraestructura.timeline.redis.cache.RepositorioDeConsultaDeTimelineRedisCacheado;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {
  @Bean
  public CacheadorDeTuits cacheadorDeTuits(StringRedisTemplate redis, ObjectMapper objectMapper) {
    return new CacheadorDeTuits(redis, objectMapper);
  }

  @Bean
  public TimelineRedisAdapter timelineRedisAdapter(StringRedisTemplate redis, CacheadorDeTuits cacheador) {
    return new TimelineRedisAdapter(redis, cacheador);
  }

  @Bean
  public RepositorioDeConsultaDeTimelineRedisCacheado repositorioDeConsultaDeTimelineRedisCacheado(StringRedisTemplate redis, RepositorioDeBusquedaTuits repoPostgres, ObjectMapper objectMapper, CacheadorDeTuits cacheador) {
    return new RepositorioDeConsultaDeTimelineRedisCacheado(redis, repoPostgres, objectMapper,
      cacheador);
  }
}
