package com.challenge.tuiter.aplicacion.seguimiento;

import com.challenge.tuiter.dominio.seguimiento.RepositorioDeConsultaDeSeguimientos;
import com.challenge.tuiter.dominio.usuario.Usuario;

import java.util.List;

public class BuscadorDeSeguidores {
  private final RepositorioDeConsultaDeSeguimientos repositorio;

  public BuscadorDeSeguidores(RepositorioDeConsultaDeSeguimientos repositorio) {
    this.repositorio = repositorio;
  }

  public List<Usuario> buscarSeguidosDe(String seguidorId) {
    return repositorio.seguidosPor(seguidorId).stream().sorted().toList();
  }
}
