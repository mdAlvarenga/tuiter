package com.challenge.tuiter.infraestructura.tuit.postgresql;

import com.challenge.tuiter.configuracion.CasosDeUsoTestConfig;
import com.challenge.tuiter.dominio.tuit.RepositorioDeBusquedaTuits;
import com.challenge.tuiter.dominio.tuit.RepositorioDeGuardadoTuits;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@Import({CasosDeUsoTestConfig.class, ManejadorDeErrores.class})
@ActiveProfiles("test")
class TuitsJpaAdapterTest {
  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
  private final Clock reloj = Clock.fixed(Instant.parse("2025-05-10T12:00:00Z"), ZoneOffset.UTC);
  @Autowired
  RepositorioDeGuardadoTuits repoGuardar;

  @Autowired
  RepositorioDeBusquedaTuits repoBusqueda;

  @DynamicPropertySource
  static void propiedades(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Test
  void guardaYRecuperaUnTuit() {
    Usuario autor = new Usuario("juan");
    Tuit original = Tuit.nuevo(autor, "Hola mundo", reloj);
    Tuit guardado = repoGuardar.guardar(original);

    Optional<Tuit> recuperado = repoBusqueda.buscarPorId(guardado.getId().toString());

    assertTrue(recuperado.isPresent());
    assertEquals("Hola mundo", recuperado.get().getContenido());
    assertEquals("juan", recuperado.get().getAutor().id());
    assertEquals(guardado.getId(), recuperado.get().getId());
  }
}
