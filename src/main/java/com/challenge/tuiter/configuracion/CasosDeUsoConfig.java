package com.challenge.tuiter.configuracion;

import com.challenge.tuiter.aplicacion.evento.DespachadorDeEventos;
import com.challenge.tuiter.aplicacion.evento.ManejadorDeEventoDeTuitPublicadoLog;
import com.challenge.tuiter.aplicacion.publicacion.PublicadorDeTuits;
import com.challenge.tuiter.aplicacion.seguimiento.BuscadorDeSeguidores;
import com.challenge.tuiter.aplicacion.seguimiento.SeguidorDeUsuarios;
import com.challenge.tuiter.aplicacion.timeline.ExploradorDeTimeline;
import com.challenge.tuiter.dominio.seguimiento.RepositorioDeConsultaDeSeguimientos;
import com.challenge.tuiter.dominio.seguimiento.RepositorioDeRegistroDeSeguimientos;
import com.challenge.tuiter.dominio.timeline.RepositorioDeConsultaDeTimeline;
import com.challenge.tuiter.dominio.timeline.RepositorioDeEscrituraDeTimeline;
import com.challenge.tuiter.dominio.tuit.RepositorioDeGuardadoTuits;
import com.challenge.tuiter.infraestructura.seguimiento.postgresql.SeguimientosJpaAdapter;
import com.challenge.tuiter.infraestructura.tuit.postgresql.TuitJpaRepository;
import com.challenge.tuiter.infraestructura.tuit.postgresql.TuitsJpaAdapter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.util.List;

@Configuration
@Profile("!test")
public class CasosDeUsoConfig {
  @Bean
  public PublicadorDeTuits publicadorDeTuits(RepositorioDeGuardadoTuits repositorio, RepositorioDeEscrituraDeTimeline timelineRepo, RepositorioDeConsultaDeSeguimientos seguimientoRepo, DespachadorDeEventos despachador, Clock clock) {
    return new PublicadorDeTuits(repositorio, timelineRepo, seguimientoRepo, despachador, clock);
  }

  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }

  @Bean
  public RepositorioDeGuardadoTuits repositorioDeGuardadoTuits(TuitJpaRepository jpa) {
    return new TuitsJpaAdapter(jpa);
  }

  @Bean
  public SeguidorDeUsuarios seguidorDeUsuarios(SeguimientosJpaAdapter adapter) {
    return new SeguidorDeUsuarios(adapter, adapter);
  }

  @Bean
  public RepositorioDeRegistroDeSeguimientos repoRegistroDeSeguimientos(SeguimientosJpaAdapter adapter) {
    return adapter;
  }

  @Bean
  public BuscadorDeSeguidores buscadorDeSeguidores(RepositorioDeConsultaDeSeguimientos repositorio) {
    return new BuscadorDeSeguidores(repositorio);
  }

  @Bean
  public ExploradorDeTimeline exploradorDeTimeline(RepositorioDeConsultaDeTimeline repoTimeline, RepositorioDeConsultaDeSeguimientos repoSeguimientos) {
    return new ExploradorDeTimeline(repoTimeline, repoSeguimientos);
  }

  @Bean
  public CommandLineRunner runner(ApplicationContext ctx) {
    return args -> {
      System.out.println("¿Publicador? " + ctx.containsBeanDefinition("publicadorDeTuits"));
      System.out.println(
        "¿TuitsJpaAdapter? " + ctx.getBeansOfType(RepositorioDeGuardadoTuits.class));
      System.out.println("¿TuitJpaRepository? " + ctx.getBeansOfType(TuitJpaRepository.class));
    };
  }

  @Bean
  public DespachadorDeEventos despachadorDeEventos() {
    return new DespachadorDeEventos(List.of(new ManejadorDeEventoDeTuitPublicadoLog()));
  }
}
