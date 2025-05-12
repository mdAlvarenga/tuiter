package com.challenge.tuiter.infraestructura.evento.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PublicadorDeEventosKafkaTest {
  @Test
  void publicaEventoEnKafka() {
    KafkaTemplate<String, Object> kafkaTemplate = mock(KafkaTemplate.class);
    PublicadorDeEventosKafka publicador = new PublicadorDeEventosKafka(kafkaTemplate);
    Object evento = new Object();

    publicador.publicar(evento);

    verify(kafkaTemplate).send("tuits-publicados", evento);
  }
}
