package com.challenge.tuiter.integracion;

import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.controlador.seguimiento.dto.PeticionDeSeguirUsuarioDTO;
import com.challenge.tuiter.infraestructura.controlador.timeline.dto.TuitResponseDTO;
import com.challenge.tuiter.infraestructura.controlador.tuit.dto.PeticionDePublicacionDTO;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FanOutDeTuitTest extends BaseIntegrationTest {
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

  @Test
  void seguidorVeTuitDelSeguidoEnSuTimeline() {
    var seguido = new Usuario("seguido");
    var seguidor = new Usuario("seguidor");

    seguir(seguidor, seguido);
    var contenido = "Hola, soy el seguido";

    publicarTuit(new PeticionDePublicacionDTO(seguido.id(), contenido));

    Awaitility.await().atMost(Duration.ofSeconds(5)).pollInterval(Duration.ofMillis(300))
              .untilAsserted(() -> {
                var timeline = explorarTimeline(seguidor);
                assertThat(timeline).anyMatch(
                  t -> t.contenido().equals(contenido) && t.autorId().equals(seguido.id()));
              });
  }

  private void seguir(Usuario seguidor, Usuario seguido) {
    webTestClient.post().uri("/seguimientos")
                 .bodyValue(new PeticionDeSeguirUsuarioDTO(seguidor.id(), seguido.id())).exchange()
                 .expectStatus().isCreated();
  }

  private void publicarTuit(PeticionDePublicacionDTO dto) {
    webTestClient.post().uri("/tuits").bodyValue(dto).exchange().expectStatus().isCreated();
  }

  private List<TuitResponseDTO> explorarTimeline(Usuario usuario) {
    return webTestClient.get().uri("/usuarios/" + usuario.id() + "/timeline").exchange()
                        .expectStatus().isOk().expectBodyList(TuitResponseDTO.class).returnResult()
                        .getResponseBody();
  }
}
