package com.challenge.tuiter.infraestructura.seguimiento.postgresql;

import com.challenge.tuiter.dominio.seguimiento.RelacionDeSeguimiento;
import com.challenge.tuiter.dominio.seguimiento.RepositorioDeConsultaDeSeguimientos;
import com.challenge.tuiter.dominio.seguimiento.RepositorioDeRegistroDeSeguimientos;
import com.challenge.tuiter.dominio.seguimiento.Seguimiento;
import com.challenge.tuiter.dominio.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class SeguimientosJpaAdapterTest {
  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
  private final String seguidor = "juan";
  private final String seguido1 = "ana";
  private final String seguido2 = "pablo";
  @Autowired
  RepositorioDeRegistroDeSeguimientos repoGuardar;
  @Autowired
  RepositorioDeConsultaDeSeguimientos repoConsulta;

  @DynamicPropertySource
  static void registrarPropiedades(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Test
  void guardaSeguimientosYConsultaQueExistanEnRepositorios() {
    Seguimiento seguimiento1 = Seguimiento.nuevo(seguidor, seguido1);
    Seguimiento seguimiento2 = Seguimiento.nuevo(seguidor, seguido2);

    repoGuardar.registrar(seguimiento1);
    repoGuardar.registrar(seguimiento2);

    assertTrue(repoConsulta.existe(new RelacionDeSeguimiento(seguidor, seguido1)));
    assertTrue(repoConsulta.existe(new RelacionDeSeguimiento(seguidor, seguido2)));

    List<Usuario> seguidos = repoConsulta.seguidosPor(seguidor);
    assertEquals(2, seguidos.size());
    assertTrue(seguidos.contains(new Usuario(seguido1)));
    assertTrue(seguidos.contains(new Usuario(seguido2)));
  }
}
