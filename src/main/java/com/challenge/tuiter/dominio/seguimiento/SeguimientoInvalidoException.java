package com.challenge.tuiter.dominio.seguimiento;

public class SeguimientoInvalidoException extends RuntimeException {
  public SeguimientoInvalidoException(String mensaje) {
    super(mensaje);
  }
}
