package com.challenge.tuiter.dominio.excepcion;

public abstract class ExcepcionDeDominio extends RuntimeException {
  public ExcepcionDeDominio(String mensaje) {
    super(mensaje);
  }
}
