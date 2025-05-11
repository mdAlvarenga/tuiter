package com.challenge.tuiter.dominio.tuit.excepcion;

import com.challenge.tuiter.dominio.excepcion.ExcepcionDeDominio;

public class AutorInvalidoException extends ExcepcionDeDominio {
  public AutorInvalidoException() {
    super("El autor del tuit no puede estar vacío");
  }
}