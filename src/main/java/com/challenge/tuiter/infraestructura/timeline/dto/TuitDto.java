package com.challenge.tuiter.infraestructura.timeline.dto;

import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class TuitDto {
  public UUID id;
  public Usuario autor;
  public String contenido;
  public Instant instanteDeCreacion;

  public static TuitDto desdeTuit(Tuit tuit) {
    return new TuitDto(tuit.getId(), tuit.getAutor(), tuit.getContenido(),
      tuit.getInstanteDeCreacion());
  }

  public Tuit aTuit() {
    return Tuit.desde(id, autor, contenido, instanteDeCreacion);
  }
}
