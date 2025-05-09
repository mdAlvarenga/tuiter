package com.challenge.tuiter.dominio.tuit;

import com.challenge.tuiter.dominio.tuit.excepcion.AutorInvalidoException;
import com.challenge.tuiter.dominio.tuit.excepcion.ContenidoInvalidoException;
import com.challenge.tuiter.dominio.usuario.Usuario;
import lombok.Value;

import java.util.UUID;

@Value
public class Tuit {
  public static final int MAX_CARACTERES = 280;
  UUID id;
  Usuario autor;
  String contenido;

  private Tuit(UUID id, Usuario autor, String contenido) {
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
  }

  public static Tuit nuevo(Usuario autor, String contenido) {
    return new Tuit(UUID.randomUUID(), autor, contenido);
  }

  public String getAutorID() {
    return autor.id();
  }
}