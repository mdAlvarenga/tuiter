package com.challenge.tuiter.configuracion;

import com.challenge.tuiter.aplicacion.publicacion.PublicadorDeTuits;
import com.challenge.tuiter.dominio.tuit.RepositorioDeGuardadoTuits;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@TestConfiguration
public class CasosDeUsoTestConfig {
  @Bean
  public Clock fixedClock() {
    return Clock.fixed(Instant.parse("2025-05-10T12:00:00Z"), ZoneOffset.UTC);
  }

  @Bean
  public RepositorioDeGuardadoTuits repositorioFake() {
    return Mockito.mock(RepositorioDeGuardadoTuits.class);
  }

  @Bean
  public PublicadorDeTuits publicadorDeTuits(RepositorioDeGuardadoTuits repositorio, Clock clock) {
    return Mockito.mock(PublicadorDeTuits.class);
  }
}
