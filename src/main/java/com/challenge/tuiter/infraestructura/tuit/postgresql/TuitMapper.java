package com.challenge.tuiter.infraestructura.tuit.postgresql;

import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;

public class TuitMapper {

  public static TuitEntity aEntidad(Tuit tuit) {
    return new TuitEntity(tuit.getId(), tuit.getAutor().id(), tuit.getContenido(),
      tuit.getInstanteDeCreacion());
  }

  public static Tuit aDominio(TuitEntity entidad) {
    return Tuit.desde(entidad.getId(), new Usuario(entidad.getAutorId()), entidad.getContenido(),
      entidad.getInstanteDeCreacion());
  }
}
