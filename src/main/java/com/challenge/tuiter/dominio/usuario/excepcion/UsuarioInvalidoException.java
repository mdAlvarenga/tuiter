package com.challenge.tuiter.dominio.usuario.excepcion;

public class UsuarioInvalidoException extends RuntimeException {
  public UsuarioInvalidoException() {
    super("El ID del usuario no es valido");
  }
}