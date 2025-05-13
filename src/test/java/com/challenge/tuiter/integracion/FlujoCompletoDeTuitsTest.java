package com.challenge.tuiter.integracion;

import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.controlador.timeline.dto.TuitResponseDTO;
import com.challenge.tuiter.infraestructura.controlador.tuit.dto.PeticionDePublicacionDTO;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@SpringBootTest(classes = TestApplication.class,
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
class FlujoCompletoDeTuitsTest {
  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

  @LocalServerPort
  private int port;

  private WebTestClient webTestClient;

  @DynamicPropertySource
  static void propiedadesDinamicas(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @BeforeEach
  void setUp() {
    this.webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
  }

  @Test
  void publicaUnTuitYSePuedeLeerEnElTimeline() {
    var autor = new Usuario("autor");
    var contenido = "Hola, soy un tuit de integración!";

    publicarTuit(new PeticionDePublicacionDTO(autor.id(), contenido));
    var timeline = explorarTimeline(autor);

    assertThat(timeline).isNotEmpty();
    assertThat(timeline.get(0).contenido()).isEqualTo(contenido);
    assertThat(timeline.get(0).autorId()).isEqualTo(autor.id());
  }

  private @Nullable List<TuitResponseDTO> explorarTimeline(Usuario autor) {
    return webTestClient.get().uri("/usuarios/" + autor.id() + "/timeline").exchange()
                        .expectStatus().isOk().expectBodyList(TuitResponseDTO.class).returnResult()
                        .getResponseBody();
  }

  private void publicarTuit(PeticionDePublicacionDTO request) {
    webTestClient.post().uri("/tuits").bodyValue(request).exchange().expectStatus().isCreated()
                 .expectBody().jsonPath("$.id").exists().returnResult().getResponseBodyContent();
  }
}
