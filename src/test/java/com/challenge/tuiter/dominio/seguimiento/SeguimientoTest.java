package com.challenge.tuiter.dominio.seguimiento;

import com.challenge.tuiter.dominio.seguimiento.excepcion.SeguimientoInvalidoException;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SeguimientoTest {
  @Test
  void unUsuarioSigueAOtroyFormaUnaRelacionValida() {
    Seguimiento seguimiento = Seguimiento.nuevo("unUsuario", "otroUsuario");

    assertEquals("unUsuario", seguimiento.getSeguidorId());
    assertEquals("otroUsuario", seguimiento.getSeguidoId());
  }

  @Test
  void creaDosTuitsValidosConIdDiferente() {
    Tuit unTuit = Tuit.nuevo(new Usuario("autor"), "Hola mundo");
    Tuit otroTuit = Tuit.nuevo(new Usuario("autor"), "Hola mundo");

    assertNotEquals(unTuit.getId(), otroTuit.getId());
  }

  @Test
  void unUsuarioNoPuedeSeguirseASiMismo() {
    assertThrows(SeguimientoInvalidoException.class,
      () -> Seguimiento.nuevo("unUsuario", "unUsuario"));
  }
}
