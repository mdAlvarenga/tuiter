package com.challenge.tuiter.aplicacion.timeline;

import com.challenge.tuiter.dominio.seguimiento.RepositorioDeConsultaDeSeguimientos;
import com.challenge.tuiter.dominio.timeline.RepositorioDeConsultaDeTimeline;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;

import java.util.List;

public class ExploradorDeTimeline {
  private final RepositorioDeConsultaDeTimeline repoTimeline;
  private final RepositorioDeConsultaDeSeguimientos repoSeguimientos;

  public ExploradorDeTimeline(RepositorioDeConsultaDeTimeline repoTimeline, RepositorioDeConsultaDeSeguimientos repoSeguimientos) {
    this.repoTimeline = repoTimeline;
    this.repoSeguimientos = repoSeguimientos;
  }

  public List<Tuit> explorarPara(Usuario usuario) {
    List<Usuario> seguidos = repoSeguimientos.seguidosPor(usuario.id());
    return repoTimeline.timelineDe(seguidos).stream().sorted((t1, t2) -> t2.esPosteriorA(t1))
                       .toList();
  }
}
