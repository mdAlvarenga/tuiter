package com.challenge.tuiter.aplicacion.seguimiento;

import com.challenge.tuiter.dominio.seguimiento.Seguimiento;
import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.memoria.SeguimientosEnMemoriaDeConsulta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuscadorDeSeguidoresTest {
  private SeguimientosEnMemoriaDeConsulta repositorio;
  private BuscadorDeSeguidores buscador;

  @BeforeEach
  void setUp() {
    repositorio = new SeguimientosEnMemoriaDeConsulta();
    buscador = new BuscadorDeSeguidores(repositorio);
  }

  @Test
  void devuelveListaDeSeguidosParaUnUsuario() {
    repositorio.registrar(Seguimiento.nuevo("juan", "ana"));
    repositorio.registrar(Seguimiento.nuevo("juan", "pablo"));

    var seguidores = buscador.buscarSeguidoresDe("juan");

    assertEquals(Stream.of(new Usuario("ana"), new Usuario("pablo")).sorted().toList(),
      seguidores.stream().sorted().toList());
  }
}
