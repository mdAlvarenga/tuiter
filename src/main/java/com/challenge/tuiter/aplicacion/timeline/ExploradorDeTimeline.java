package com.challenge.tuiter.aplicacion.timeline;

import com.challenge.tuiter.dominio.timeline.RepositorioDeTimeline;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;

import java.util.List;

public class ExploradorDeTimeline {
  private final RepositorioDeTimeline repositorio;

  public ExploradorDeTimeline(RepositorioDeTimeline repositorio) {
    this.repositorio = repositorio;
  }

  public List<Tuit> explorarPara(Usuario usuario) {
    return repositorio.timelineDe(usuario).stream().sorted((t1, t2) -> t2.esPosteriorA(t1))
                      .toList();
  }
}
