package com.challenge.tuiter.aplicacion.seguimiento;

import com.challenge.tuiter.dominio.seguimiento.RepositorioDeRegistroDeSeguimientos;
import com.challenge.tuiter.dominio.seguimiento.Seguimiento;

public class SeguidorDeUsuarios {
  private final RepositorioDeRegistroDeSeguimientos repositorio;

  public SeguidorDeUsuarios(RepositorioDeRegistroDeSeguimientos repositorio) {
    this.repositorio = repositorio;
  }

  public Seguimiento seguirDesde(PeticionDeSeguimiento peticion) {
    Seguimiento seguimiento = Seguimiento.nuevo(peticion.seguidorId(), peticion.seguidoId());
    this.repositorio.registrar(seguimiento);
    return seguimiento;
  }
}
