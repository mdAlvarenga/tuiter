package com.challenge.tuiter.infraestructura.configuracion;

import com.challenge.tuiter.dominio.seguimiento.RepositorioDeRegistroDeSeguimientos;
import com.challenge.tuiter.dominio.tuit.RepositorioDeGuardadoTuits;
import com.challenge.tuiter.infraestructura.evento.kafka.PublicadorDeEventosKafka;
import com.challenge.tuiter.infraestructura.seguimiento.postgresql.SeguimientosJpaAdapter;
import com.challenge.tuiter.infraestructura.tuit.postgresql.TuitJpaRepository;
import com.challenge.tuiter.infraestructura.tuit.postgresql.TuitsJpaAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class InfraestructuraConfig {
  @Bean
  public RepositorioDeGuardadoTuits repositorioDeGuardadoTuits(TuitJpaRepository jpa) {
    return new TuitsJpaAdapter(jpa);
  }

  @Bean
  public RepositorioDeRegistroDeSeguimientos repoRegistroDeSeguimientos(SeguimientosJpaAdapter adapter) {
    return adapter;
  }

  @Bean
  public PublicadorDeEventosKafka publicadorDeEventosKafka(KafkaTemplate<String, Object> kafkaTemplate) {
    return new PublicadorDeEventosKafka(kafkaTemplate);
  }
}
