package com.challenge.tuiter.configuracion;

import com.challenge.tuiter.aplicacion.publicacion.PublicadorDeTuits;
import com.challenge.tuiter.dominio.tuit.RepositorioDeGuardadoTuits;
import com.challenge.tuiter.infraestructura.tuit.postgresql.TuitJpaRepository;
import com.challenge.tuiter.infraestructura.tuit.postgresql.TuitsJpaAdapter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;

@Configuration
@Profile("!test")
public class CasosDeUsoConfig {
  @Bean
  public PublicadorDeTuits publicadorDeTuits(RepositorioDeGuardadoTuits repositorio, Clock clock) {
    return new PublicadorDeTuits(repositorio, clock);
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
  public CommandLineRunner runner(ApplicationContext ctx) {
    return args -> {
      System.out.println("¿Publicador? " + ctx.containsBeanDefinition("publicadorDeTuits"));
      System.out.println("¿TuitsJpaAdapter? " + ctx.getBeansOfType(RepositorioDeGuardadoTuits.class));
      System.out.println("¿TuitJpaRepository? " + ctx.getBeansOfType(TuitJpaRepository.class));
    };
  }
}
