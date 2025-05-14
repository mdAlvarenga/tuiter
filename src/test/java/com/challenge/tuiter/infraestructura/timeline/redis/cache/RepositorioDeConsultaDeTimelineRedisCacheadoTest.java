package com.challenge.tuiter.infraestructura.timeline.redis.cache;

import com.challenge.tuiter.dominio.tuit.RepositorioDeBusquedaTuits;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.timeline.dto.TuitDto;
import com.challenge.tuiter.infraestructura.timeline.redis.ClaveDeTipo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class RepositorioDeConsultaDeTimelineRedisCacheadoTest {

  private StringRedisTemplate redis;
  private ZSetOperations<String, String> zsetOps;
  private ValueOperations<String, String> valueOps;
  private RepositorioDeBusquedaTuits repoPostgres;
  private ObjectMapper objectMapper;
  private CacheadorDeTuits cacheador;
  private RepositorioDeConsultaDeTimelineRedisCacheado adapter;

  @BeforeEach
  void setUp() {
    redis = mock(StringRedisTemplate.class);
    zsetOps = mock(ZSetOperations.class);
    valueOps = mock(ValueOperations.class);
    repoPostgres = mock(RepositorioDeBusquedaTuits.class);
    cacheador = mock(CacheadorDeTuits.class);

    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    when(redis.opsForZSet()).thenReturn(zsetOps);
    when(redis.opsForValue()).thenReturn(valueOps);

    adapter = new RepositorioDeConsultaDeTimelineRedisCacheado(redis, repoPostgres, objectMapper,
      cacheador);
  }

  @Test
  void timelineConTodosLosTuitsEnCache() throws Exception {
    Usuario usuario = new Usuario("juan");
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    Tuit tuit1 = Tuit.desde(id1, usuario, "Hola", Instant.now());
    Tuit tuit2 = Tuit.desde(id2, usuario, "Mundo", Instant.now());

    Set<String> ids = new LinkedHashSet<>(List.of(id1.toString(), id2.toString()));
    when(zsetOps.reverseRange(ClaveDeTipo.TIMELINE.con("juan"), 0, 49)).thenReturn(ids);
    when(valueOps.multiGet(List.of(ClaveDeTipo.TUIT.con(id1.toString()),
      ClaveDeTipo.TUIT.con(id2.toString())))).thenReturn(
      List.of(objectMapper.writeValueAsString(TuitDto.desdeTuit(tuit1)),
        objectMapper.writeValueAsString(TuitDto.desdeTuit(tuit2))));


    List<Tuit> resultado = adapter.timelineDe(usuario);


    assertEquals(2, resultado.size());
    assertEquals(id1, resultado.get(0).getId());
    assertEquals(id2, resultado.get(1).getId());
    verifyNoInteractions(repoPostgres);
    verifyNoInteractions(cacheador);
  }

  @Test
  void timelineConFaltantesHaceFallbackAPostgres() throws Exception {
    Usuario usuario = new Usuario("ana");
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    Tuit tuit2 = Tuit.desde(id2, usuario, "Fallback", Instant.now());

    Set<String> ids = new LinkedHashSet<>(List.of(id1.toString(), id2.toString()));
    when(zsetOps.reverseRange(ClaveDeTipo.TIMELINE.con("ana"), 0, 49)).thenReturn(ids);

    List<String> respuestasCache = Arrays.asList(null,
      objectMapper.writeValueAsString(TuitDto.desdeTuit(tuit2)));
    when(valueOps.multiGet(anyList())).thenReturn(respuestasCache);

    Tuit tuitFaltante = Tuit.desde(id1, usuario, "Desde DB", Instant.now());
    when(repoPostgres.buscarTodosPorId(anyList())).thenReturn(List.of(tuitFaltante));


    adapter.timelineDe(usuario);


    verify(cacheador).cachear(List.of(tuitFaltante));
  }

  @Test
  void timelineVacioDevuelveListaVacia() {
    Usuario usuario = new Usuario("pepe");
    when(zsetOps.reverseRange(ClaveDeTipo.TIMELINE.con("pepe"), 0, 49)).thenReturn(
      Collections.emptySet());

    List<Tuit> resultado = adapter.timelineDe(usuario);

    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());
    verifyNoInteractions(valueOps);
    verifyNoInteractions(repoPostgres);
    verifyNoInteractions(cacheador);
  }
}
