package com.challenge.tuiter.infraestructura.memoria;

import com.challenge.tuiter.dominio.seguimiento.RelacionDeSeguimiento;
import com.challenge.tuiter.dominio.seguimiento.RepositorioDeConsultaDeSeguimientos;
import com.challenge.tuiter.dominio.seguimiento.RepositorioDeRegistroDeSeguimientos;
import com.challenge.tuiter.dominio.seguimiento.Seguimiento;
import com.challenge.tuiter.dominio.usuario.Usuario;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeguimientosEnMemoriaDeConsulta implements RepositorioDeConsultaDeSeguimientos, RepositorioDeRegistroDeSeguimientos {
  private final Set<Seguimiento> relaciones = new HashSet<>();

  @Override
  public void registrar(Seguimiento seguimiento) {
    relaciones.add(seguimiento);
  }

  @Override
  public boolean existe(RelacionDeSeguimiento relacion) {
    return relaciones.stream().anyMatch(s -> s.getSeguidorId().equals(relacion.seguidorId()) &&
      s.getSeguidoId().equals(relacion.seguidoId()));
  }

  @Override
  public List<Usuario> seguidosPor(String seguidorId) {
    return relaciones.stream().filter(s -> s.esSeguidor(seguidorId))
                     .map(s -> new Usuario(s.getSeguidoId())).toList();
  }
}
