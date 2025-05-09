package com.challenge.tuiter.infraestructura.memoria;

import com.challenge.tuiter.dominio.seguimiento.RelacionDeSeguimiento;
import com.challenge.tuiter.dominio.seguimiento.RepositorioDeSeguimientos;
import com.challenge.tuiter.dominio.seguimiento.Seguimiento;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeguimientosEnMemoria implements RepositorioDeSeguimientos {
  private final Set<Seguimiento> relaciones = new HashSet<>();

  @Override
  public void guardar(Seguimiento seguimiento) {
    relaciones.add(seguimiento);
  }

  @Override
  public boolean existe(RelacionDeSeguimiento relacion) {
    return relaciones.stream().anyMatch(s -> s.getSeguidorId().equals(relacion.seguidorId()) &&
      s.getSeguidoId().equals(relacion.seguidoId()));
  }

  @Override
  public List<String> seguidoresDe(String seguidoId) {
    return relaciones.stream().filter(s -> s.getSeguidoId().equals(seguidoId))
                     .map(Seguimiento::getSeguidorId).toList();
  }
}
