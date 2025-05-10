package com.challenge.tuiter.dominio.seguimiento;

import com.challenge.tuiter.dominio.seguimiento.excepcion.SeguimientoInvalidoException;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SeguimientoTest {
  private Clock fixedClock;

  @BeforeEach
  void setUp() {
    fixedClock = Clock.fixed(Instant.parse("2025-05-01T12:00:00Z"), ZoneOffset.UTC);
  }

  @Test
  void unUsuarioSigueAOtroyFormaUnaRelacionValida() {
    Seguimiento seguimiento = Seguimiento.nuevo("unUsuario", "otroUsuario");

    assertEquals("unUsuario", seguimiento.getSeguidorId());
    assertEquals("otroUsuario", seguimiento.getSeguidoId());
  }

  @Test
  void creaDosTuitsValidosConIdDiferente() {
    Tuit unTuit = Tuit.nuevo(new Usuario("autor"), "Hola mundo", fixedClock);
    Tuit otroTuit = Tuit.nuevo(new Usuario("autor"), "Hola mundo", fixedClock);

    assertNotEquals(unTuit.getId(), otroTuit.getId());
  }

  @Test
  void unUsuarioNoPuedeSeguirseASiMismo() {
    assertThrows(SeguimientoInvalidoException.class,
      () -> Seguimiento.nuevo("unUsuario", "unUsuario"));
  }
}
