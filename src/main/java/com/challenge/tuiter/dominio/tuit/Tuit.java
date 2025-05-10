package com.challenge.tuiter.dominio.tuit;

import com.challenge.tuiter.dominio.tuit.excepcion.AutorInvalidoException;
import com.challenge.tuiter.dominio.tuit.excepcion.ContenidoInvalidoException;
import com.challenge.tuiter.dominio.tuit.excepcion.RelojInvalidoException;
import com.challenge.tuiter.dominio.usuario.Usuario;
import lombok.Value;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Value
public class Tuit {
  public static final int MAX_CARACTERES = 280;
  UUID id;
  Usuario autor;
  String contenido;
  Instant instanteDeCreacion;

  private Tuit(UUID id, Usuario autor, String contenido, Instant instanteDeCreacion) {
    if (autor == null) {
      throw new AutorInvalidoException();
    }
    if (contenido == null || contenido.isBlank()) {
      throw new ContenidoInvalidoException("El contenido del tuit no puede estar vacío");
    }
    if (contenido.length() > MAX_CARACTERES) {
      throw new ContenidoInvalidoException(
        "El contenido del tuit no puede superar los " + MAX_CARACTERES + " caracteres.");
    }
    this.id = id;
    this.autor = autor;
    this.contenido = contenido;
    this.instanteDeCreacion = instanteDeCreacion;
  }

  public static Tuit nuevo(Usuario autor, String contenido, Clock clock) {
    if (clock == null) {
      throw new RelojInvalidoException();
    }
    return new Tuit(UUID.randomUUID(), autor, contenido, Instant.now(clock));
  }

  public static Tuit desde(UUID id, Usuario usuario, String contenido, Instant instanteDeCreacion) {
    return new Tuit(id, usuario, contenido, instanteDeCreacion);
  }

  public String getAutorID() {
    return autor.id();
  }

  public boolean perteneceA(Usuario usuario) {
    return autor.equals(usuario);
  }

  public int esPosteriorA(Tuit otroTuit) {
    return this.instanteDeCreacion.compareTo(otroTuit.instanteDeCreacion);
  }
}