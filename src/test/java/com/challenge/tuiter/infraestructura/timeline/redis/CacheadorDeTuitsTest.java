package com.challenge.tuiter.infraestructura.timeline.redis;

import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.timeline.dto.TuitDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Clock;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CacheadorDeTuitsTest {
  private StringRedisTemplate redis;
  private ObjectMapper objectMapper;
  private ValueOperations<String, String> valueOperations;
  private CacheadorDeTuits cacheador;

  @BeforeEach
  void setUp() {
    redis = mock(StringRedisTemplate.class);
    objectMapper = new ObjectMapper();
    valueOperations = mock(ValueOperations.class);
    objectMapper.registerModule(new JavaTimeModule());

    when(redis.opsForValue()).thenReturn(valueOperations);

    cacheador = new CacheadorDeTuits(redis, objectMapper);
  }

  @Test
  void cachearUnSoloTuitSerializaYGuardaEnRedis() {
    var tuit = Tuit.nuevo(new Usuario("juan"), "hola mundo", Clock.systemUTC());
    cacheador.cachear(tuit);

    String expectedKey = "tuit:" + tuit.getId();
    ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

    verify(valueOperations).set(keyCaptor.capture(), valueCaptor.capture());

    assertEquals(expectedKey, keyCaptor.getValue());

    TuitDto deserializado = null;
    try {
      deserializado = objectMapper.readValue(valueCaptor.getValue(), TuitDto.class);
    } catch (Exception e) {
      throw new AssertionError("No se pudo deserializar el JSON", e);
    }

    assertEquals(tuit.getId(), deserializado.aTuit().getId());
    assertEquals(tuit.getContenido(), deserializado.aTuit().getContenido());
  }

  @Test
  void cachearListaDeTuitsLlamaCachearPorCadaUno() {
    var t1 = Tuit.nuevo(new Usuario("a"), "1", Clock.systemUTC());
    var t2 = Tuit.nuevo(new Usuario("b"), "2", Clock.systemUTC());

    cacheador.cachear(List.of(t1, t2));

    verify(valueOperations, times(2)).set(any(), any());
  }
}
