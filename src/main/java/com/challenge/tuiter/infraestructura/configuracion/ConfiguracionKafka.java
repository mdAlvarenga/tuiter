package com.challenge.tuiter.infraestructura.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class ConfiguracionKafka {
  @Bean
  public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> factory) {
    return new KafkaTemplate<>(factory);
  }
}
