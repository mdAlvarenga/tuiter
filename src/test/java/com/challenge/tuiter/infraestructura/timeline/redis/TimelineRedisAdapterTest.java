package com.challenge.tuiter.infraestructura.timeline.redis;

import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TimelineRedisAdapterTest {
  private ZSetOperations<String, String> zsetOps;
  private CacheadorDeTuits cacheador;
  private TimelineRedisAdapter adapter;

  @BeforeEach
  void setUp() {
    StringRedisTemplate redisTemplate = mock(StringRedisTemplate.class);
    zsetOps = mock(ZSetOperations.class);
    cacheador = mock(CacheadorDeTuits.class);
    when(redisTemplate.opsForZSet()).thenReturn(zsetOps);
    adapter = new TimelineRedisAdapter(redisTemplate, cacheador);
  }

  @Test
  void publicaTuitEnRedisConClaveCorrectaYCachea() {
    var userId = UUID.randomUUID();
    var tuitId = UUID.randomUUID();
    var usuario = new Usuario(userId.toString());
    var instante = Instant.parse("2025-01-01T12:00:00Z");
    var tuit = Tuit.desde(tuitId, usuario, "texto", instante);

    var clave = ClaveDeTipo.TIMELINE.con(userId.toString());
    var tuitStr = tuitId.toString();
    var score = (double) instante.getEpochSecond();

    when(zsetOps.score(clave, tuitStr)).thenReturn(null);

    adapter.publicarTuit(usuario, tuit);

    verify(zsetOps).add(clave, tuitStr, score);
    verify(cacheador).cachear(tuit);
  }

  @Test
  void publicaTuitUsandoTimestampComoScore() {
    var userId = UUID.randomUUID();
    var tuitId = UUID.randomUUID();
    var instante = Instant.parse("2024-10-10T15:30:00Z");
    var usuario = new Usuario(userId.toString());
    var tuit = Tuit.desde(tuitId, usuario, "contenido", instante);

    var clave = ClaveDeTipo.TIMELINE.con(userId.toString());
    var tuitStr = tuitId.toString();

    when(zsetOps.score(clave, tuitStr)).thenReturn(null);

    adapter.publicarTuit(usuario, tuit);

    ArgumentCaptor<Double> scoreCaptor = ArgumentCaptor.forClass(Double.class);
    verify(zsetOps).add(anyString(), anyString(), scoreCaptor.capture());

    assertEquals((double) instante.getEpochSecond(), scoreCaptor.getValue());
  }

  @Test
  void noPublicaTuitDuplicadoNiLoCachea() {
    var userId = UUID.randomUUID();
    var tuitId = UUID.randomUUID();
    var usuario = new Usuario(userId.toString());
    var instante = Instant.now();
    var tuit = Tuit.desde(tuitId, usuario, "texto", instante);

    var clave = ClaveDeTipo.TIMELINE.con(userId.toString());
    var tuitStr = tuitId.toString();
    var score = (double) instante.getEpochSecond();

    when(zsetOps.score(clave, tuitStr)).thenReturn(null).thenReturn(score);

    adapter.publicarTuit(usuario, tuit);
    adapter.publicarTuit(usuario, tuit);

    verify(zsetOps, times(2)).score(clave, tuitStr);
    verify(zsetOps, times(1)).add(clave, tuitStr, score);
    verify(cacheador, times(1)).cachear(tuit);
  }

  @Test
  void eliminaTuitsAntiguosSiSuperaElMaximoPermitido() {
    var userId = UUID.randomUUID();
    var tuitId = UUID.randomUUID();
    var usuario = new Usuario(userId.toString());
    var instante = Instant.now();
    var tuit = Tuit.desde(tuitId, usuario, "texto", instante);
    var clave = ClaveDeTipo.TIMELINE.con(userId.toString());
    var tuitStr = tuitId.toString();
    when(zsetOps.score(clave, tuitStr)).thenReturn(null);

    adapter.publicarTuit(usuario, tuit);

    verify(zsetOps).removeRange(clave, 0, -1001);
  }
}