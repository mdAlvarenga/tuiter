package com.challenge.tuiter.dominio.seguimiento;

import com.challenge.tuiter.dominio.seguimiento.excepcion.SeguimientoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SeguimientoTest {
  @Test
  void unUsuarioSigueAOtroyFormaUnaRelacionValida() {
    Seguimiento seguimiento = Seguimiento.nuevo("unUsuario", "otroUsuario");

    assertEquals("unUsuario", seguimiento.getSeguidorId());
    assertEquals("otroUsuario", seguimiento.getSeguidoId());
  }

  @Test
  void unUsuarioNoPuedeSeguirseASiMismo() {
    assertThrows(SeguimientoInvalidoException.class,
      () -> Seguimiento.nuevo("unUsuario", "unUsuario"));
  }
}
