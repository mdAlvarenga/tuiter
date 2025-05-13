package com.challenge.tuiter.aplicacion.timeline;

import com.challenge.tuiter.dominio.timeline.RepositorioDeConsultaDeTimeline;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;

import java.util.List;

public class ExploradorDeTimeline {
  private final RepositorioDeConsultaDeTimeline repoTimeline;

  public ExploradorDeTimeline(RepositorioDeConsultaDeTimeline repoTimeline) {
    this.repoTimeline = repoTimeline;
  }

  public List<Tuit> explorarPara(Usuario usuario) {
    return repoTimeline.timelineDe(usuario).stream().sorted((t1, t2) -> t2.esPosteriorA(t1))
                       .toList();
  }
}
