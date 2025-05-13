package com.challenge.tuiter.infraestructura.controlador.tuit;

import com.challenge.tuiter.aplicacion.publicacion.PeticionDePublicarTuit;
import com.challenge.tuiter.aplicacion.publicacion.PublicadorDeTuits;
import com.challenge.tuiter.configuracion.MocksDeCasosDeUsoConfig;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ControladorDePublicacionDeTuits.class)
@Import(MocksDeCasosDeUsoConfig.class)
class ControladorDePublicacionDeTuitsTest {
  private final Clock reloj = Clock.fixed(Instant.parse("2025-05-10T12:00:00Z"), ZoneOffset.UTC);
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private PublicadorDeTuits publicador;

  @Test
  void publicaUnTuitYDevuelve201ConId() throws Exception {
    var autorId = "juan";
    var contenido = "Hola, mundo";
    var peticionJson = """
      {
        "autorId": "juan",
        "contenido": "Hola, mundo"
      }
      """;

    var tuit = Tuit.nuevo(new Usuario(autorId), contenido, reloj);
    var idGenerado = tuit.getId();

    when(publicador.publicar(new PeticionDePublicarTuit(autorId, contenido))).thenReturn(tuit);

    mockMvc.perform(post("/tuits").contentType("application/json").content(peticionJson))
           .andExpect(status().isCreated()).andExpect(content().contentType("application/json"))
           .andExpect(jsonPath("$.id").value(idGenerado.toString()));
  }
}
