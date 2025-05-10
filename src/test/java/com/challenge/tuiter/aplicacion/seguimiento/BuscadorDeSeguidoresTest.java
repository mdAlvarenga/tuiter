package com.challenge.tuiter.aplicacion.seguimiento;

import com.challenge.tuiter.dominio.seguimiento.Seguimiento;
import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.memoria.SeguimientosEnMemoriaDeConsulta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuscadorDeSeguidoresTest {
  private SeguimientosEnMemoriaDeConsulta repositorio;
  private BuscadorDeSeguidores buscador;

  @BeforeEach
  void setUp() {
    repositorio = new SeguimientosEnMemoriaDeConsulta();
    buscador = new BuscadorDeSeguidores(repositorio);
  }

  @Test
  void devuelveListaOrdenadaAlfabeticamenteDeSeguidosParaUnUsuario() {
    repositorio.registrar(Seguimiento.nuevo("juan", "ana"));
    repositorio.registrar(Seguimiento.nuevo("juan", "pablo"));

    var seguidores = buscador.buscarSeguidosDe("juan");

    assertEquals(Stream.of(new Usuario("ana"), new Usuario("pablo")).sorted().toList(),
      seguidores.stream().sorted().toList());
  }

  @Test
  void devuelveListaVaciaSiElUsuarioNoSigueANadie() {
    List<Usuario> seguidores = buscador.buscarSeguidosDe("juan");

    assertTrue(seguidores.isEmpty());
  }
}
