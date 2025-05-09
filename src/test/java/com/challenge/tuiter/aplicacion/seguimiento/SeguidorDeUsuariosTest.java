package com.challenge.tuiter.aplicacion.seguimiento;

import com.challenge.tuiter.dominio.seguimiento.Seguimiento;
import com.challenge.tuiter.dominio.seguimiento.excepcion.SeguimientoInvalidoException;
import com.challenge.tuiter.infraestructura.memoria.SeguimientosEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeguidorDeUsuariosTest {
  private SeguimientosEnMemoria repositorio;
  private SeguidorDeUsuarios seguidor;

  @BeforeEach
  void setUp() {
    repositorio = new SeguimientosEnMemoria();
    seguidor = new SeguidorDeUsuarios(repositorio);
  }

  @Test
  void sigueAUnUsuarioCorrectamente() {
    var peticion = new PeticionDeSeguimiento("user123", "user456");

    seguidor.seguirDesde(peticion);

    assertTrue(repositorio.existe(new Seguimiento("user123", "user456")));
  }

  @Test
  void lanzaExcepcionSiElSeguimientoEsInvalido() {
    var peticion = new PeticionDeSeguimiento("unUsuario", "unUsuario");

    assertThrows(SeguimientoInvalidoException.class, () -> seguidor.seguirDesde(peticion));
  }
}
