package com.challenge.tuiter.infraestructura.controlador.usuario;

import com.challenge.tuiter.aplicacion.timeline.ExploradorDeTimeline;
import com.challenge.tuiter.configuracion.MocksDeCasosDeUsoConfig;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.controlador.timeline.ControladorDeTimeline;
import com.challenge.tuiter.infraestructura.errores.ManejadorDeErrores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ControladorDeTimeline.class)
@Import({MocksDeCasosDeUsoConfig.class, ManejadorDeErrores.class})
class ControladorDeTimelineTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ExploradorDeTimeline explorador;

  @Test
  void devuelveTimelineDeUnUsuarioDado() throws Exception {
    Usuario juan = new Usuario("juan");
    Usuario tuitero1 = new Usuario("tuitero1");
    Usuario tuitero2 = new Usuario("tuitero2");
    var tuit1 = Tuit.desde(UUID.fromString("00000000-0000-0000-0000-000000000001"), tuitero1,
      "primer tuit", Instant.parse("2024-05-01T10:00:00Z"));
    var tuit2 = Tuit.desde(UUID.fromString("00000000-0000-0000-0000-000000000002"), tuitero2,
      "otro tuit", Instant.parse("2024-05-01T09:00:00Z"));

    when(explorador.explorarPara(juan)).thenReturn(List.of(tuit1, tuit2));

    mockMvc.perform(get("/usuarios/juan/timeline")).andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value("00000000-0000-0000-0000-000000000001"))
           .andExpect(jsonPath("$[0].contenido").value("primer tuit"))
           .andExpect(jsonPath("$[0].autorId").value(tuitero1.id()))
           .andExpect(jsonPath("$[0].fechaCreacion").value("2024-05-01T10:00:00Z"))
           .andExpect(jsonPath("$[1].id").value("00000000-0000-0000-0000-000000000002"))
           .andExpect(jsonPath("$[1].contenido").value("otro tuit"))
           .andExpect(jsonPath("$[1].autorId").value(tuitero2.id()))
           .andExpect(jsonPath("$[1].fechaCreacion").value("2024-05-01T09:00:00Z"));
  }
}
