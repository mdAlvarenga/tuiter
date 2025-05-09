package com.challenge.tuiter.dominio.seguimiento.excepcion;

public class SeguimientoInvalidoException extends RuntimeException {
  public SeguimientoInvalidoException(String mensaje) {
    super(mensaje);
  }
}
