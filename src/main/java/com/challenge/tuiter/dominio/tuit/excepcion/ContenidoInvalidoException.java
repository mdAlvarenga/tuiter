package com.challenge.tuiter.dominio.tuit.excepcion;

import com.challenge.tuiter.dominio.excepcion.ExcepcionDeDominio;

public class ContenidoInvalidoException extends ExcepcionDeDominio {
  public ContenidoInvalidoException(String mensaje) {
    super(mensaje);
  }
}
