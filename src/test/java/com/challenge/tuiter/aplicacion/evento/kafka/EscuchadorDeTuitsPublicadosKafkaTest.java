package com.challenge.tuiter.aplicacion.evento.kafka;

import com.challenge.tuiter.dominio.seguimiento.RepositorioDeConsultaDeSeguimientos;
import com.challenge.tuiter.dominio.timeline.RepositorioDeEscrituraDeTimeline;
import com.challenge.tuiter.dominio.tuit.evento.EventoDeTuitPublicado;
import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.evento.kafka.EscuchadorDeTuitsPublicadosKafka;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EscuchadorDeTuitsPublicadosKafkaTest {
  @Test
  void haceFanoutDelTuitAlTimelineDeLosSeguidores() {
    var evento = new EventoDeTuitPublicado("00000000-0000-0000-0000-000000000001", "ana",
      "contenido", Instant.now());
    var seguidor1 = new Usuario("luz");
    var seguidor2 = new Usuario("juan");

    var seguidoresRepo = mock(RepositorioDeConsultaDeSeguimientos.class);
    var timelineRepo = mock(RepositorioDeEscrituraDeTimeline.class);
    var escuchador = new EscuchadorDeTuitsPublicadosKafka(seguidoresRepo, timelineRepo);

    when(seguidoresRepo.seguidoresDe(new Usuario("ana"))).thenReturn(List.of(seguidor1, seguidor2));


    escuchador.escuchar(evento);


    verify(timelineRepo).publicarTuit(seguidor1, evento.aTuit());
    verify(timelineRepo).publicarTuit(seguidor2, evento.aTuit());
  }
}
