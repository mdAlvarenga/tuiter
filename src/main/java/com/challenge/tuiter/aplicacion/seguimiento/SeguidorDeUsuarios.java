package com.challenge.tuiter.aplicacion.seguimiento;

import com.challenge.tuiter.dominio.seguimiento.RepositorioDeSeguimientos;
import com.challenge.tuiter.dominio.seguimiento.Seguimiento;

public class SeguidorDeUsuarios {
  private final RepositorioDeSeguimientos repositorio;

  public SeguidorDeUsuarios(RepositorioDeSeguimientos repositorio) {
    this.repositorio = repositorio;
  }

  public Seguimiento seguirDesde(PeticionDeSeguimiento peticion) {
    Seguimiento seguimiento = new Seguimiento(peticion.seguidorId(), peticion.seguidoId());
    this.repositorio.guardar(seguimiento);
    return seguimiento;
  }
}
