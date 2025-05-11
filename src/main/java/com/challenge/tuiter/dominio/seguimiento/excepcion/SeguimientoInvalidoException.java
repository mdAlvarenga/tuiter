package com.challenge.tuiter.dominio.seguimiento.excepcion;

import com.challenge.tuiter.dominio.excepcion.ExcepcionDeDominio;

public class SeguimientoInvalidoException extends ExcepcionDeDominio {
  public SeguimientoInvalidoException(String mensaje) {
    super(mensaje);
  }
}
