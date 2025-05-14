package com.challenge.tuiter.infraestructura.timeline.redis;

import com.challenge.tuiter.dominio.timeline.RepositorioDeEscrituraDeTimeline;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.timeline.redis.cache.CacheadorDeTuits;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

@Slf4j
public class TimelineRedisAdapter implements RepositorioDeEscrituraDeTimeline {
  private static final int MAX_TIMELINE = 1000;

  private final StringRedisTemplate redis;
  private final CacheadorDeTuits cacheador;

  public TimelineRedisAdapter(StringRedisTemplate redis, CacheadorDeTuits cacheador) {
    this.redis = redis;
    this.cacheador = cacheador;
  }

  @Override
  public void publicarTuit(Usuario usuario, Tuit tuit) {
    String propietario = ClaveDeTipo.TIMELINE.con(usuario.id());
    String tuitId = tuit.getId().toString();
    double puntaje = tuit.getInstanteDeCreacion().getEpochSecond();

    Double existente = redis.opsForZSet().score(propietario, tuitId);
    if (existente != null) {
      log.info("Tuit ya existe en timeline con score: {}", existente);
      return;
    }

    redis.opsForZSet().add(propietario, tuitId, puntaje);
    redis.opsForZSet().removeRange(propietario, 0, -MAX_TIMELINE - 1);

    cacheador.cachear(tuit);
  }
}
