package com.challenge.tuiter.configuracion;

import com.challenge.tuiter.aplicacion.evento.DespachadorDeEventos;
import com.challenge.tuiter.aplicacion.evento.PublicadorDeEventos;
import com.challenge.tuiter.aplicacion.publicacion.PublicadorDeTuits;
import com.challenge.tuiter.aplicacion.seguimiento.BuscadorDeSeguidores;
import com.challenge.tuiter.aplicacion.seguimiento.SeguidorDeUsuarios;
import com.challenge.tuiter.aplicacion.timeline.ExploradorDeTimeline;
import com.challenge.tuiter.dominio.comun.evento.ManejadorDeEvento;
import com.challenge.tuiter.dominio.seguimiento.RepositorioDeConsultaDeSeguimientos;
import com.challenge.tuiter.dominio.seguimiento.RepositorioDeRegistroDeSeguimientos;
import com.challenge.tuiter.dominio.timeline.RepositorioDeConsultaDeTimeline;
import com.challenge.tuiter.dominio.timeline.RepositorioDeEscrituraDeTimeline;
import com.challenge.tuiter.dominio.tuit.RepositorioDeGuardadoTuits;
import com.challenge.tuiter.infraestructura.evento.ManejadorDeEventoDeTuitPublicadoLog;
import com.challenge.tuiter.infraestructura.evento.PublicadorDeEventosSincrono;
import com.challenge.tuiter.infraestructura.evento.kafka.EscuchadorDeTuitsPublicadosKafka;
import com.challenge.tuiter.infraestructura.evento.kafka.PublicadorDeEventosKafka;
import com.challenge.tuiter.infraestructura.seguimiento.postgresql.SeguimientosJpaAdapter;
import com.challenge.tuiter.infraestructura.tuit.postgresql.TuitJpaRepository;
import com.challenge.tuiter.infraestructura.tuit.postgresql.TuitsJpaAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Clock;
import java.util.List;

@Configuration
@Profile("!test")
public class CasosDeUsoConfig {
  @Bean
  public PublicadorDeTuits publicadorDeTuits(RepositorioDeGuardadoTuits repositorio, RepositorioDeEscrituraDeTimeline timelineRepo, RepositorioDeConsultaDeSeguimientos seguimientoRepo, PublicadorDeEventos publicador, Clock clock) {
    return new PublicadorDeTuits(repositorio, timelineRepo, seguimientoRepo, publicador, clock);
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
  public DespachadorDeEventos despachadorDeEventos(List<ManejadorDeEvento<?>> manejadores) {
    return new DespachadorDeEventos(manejadores);
  }

  @Bean
  public PublicadorDeEventos publicadorDeEventosSincrono(DespachadorDeEventos despachador) {
    return new PublicadorDeEventosSincrono(despachador);
  }

  @Bean
  public ManejadorDeEventoDeTuitPublicadoLog manejadorDeEventoDeTuitPublicadoLog() {
    return new ManejadorDeEventoDeTuitPublicadoLog();
  }

  @Bean
  public PublicadorDeEventosKafka publicadorDeEventosKafka(KafkaTemplate<String, Object> kafkaTemplate) {
    return new PublicadorDeEventosKafka(kafkaTemplate);
  }

  @Bean
  public EscuchadorDeTuitsPublicadosKafka escuchadorDeTuitsPublicadosKafka(RepositorioDeConsultaDeSeguimientos repositorio, RepositorioDeEscrituraDeTimeline timeline) {
    return new EscuchadorDeTuitsPublicadosKafka(repositorio, timeline);
  }
}
