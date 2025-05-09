package com.challenge.tuiter.aplicacion.seguimiento;

import com.challenge.tuiter.dominio.seguimiento.Seguimiento;
import com.challenge.tuiter.infraestructura.memoria.SeguimientosEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuscadorDeSeguidoresTest {
  private SeguimientosEnMemoria repositorio;
  private BuscadorDeSeguidores buscador;

  @BeforeEach
  void setUp() {
    repositorio = new SeguimientosEnMemoria();
    buscador = new BuscadorDeSeguidores(repositorio);
  }

  @Test
  void devuelveListaDeSeguidoresParaUnUsuario() {
    repositorio.guardar(Seguimiento.nuevo("ana", "juan"));
    repositorio.guardar(Seguimiento.nuevo("pablo", "juan"));

    var seguidores = buscador.buscarSeguidoresDe("juan");

    assertEquals(Stream.of("ana", "pablo").sorted().toList(),
      seguidores.stream().sorted().toList());
  }
}
