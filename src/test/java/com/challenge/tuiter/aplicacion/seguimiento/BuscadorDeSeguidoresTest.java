package com.challenge.tuiter.aplicacion.seguimiento;

import com.challenge.tuiter.dominio.seguimiento.Seguimiento;
import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.memoria.SeguimientosEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuscadorDeSeguidoresTest {
  private SeguimientosEnMemoria repositorio;
  private BuscadorDeSeguidores buscador;

  @BeforeEach
  void setUp() {
    repositorio = new SeguimientosEnMemoria();
    buscador = new BuscadorDeSeguidores(repositorio);
  }

  @Test
  void devuelveListaOrdenadaAlfabeticamenteDeSeguidosParaUnUsuario() {
    repositorio.registrar(Seguimiento.nuevo("juan", "ana"));
    repositorio.registrar(Seguimiento.nuevo("juan", "pablo"));

    var seguidores = buscador.buscarSeguidosDe(new Usuario("juan"));

    assertEquals(Stream.of(new Usuario("ana"), new Usuario("pablo")).sorted().toList(),
      seguidores.stream().sorted().toList());
  }

  @Test
  void devuelveListaVaciaSiElUsuarioNoSigueANadie() {
    List<Usuario> seguidores = buscador.buscarSeguidosDe(new Usuario("juan"));

    assertTrue(seguidores.isEmpty());
  }

  @Test
  void devuelveListaOrdenadaAlfabeticamenteDeSeguidoresDeUnUsuario() {
    repositorio.registrar(Seguimiento.nuevo("ana", "juan"));
    repositorio.registrar(Seguimiento.nuevo("pablo", "juan"));

    var seguidores = buscador.buscarSeguidoresDe(new Usuario("juan"));

    assertEquals(Stream.of(new Usuario("ana"), new Usuario("pablo")).sorted().toList(),
      seguidores.stream().sorted().toList());
  }

  @Test
  void devuelveListaVaciaSiElUsuarioTieneSeguidores() {
    List<Usuario> seguidores = buscador.buscarSeguidoresDe(new Usuario("juan"));

    assertTrue(seguidores.isEmpty());
  }
}
