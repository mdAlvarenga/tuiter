package com.challenge.tuiter.infraestructura.memoria;

import com.challenge.tuiter.dominio.timeline.RepositorioDeConsultaDeTimeline;
import com.challenge.tuiter.dominio.timeline.RepositorioDeEscrituraDeTimeline;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepositorioDeTimelineEnMemoria implements RepositorioDeConsultaDeTimeline, RepositorioDeEscrituraDeTimeline {
  private final Map<Usuario, List<Tuit>> timelines = new HashMap<>();

  @Override
  public List<Tuit> timelineDe(Usuario usuario) {
    return timelines.getOrDefault(usuario, List.of());
  }

  public void publicarTuit(Usuario propietario, Tuit tuit) {
    timelines.computeIfAbsent(propietario, k -> new ArrayList<>()).add(tuit);
  }
}