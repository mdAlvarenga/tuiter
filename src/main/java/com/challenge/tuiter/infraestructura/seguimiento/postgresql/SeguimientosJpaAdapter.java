package com.challenge.tuiter.infraestructura.seguimiento.postgresql;

import com.challenge.tuiter.dominio.seguimiento.RelacionDeSeguimiento;
import com.challenge.tuiter.dominio.seguimiento.RepositorioDeConsultaDeSeguimientos;
import com.challenge.tuiter.dominio.seguimiento.RepositorioDeRegistroDeSeguimientos;
import com.challenge.tuiter.dominio.seguimiento.Seguimiento;
import com.challenge.tuiter.dominio.usuario.Usuario;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SeguimientosJpaAdapter implements RepositorioDeConsultaDeSeguimientos, RepositorioDeRegistroDeSeguimientos {
  private final SeguimientoJpaRepository jpa;

  public SeguimientosJpaAdapter(SeguimientoJpaRepository jpa) {
    this.jpa = jpa;
  }

  @Override
  public void registrar(Seguimiento seguimiento) {
    jpa.save(new SeguimientoEntidad(seguimiento.getId(), seguimiento.getSeguidorId(),
      seguimiento.getSeguidoId()));
  }

  @Override
  public boolean existe(RelacionDeSeguimiento relacion) {
    return jpa.existsBySeguidorIdAndSeguidoId(relacion.seguidorId(), relacion.seguidoId());
  }

  @Override
  public List<Usuario> seguidoresDe(Usuario usuario) {
    return jpa.findBySeguidoId(usuario.id()).stream().map(e -> new Usuario(e.getSeguidorId()))
              .toList();
  }

  @Override
  public List<Usuario> seguidosDe(Usuario usuario) {
    return jpa.findBySeguidorId(usuario.id()).stream().map(e -> new Usuario(e.getSeguidoId()))
              .toList();
  }
}
