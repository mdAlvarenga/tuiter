package com.challenge.tuiter.dominio.tuit;

public class ContenidoInvalidoException extends RuntimeException {
  public ContenidoInvalidoException(String mensaje) {
    super(mensaje);
  }
}
