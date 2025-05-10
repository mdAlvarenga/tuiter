package com.challenge.tuiter.aplicacion.publicacion;

import com.challenge.tuiter.dominio.tuit.RepositorioDeTuits;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;

import java.time.Clock;

public class PublicadorDeTuits {
  private final RepositorioDeTuits repositorio;
  private final Clock clock;

  public PublicadorDeTuits(RepositorioDeTuits repositorio, Clock clock) {
    this.repositorio = repositorio;
    this.clock = clock;
  }

  public Tuit publicar(PeticionDePublicarTuit peticion) {
    Tuit tuit = Tuit.nuevo(new Usuario(peticion.autor()), peticion.contenido(), clock);
    this.repositorio.guardar(tuit);
    return tuit;
  }
}
