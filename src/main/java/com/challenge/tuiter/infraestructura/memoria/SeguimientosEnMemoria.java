package com.challenge.tuiter.infraestructura.memoria;

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
  public boolean existe(Seguimiento seguimiento) {
    return relaciones.contains(seguimiento);
  }

  @Override
  public List<String> seguidoresDe(String seguidoId) {
    return relaciones.stream().filter(s -> s.seguidoId().equals(seguidoId))
                     .map(Seguimiento::seguidorId).toList();
  }
}
