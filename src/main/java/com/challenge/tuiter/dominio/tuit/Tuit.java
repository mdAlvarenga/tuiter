package com.challenge.tuiter.dominio.tuit;

public record Tuit(String autor, String contenido) {
  public static final int MAX_CARACTERES = 280;

  public Tuit {
    if (contenido.isBlank()) {
      throw new ContenidoInvalidoException("El contenido del tuit no puede estar vacío");
    }
    if (contenido.length() > MAX_CARACTERES) {
      throw new ContenidoInvalidoException(
        "El contenido del tuit no puede superar los " + MAX_CARACTERES + " caracteres.");
    }
  }
}

