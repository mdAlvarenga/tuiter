package com.challenge.tuiter.dominio.usuario.excepcion;

import com.challenge.tuiter.dominio.excepcion.ExcepcionDeDominio;

public class UsuarioInvalidoException extends ExcepcionDeDominio {
  public UsuarioInvalidoException() {
    super("El ID del usuario no es valido");
  }
}