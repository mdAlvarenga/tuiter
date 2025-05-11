package com.challenge.tuiter.infraestructura.controlador.seguimiento;

import com.challenge.tuiter.aplicacion.seguimiento.BuscadorDeSeguidores;
import com.challenge.tuiter.aplicacion.seguimiento.PeticionDeSeguimiento;
import com.challenge.tuiter.aplicacion.seguimiento.SeguidorDeUsuarios;
import com.challenge.tuiter.configuracion.CasosDeUsoTestConfig;
import com.challenge.tuiter.dominio.seguimiento.Seguimiento;
import com.challenge.tuiter.dominio.seguimiento.excepcion.SeguimientoInvalidoException;
import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.errores.ManejadorDeErrores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ControladorDeSeguimientos.class)
@Import({CasosDeUsoTestConfig.class, ManejadorDeErrores.class})
class ControladorDeSeguimientosTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private SeguidorDeUsuarios seguidor;
  @Autowired
  private BuscadorDeSeguidores buscador;

  @Test
  void sigueUsuarioYDevuelve201() throws Exception {
    var seguimiento = Seguimiento.desde(UUID.randomUUID(), "juan", "carlos");
    var peticionJson = """
      {
        "seguidorId": "juan",
        "seguidoId": "carlos"
      }
      """;

    when(seguidor.seguirDesde(new PeticionDeSeguimiento("juan", "carlos"))).thenReturn(seguimiento);

    mockMvc.perform(post("/seguimientos").contentType("application/json").content(peticionJson))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.seguimientoId").value(seguimiento.seguimientoId()))
           .andExpect(jsonPath("$.seguidorId").value(seguimiento.getSeguidorId()))
           .andExpect(jsonPath("$.seguidoId").value(seguimiento.getSeguidoId()));
  }

  @Test
  void noPuedeSeguirseASiMismoLanzandoBadRequest() throws Exception {
    var peticionJson = """
      {
        "seguidorId": "mismoUsuario",
        "seguidoId": "mismoUsuario"
      }
      """;

    when(seguidor.seguirDesde(new PeticionDeSeguimiento("mismoUsuario", "mismoUsuario"))).thenThrow(
      new SeguimientoInvalidoException("No puede seguirse a sí mismo"));

    mockMvc.perform(post("/seguimientos").contentType("application/json").content(peticionJson))
           .andExpect(status().isBadRequest())
           .andExpect(content().string("No puede seguirse a sí mismo"));
  }

  @Test
  void devuelveUsuariosSeguidosPorUnUsuario() throws Exception {
    var usuario1 = new Usuario("carlos");
    var usuario2 = new Usuario("lucia");

    when(buscador.buscarSeguidosDe("juan")).thenReturn(List.of(usuario1, usuario2));

    mockMvc.perform(get("/seguimientos/juan/seguidos")).andExpect(status().isOk())
           .andExpect(jsonPath("$[0].usuarioId").value("carlos"))
           .andExpect(jsonPath("$[1].usuarioId").value("lucia"));
  }
}
