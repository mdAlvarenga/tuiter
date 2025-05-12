package com.challenge.tuiter.infraestructura.evento.kafka;

import com.challenge.tuiter.aplicacion.evento.PublicadorDeEventos;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class PublicadorDeEventosKafka implements PublicadorDeEventos {
  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public void publicar(Object evento) {
    kafkaTemplate.send("tuits-publicados", evento);
  }
}
