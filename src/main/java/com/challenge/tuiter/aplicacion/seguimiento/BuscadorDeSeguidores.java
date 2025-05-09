package com.challenge.tuiter.aplicacion.seguimiento;

import com.challenge.tuiter.dominio.seguimiento.RepositorioDeSeguimientos;

import java.util.List;

public class BuscadorDeSeguidores {
  private final RepositorioDeSeguimientos repositorio;

  public BuscadorDeSeguidores(RepositorioDeSeguimientos repositorio) {
    this.repositorio = repositorio;
  }

  public List<String> buscarSeguidoresDe(String seguidoId) {
    return repositorio.seguidoresDe(seguidoId).stream().sorted().toList();
  }
}
