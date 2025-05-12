package com.challenge.tuiter.infraestructura.controlador.timeline;

import com.challenge.tuiter.aplicacion.timeline.ExploradorDeTimeline;
import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.controlador.timeline.dto.TuitDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class ControladorDeTimeline {
  private final ExploradorDeTimeline explorador;

  public ControladorDeTimeline(ExploradorDeTimeline explorador) {
    this.explorador = explorador;
  }

  @GetMapping("/{id}/timeline")
  public List<TuitDTO> obtenerTimeline(@PathVariable String id) {
    return explorador.explorarPara(new Usuario(id)).stream().map(
      t -> new TuitDTO(t.getId().toString(), t.getContenido(), t.getAutorID(),
        t.getInstanteDeCreacion())).toList();
  }
}
