package com.challenge.tuiter.infraestructura.timeline.postgresql;

import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;

public class TimelineMapper {
  public static TimelineTuitEntity aEntidad(Tuit tuit, Usuario propietario) {
    return new TimelineTuitEntity(tuit.getId(), tuit.getAutor().id(), propietario.id(),
      tuit.getContenido(), tuit.getInstanteDeCreacion());
  }

  public static Tuit aDominio(TimelineTuitEntity entity) {
    return Tuit.desde(entity.getId(), new Usuario(entity.getAutorId()), entity.getContenido(),
      entity.getInstanteDeCreacion());
  }
}
