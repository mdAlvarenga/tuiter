package com.challenge.tuiter.infraestructura.controlador.seguimiento;

import com.challenge.tuiter.aplicacion.seguimiento.BuscadorDeSeguidores;
import com.challenge.tuiter.aplicacion.seguimiento.PeticionDeSeguimiento;
import com.challenge.tuiter.aplicacion.seguimiento.SeguidorDeUsuarios;
import com.challenge.tuiter.dominio.seguimiento.Seguimiento;
import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.controlador.seguimiento.dto.PeticionDeSeguirUsuarioDTO;
import com.challenge.tuiter.infraestructura.controlador.seguimiento.dto.RespuestaSeguimientoDTO;
import com.challenge.tuiter.infraestructura.controlador.seguimiento.dto.UsuarioSeguidoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seguimientos")
public class ControladorDeSeguimientos {
  private final SeguidorDeUsuarios seguidor;
  private final BuscadorDeSeguidores buscador;

  public ControladorDeSeguimientos(SeguidorDeUsuarios seguidor, BuscadorDeSeguidores buscador) {
    this.seguidor = seguidor;
    this.buscador = buscador;
  }

  @PostMapping
  public ResponseEntity<RespuestaSeguimientoDTO> seguir(
    @RequestBody PeticionDeSeguirUsuarioDTO peticion) {
    Seguimiento seguimiento = seguidor.seguirDesde(
      new PeticionDeSeguimiento(peticion.seguidorId(), peticion.seguidoId()));
    var respuesta = new RespuestaSeguimientoDTO(seguimiento.seguimientoId(),
      seguimiento.getSeguidorId(), seguimiento.getSeguidoId());
    return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
  }


  @GetMapping("/{usuarioId}/seguidos")
  public ResponseEntity<List<UsuarioSeguidoDTO>> verUsuariosSeguidos(
    @PathVariable String usuarioId) {
    var usuario = new Usuario(usuarioId);
    List<Usuario> seguidos = buscador.buscarSeguidosDe(usuario);
    List<UsuarioSeguidoDTO> respuesta = seguidos.stream().map(u -> new UsuarioSeguidoDTO(u.id()))
                                                .toList();
    return ResponseEntity.ok(respuesta);
  }
}
