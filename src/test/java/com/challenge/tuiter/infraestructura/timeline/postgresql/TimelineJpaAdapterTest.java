package com.challenge.tuiter.infraestructura.timeline.postgresql;

import com.challenge.tuiter.configuracion.CasosDeUsoTestConfig;
import com.challenge.tuiter.dominio.timeline.RepositorioDeConsultaDeTimeline;
import com.challenge.tuiter.dominio.timeline.RepositorioDeEscrituraDeTimeline;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.errores.ManejadorDeErrores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@Import({CasosDeUsoTestConfig.class, ManejadorDeErrores.class})
@ActiveProfiles("test")
class TimelineJpaAdapterTest {
  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
  private final Clock reloj = Clock.fixed(Instant.parse("2025-05-10T15:00:00Z"), ZoneOffset.UTC);
  @Autowired
  RepositorioDeEscrituraDeTimeline repoEscritura;

  @Autowired
  RepositorioDeConsultaDeTimeline repoConsulta;

  @DynamicPropertySource
  static void registrar(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Test
  void guardaYConsultaTuitsEnTimeline() {
    Usuario propietario = new Usuario("propietario");
    Usuario juan = new Usuario("juan");
    Usuario ana = new Usuario("ana");

    Tuit tuit1 = Tuit.nuevo(juan, "Hola soy juan", reloj);
    Tuit tuit2 = Tuit.nuevo(ana, "Hola soy ana", reloj);

    repoEscritura.publicarTuit(propietario, tuit1);
    repoEscritura.publicarTuit(propietario, tuit2);

    List<Tuit> timeline = repoConsulta.timelineDe(propietario);
    assertEquals(2, timeline.size());

    List<String> contenidos = timeline.stream().map(Tuit::getContenido).toList();
    assertTrue(contenidos.contains("Hola soy juan"));
    assertTrue(contenidos.contains("Hola soy ana"));
  }
}
