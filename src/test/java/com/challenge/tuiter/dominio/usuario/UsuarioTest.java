package com.challenge.tuiter.dominio.usuario;

import com.challenge.tuiter.dominio.usuario.excepcion.UsuarioInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UsuarioTest {
  @Test
  void creaUnUsuarioNormalizado() {
    Usuario usuario = new Usuario("jUaN");

    assertEquals("juan", usuario.id());
  }

  @Test
  void noPermiteUsuarioConIdVacio() {
    assertThrows(UsuarioInvalidoException.class, () -> new Usuario(""));
  }

  @Test
  void noPermiteUsuarioSinId() {
    assertThrows(UsuarioInvalidoException.class, () -> new Usuario(null));
  }
}
