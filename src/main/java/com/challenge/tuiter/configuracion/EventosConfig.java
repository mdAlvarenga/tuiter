package com.challenge.tuiter.configuracion;

import com.challenge.tuiter.aplicacion.evento.DespachadorDeEventos;
import com.challenge.tuiter.aplicacion.evento.PublicadorDeEventos;
import com.challenge.tuiter.dominio.comun.evento.ManejadorDeEvento;
import com.challenge.tuiter.dominio.seguimiento.RepositorioDeConsultaDeSeguimientos;
import com.challenge.tuiter.dominio.timeline.RepositorioDeEscrituraDeTimeline;
import com.challenge.tuiter.infraestructura.evento.ManejadorDeEventoDeTuitPublicadoLog;
import com.challenge.tuiter.infraestructura.evento.PublicadorDeEventosCompuesto;
import com.challenge.tuiter.infraestructura.evento.PublicadorDeEventosSincrono;
import com.challenge.tuiter.infraestructura.evento.kafka.EscuchadorDeTuitsPublicadosKafka;
import com.challenge.tuiter.infraestructura.evento.kafka.PublicadorDeEventosKafka;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
public class EventosConfig {
  @Bean
  public DespachadorDeEventos despachadorDeEventos(List<ManejadorDeEvento<?>> manejadores) {
    return new DespachadorDeEventos(manejadores);
  }

  @Bean
  public PublicadorDeEventosSincrono publicadorDeEventosSincrono(DespachadorDeEventos despachador) {
    return new PublicadorDeEventosSincrono(despachador);
  }

  @Bean
  public ManejadorDeEventoDeTuitPublicadoLog manejadorDeEventoDeTuitPublicadoLog() {
    return new ManejadorDeEventoDeTuitPublicadoLog();
  }

  @Bean
  public EscuchadorDeTuitsPublicadosKafka escuchadorDeTuitsPublicadosKafka(RepositorioDeConsultaDeSeguimientos seguimientoRepo, RepositorioDeEscrituraDeTimeline timelineRepo) {
    return new EscuchadorDeTuitsPublicadosKafka(seguimientoRepo, timelineRepo);
  }

  @Bean
  public PublicadorDeEventos publicadorDeEventosCompuesto(PublicadorDeEventosSincrono sincrono, PublicadorDeEventosKafka kafka) {
    return new PublicadorDeEventosCompuesto(List.of(sincrono, kafka));
  }
}
