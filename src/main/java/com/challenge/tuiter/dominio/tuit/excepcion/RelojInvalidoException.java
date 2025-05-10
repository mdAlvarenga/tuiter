package com.challenge.tuiter.dominio.tuit.excepcion;

public class RelojInvalidoException extends RuntimeException {
  public RelojInvalidoException() {
    super("El reloj no puede ser nulo");
  }
}