package com.challenge.tuiter.aplicacion.seguimiento;

import com.challenge.tuiter.dominio.seguimiento.RepositorioDeConsultaDeSeguimientos;
import com.challenge.tuiter.dominio.usuario.Usuario;

import java.util.List;

public class BuscadorDeSeguidores {
  private final RepositorioDeConsultaDeSeguimientos repositorio;

  public BuscadorDeSeguidores(RepositorioDeConsultaDeSeguimientos repositorio) {
    this.repositorio = repositorio;
  }

  public List<Usuario> buscarSeguidosDe(Usuario usuario) {
    return repositorio.seguidosDe(usuario).stream().sorted().toList();
  }

  public List<Usuario> buscarSeguidoresDe(Usuario usuario) {
    return repositorio.seguidoresDe(usuario).stream().sorted().toList();
  }
}
