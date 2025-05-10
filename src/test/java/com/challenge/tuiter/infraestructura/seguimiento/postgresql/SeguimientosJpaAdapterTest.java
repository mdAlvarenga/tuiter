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
  public static final String SEGUIDOR = "juan";
  public static final String SEGUIDO_1 = "ana";
  public static final String SEGUIDO_2 = "pablo";
  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
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
    Seguimiento seguimiento1 = Seguimiento.nuevo(SEGUIDOR, SEGUIDO_1);
    Seguimiento seguimiento2 = Seguimiento.nuevo(SEGUIDOR, SEGUIDO_2);

    repoGuardar.registrar(seguimiento1);
    repoGuardar.registrar(seguimiento2);

    assertTrue(repoConsulta.existe(new RelacionDeSeguimiento(SEGUIDOR, SEGUIDO_1)));
    assertTrue(repoConsulta.existe(new RelacionDeSeguimiento(SEGUIDOR, SEGUIDO_2)));

    List<Usuario> seguidos = repoConsulta.seguidosPor(SEGUIDOR);
    assertEquals(2, seguidos.size());
    assertTrue(seguidos.contains(new Usuario(SEGUIDO_1)));
    assertTrue(seguidos.contains(new Usuario(SEGUIDO_2)));
  }
}
