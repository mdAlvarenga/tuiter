package com.challenge.tuiter.dominio.timeline;

import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;

import java.util.List;

public interface RepositorioDeConsultaDeTimeline {
  List<Tuit> timelineDe(Usuario usuario);
}
