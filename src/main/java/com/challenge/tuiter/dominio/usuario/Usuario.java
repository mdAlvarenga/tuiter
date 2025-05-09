package com.challenge.tuiter.dominio.usuario;

import com.challenge.tuiter.dominio.usuario.excepcion.UsuarioInvalidoException;

public record Usuario(String id) {
  public Usuario(String id) {
    if (id == null || id.isBlank())
      throw new UsuarioInvalidoException();
    this.id = id.trim().toLowerCase();
  }
}
