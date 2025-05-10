package com.challenge.tuiter.aplicacion.seguimiento;

import com.challenge.tuiter.dominio.seguimiento.RelacionDeSeguimiento;
import com.challenge.tuiter.dominio.seguimiento.excepcion.SeguimientoInvalidoException;
import com.challenge.tuiter.infraestructura.memoria.SeguimientosEnMemoriaDeConsulta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeguidorDeUsuariosTest {
  private SeguimientosEnMemoriaDeConsulta repositorio;
  private SeguidorDeUsuarios seguidor;

  @BeforeEach
  void setUp() {
    repositorio = new SeguimientosEnMemoriaDeConsulta();
    seguidor = new SeguidorDeUsuarios(repositorio, repositorio);
  }

  @Test
  void siUnUsuarioSigueAOtroCorrectamenteExisteLaRelacionDeSeguimiento() {
    var peticion = new PeticionDeSeguimiento("user123", "user456");

    seguidor.seguirDesde(peticion);

    assertTrue(repositorio.existe(new RelacionDeSeguimiento("user123", "user456")));
  }

  @Test
  void lanzaExcepcionSiUnUsuarioIntentaSeguirseASiMismo() {
    var peticion = new PeticionDeSeguimiento("unUsuario", "unUsuario");

    assertThrows(SeguimientoInvalidoException.class, () -> seguidor.seguirDesde(peticion));
  }

  @Test
  void lanzaExcepcionSiUnUsuarioIntentaSeguirAOtrOUsuarioYaSeguido() {
    var peticion = new PeticionDeSeguimiento("unUsuario", "otroUsuario");
    seguidor.seguirDesde(peticion);

    assertThrows(SeguimientoInvalidoException.class, () -> seguidor.seguirDesde(peticion));
  }
}
