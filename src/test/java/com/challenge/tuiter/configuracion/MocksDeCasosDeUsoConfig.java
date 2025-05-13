package com.challenge.tuiter.configuracion;

import com.challenge.tuiter.aplicacion.publicacion.PublicadorDeTuits;
import com.challenge.tuiter.aplicacion.seguimiento.BuscadorDeSeguidores;
import com.challenge.tuiter.aplicacion.seguimiento.SeguidorDeUsuarios;
import com.challenge.tuiter.aplicacion.timeline.ExploradorDeTimeline;
import com.challenge.tuiter.dominio.tuit.RepositorioDeGuardadoTuits;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MocksDeCasosDeUsoConfig {
  @Bean
  public RepositorioDeGuardadoTuits mockReporGuardadoTuits() {
    return Mockito.mock(RepositorioDeGuardadoTuits.class);
  }

  @Bean
  public PublicadorDeTuits mockPublicadorDeTuits() {
    return Mockito.mock(PublicadorDeTuits.class);
  }

  @Bean
  public SeguidorDeUsuarios mockSeguidorDeUsuarios() {
    return Mockito.mock(SeguidorDeUsuarios.class);
  }

  @Bean
  public BuscadorDeSeguidores mockBuscadorDeSeguidores() {
    return Mockito.mock(BuscadorDeSeguidores.class);
  }

  @Bean
  public ExploradorDeTimeline mockExploradorDeTimeline() {
    return Mockito.mock(ExploradorDeTimeline.class);
  }
}
