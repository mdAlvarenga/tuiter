package com.challenge.tuiter.dominio.timeline;

import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;

public interface RepositorioDeEscrituraDeTimeline {
  void publicarTuit(Usuario propietario, Tuit tuit);
}