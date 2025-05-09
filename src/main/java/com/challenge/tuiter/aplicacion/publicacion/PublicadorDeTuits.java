package com.challenge.tuiter.aplicacion.publicacion;

import com.challenge.tuiter.dominio.tuit.RepositorioDeTuits;
import com.challenge.tuiter.dominio.tuit.Tuit;

public class PublicadorDeTuits {
  private final RepositorioDeTuits repositorio;

  public PublicadorDeTuits(RepositorioDeTuits repositorio) {
    this.repositorio = repositorio;
  }

  public Tuit publicar(PeticionDePublicarTuit peticion) {
    Tuit tuit = Tuit.nuevo(peticion.autor(), peticion.contenido());
    this.repositorio.guardar(tuit);
    return tuit;
  }
}
