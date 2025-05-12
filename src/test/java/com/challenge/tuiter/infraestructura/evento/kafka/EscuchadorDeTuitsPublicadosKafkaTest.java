package com.challenge.tuiter.infraestructura.evento.kafka;

import com.challenge.tuiter.dominio.seguimiento.RepositorioDeConsultaDeSeguimientos;
import com.challenge.tuiter.dominio.timeline.RepositorioDeEscrituraDeTimeline;
import com.challenge.tuiter.dominio.tuit.evento.EventoDeTuitPublicado;
import com.challenge.tuiter.dominio.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class EscuchadorDeTuitsPublicadosKafkaTest {
  private RepositorioDeConsultaDeSeguimientos repoSeguidores;
  private RepositorioDeEscrituraDeTimeline repoTimeline;
  private EscuchadorDeTuitsPublicadosKafka escuchador;

  @BeforeEach
  void setUp() {
    repoSeguidores = mock(RepositorioDeConsultaDeSeguimientos.class);
    repoTimeline = mock(RepositorioDeEscrituraDeTimeline.class);
    escuchador = new EscuchadorDeTuitsPublicadosKafka(repoSeguidores, repoTimeline);
  }

  @Test
  void escuchaEventoYPublicaTuitEnTimelinesDeSeguidores() {
    var autorId = UUID.randomUUID().toString();
    var seguidor1 = new Usuario(UUID.randomUUID().toString());
    var seguidor2 = new Usuario(UUID.randomUUID().toString());

    var evento = new EventoDeTuitPublicado(UUID.randomUUID().toString(), autorId, "Hola mundo",
      Instant.now());

    when(repoSeguidores.seguidoresDe(new Usuario(autorId))).thenReturn(
      List.of(seguidor1, seguidor2));


    escuchador.escuchar(evento);


    var tuitEsperado = evento.aTuit();
    verify(repoTimeline).publicarTuit(seguidor1, tuitEsperado);
    verify(repoTimeline).publicarTuit(seguidor2, tuitEsperado);
    verify(repoTimeline, times(2)).publicarTuit(any(), eq(tuitEsperado));
  }

  @Test
  void noHaceNadaSiElAutorNoTieneSeguidores() {
    var autorId = UUID.randomUUID().toString();
    var evento = new EventoDeTuitPublicado(UUID.randomUUID().toString(), autorId, "Sin seguidores",
      Instant.now());

    when(repoSeguidores.seguidoresDe(new Usuario(autorId))).thenReturn(List.of());

    escuchador.escuchar(evento);

    verifyNoInteractions(repoTimeline);
  }
}
