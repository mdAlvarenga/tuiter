package com.challenge.tuiter.dominio.usuario;

import com.challenge.tuiter.dominio.usuario.excepcion.UsuarioInvalidoException;

public record Usuario(String id) implements Comparable<Usuario> {
  public Usuario {
    if (id == null || id.isBlank())
      throw new UsuarioInvalidoException();
    id = id.trim().toLowerCase();
  }

  @Override
  public int compareTo(Usuario otro) {
    return this.id.compareTo(otro.id);
  }
}
