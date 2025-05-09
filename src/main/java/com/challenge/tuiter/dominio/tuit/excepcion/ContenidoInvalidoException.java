package com.challenge.tuiter.dominio.tuit.excepcion;

public class ContenidoInvalidoException extends RuntimeException {
  public ContenidoInvalidoException(String mensaje) {
    super(mensaje);
  }
}
