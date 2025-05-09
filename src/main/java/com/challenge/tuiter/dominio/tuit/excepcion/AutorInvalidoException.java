package com.challenge.tuiter.dominio.tuit.excepcion;

public class AutorInvalidoException extends RuntimeException {
  public AutorInvalidoException(String mensaje) {
    super(mensaje);
  }

  public AutorInvalidoException() {
    super("El autor del tuit no puede estar vacío");
  }
}