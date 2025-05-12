package com.challenge.tuiter.infraestructura.evento.kafka;

import com.challenge.tuiter.dominio.seguimiento.RepositorioDeConsultaDeSeguimientos;
import com.challenge.tuiter.dominio.timeline.RepositorioDeEscrituraDeTimeline;
import com.challenge.tuiter.dominio.tuit.evento.EventoDeTuitPublicado;
import com.challenge.tuiter.dominio.usuario.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@RequiredArgsConstructor
public class EscuchadorDeTuitsPublicadosKafka {
  private final RepositorioDeConsultaDeSeguimientos repoSeguidores;
  private final RepositorioDeEscrituraDeTimeline repoTimeline;

  @KafkaListener(topics = "tuits-publicados", groupId = "tuiter")
  public void escuchar(EventoDeTuitPublicado evento) {
    log.info("Recibido evento de tuit publicado: {}", evento);

    var autor = new Usuario(evento.autorId());
    var seguidores = repoSeguidores.seguidoresDe(autor);
    seguidores.forEach(seguidor -> {
      repoTimeline.publicarTuit(seguidor, evento.aTuit());
    });
  }
}
