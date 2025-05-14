package com.challenge.tuiter.infraestructura.timeline.redis.cache;

import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.infraestructura.timeline.dto.TuitDto;
import com.challenge.tuiter.infraestructura.timeline.redis.ClaveDeTipo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@Slf4j
public class CacheadorDeTuits {
  private final StringRedisTemplate redis;
  private final ObjectMapper objectMapper;

  public CacheadorDeTuits(StringRedisTemplate redis, ObjectMapper objectMapper) {
    this.redis = redis;
    this.objectMapper = objectMapper;
  }

  public void cachear(List<Tuit> tuits) {
    tuits.forEach(this::cachear);
  }

  public void cachear(Tuit tuit) {
    try {
      String clave = ClaveDeTipo.TUIT.con(tuit.getId().toString());
      String json = objectMapper.writeValueAsString(TuitDto.desdeTuit(tuit));
      redis.opsForValue().set(clave, json);
    } catch (JsonProcessingException e) {
      log.warn("No se pudo cachear tuit {}", tuit.getId(), e);
    }
  }
}