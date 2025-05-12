package com.challenge.tuiter.configuracion;

import com.challenge.tuiter.aplicacion.evento.PublicadorDeEventos;
import com.challenge.tuiter.aplicacion.publicacion.PublicadorDeTuits;
import com.challenge.tuiter.aplicacion.seguimiento.BuscadorDeSeguidores;
import com.challenge.tuiter.aplicacion.seguimiento.SeguidorDeUsuarios;
import com.challenge.tuiter.aplicacion.timeline.ExploradorDeTimeline;
import com.challenge.tuiter.dominio.seguimiento.RepositorioDeConsultaDeSeguimientos;
import com.challenge.tuiter.dominio.timeline.RepositorioDeConsultaDeTimeline;
import com.challenge.tuiter.dominio.timeline.RepositorioDeEscrituraDeTimeline;
import com.challenge.tuiter.dominio.tuit.RepositorioDeGuardadoTuits;
import com.challenge.tuiter.infraestructura.seguimiento.postgresql.SeguimientosJpaAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;

@Configuration
@Profile("!test")
public class AplicacionConfig {
  @Bean
  public PublicadorDeTuits publicadorDeTuits(RepositorioDeGuardadoTuits repositorio, RepositorioDeEscrituraDeTimeline timelineRepo, RepositorioDeConsultaDeSeguimientos seguimientoRepo,
                                             @Qualifier("publicadorDeEventosCompuesto")
                                             PublicadorDeEventos publicador, Clock clock) {
    return new PublicadorDeTuits(repositorio, timelineRepo, seguimientoRepo, publicador, clock);
  }

  @Bean
  public SeguidorDeUsuarios seguidorDeUsuarios(SeguimientosJpaAdapter repo) {
    return new SeguidorDeUsuarios(repo, repo);
  }

  @Bean
  public BuscadorDeSeguidores buscadorDeSeguidores(RepositorioDeConsultaDeSeguimientos repo) {
    return new BuscadorDeSeguidores(repo);
  }

  @Bean
  public ExploradorDeTimeline exploradorDeTimeline(RepositorioDeConsultaDeTimeline repoTimeline, RepositorioDeConsultaDeSeguimientos repoSeguimientos) {
    return new ExploradorDeTimeline(repoTimeline, repoSeguimientos);
  }

  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }
}
