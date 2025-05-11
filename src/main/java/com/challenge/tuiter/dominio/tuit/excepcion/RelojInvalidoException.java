package com.challenge.tuiter.dominio.tuit.excepcion;

import com.challenge.tuiter.dominio.excepcion.ExcepcionDeDominio;

public class RelojInvalidoException extends ExcepcionDeDominio {
  public RelojInvalidoException() {
    super("El reloj no puede ser nulo");
  }
}