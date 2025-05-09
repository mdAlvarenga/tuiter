package com.challenge.tuiter.dominio.seguimiento;

import com.challenge.tuiter.dominio.seguimiento.excepcion.SeguimientoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SeguimientoTest {
  @Test
  void unUsuarioSigueAOtroyFormaUnaRelacionValida() {
    Seguimiento seguimiento = new Seguimiento("unUsuario", "otroUsuario");

    assertEquals("unUsuario", seguimiento.seguidorId());
    assertEquals("otroUsuario", seguimiento.seguidoId());
  }

  @Test
  void unUsuarioNoPuedeSeguirseASiMismo() {
    assertThrows(SeguimientoInvalidoException.class, () -> new Seguimiento("unUsuario", "unUsuario"));
  }
}
