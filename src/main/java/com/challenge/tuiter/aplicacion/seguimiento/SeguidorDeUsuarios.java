package com.challenge.tuiter.aplicacion.seguimiento;

import com.challenge.tuiter.dominio.seguimiento.RelacionDeSeguimiento;
import com.challenge.tuiter.dominio.seguimiento.RepositorioDeConsultaDeSeguimientos;
import com.challenge.tuiter.dominio.seguimiento.RepositorioDeRegistroDeSeguimientos;
import com.challenge.tuiter.dominio.seguimiento.Seguimiento;
import com.challenge.tuiter.dominio.seguimiento.excepcion.SeguimientoInvalidoException;

public class SeguidorDeUsuarios {
  private final RepositorioDeRegistroDeSeguimientos repoRegistro;
  private final RepositorioDeConsultaDeSeguimientos repoConsultas;

  public SeguidorDeUsuarios(RepositorioDeRegistroDeSeguimientos repoRegistro, RepositorioDeConsultaDeSeguimientos repoConsultas) {
    this.repoRegistro = repoRegistro;
    this.repoConsultas = repoConsultas;
  }

  public Seguimiento seguirDesde(PeticionDeSeguimiento peticion) {
    if (peticion.seguidorId().equals(peticion.seguidoId())) {
      throw new SeguimientoInvalidoException("No podés seguirte a vos mismo");
    }

    RelacionDeSeguimiento relacion = new RelacionDeSeguimiento(peticion.seguidorId(),
      peticion.seguidoId());

    if (repoConsultas.existe(relacion)) {
      throw new SeguimientoInvalidoException("Ya lo estás siguiendo");
    }

    Seguimiento seguimiento = Seguimiento.nuevo(peticion.seguidorId(), peticion.seguidoId());
    repoRegistro.registrar(seguimiento);
    return seguimiento;
  }
}
