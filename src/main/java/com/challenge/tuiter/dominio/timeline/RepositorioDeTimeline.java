package com.challenge.tuiter.dominio.timeline;

import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;

import java.util.List;

public interface RepositorioDeTimeline {
  List<Tuit> timelineDe(Usuario usuario);
}
