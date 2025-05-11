package com.challenge.tuiter.infraestructura.controlador.tuit;

import com.challenge.tuiter.aplicacion.publicacion.PublicadorDeTuits;
import com.challenge.tuiter.infraestructura.controlador.tuit.dto.PeticionDePublicacionDTO;
import com.challenge.tuiter.infraestructura.controlador.tuit.dto.RespuestaDePublicacionDTO;
import com.challenge.tuiter.infraestructura.controlador.tuit.mapper.PublicacionMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tuits")
public class ControladorDePublicacionDeTuits {
  private final PublicadorDeTuits publicador;

  public ControladorDePublicacionDeTuits(PublicadorDeTuits publicador) {
    this.publicador = publicador;
  }

  @PostMapping
  public ResponseEntity<RespuestaDePublicacionDTO> publicar(
    @RequestBody PeticionDePublicacionDTO dto) {
    var peticion = PublicacionMapper.aDominio(dto);
    var tuitPublicado = publicador.publicar(peticion);

    var respuesta = PublicacionMapper.aDto(tuitPublicado.getId());

    return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
  }
}
