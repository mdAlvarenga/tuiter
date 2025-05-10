package com.challenge.tuiter.infraestructura.memoria;

import com.challenge.tuiter.dominio.timeline.RepositorioDeEscrituraDeTimeline;
import com.challenge.tuiter.dominio.timeline.RepositorioDeConsultaDeTimeline;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimelineEnMemoriaDeConsulta implements RepositorioDeConsultaDeTimeline, RepositorioDeEscrituraDeTimeline {
  private final Map<Usuario, List<Tuit>> timelines = new HashMap<>();

  @Override
  public List<Tuit> timelineDe(List<Usuario> autores) {
    return autores.stream().flatMap(autor -> timelines.getOrDefault(autor, List.of()).stream())
                  .toList();
  }

  public void agregarTuit(Tuit tuit) {
    Usuario autor = tuit.getAutor();
    timelines.computeIfAbsent(autor, k -> new ArrayList<>()).add(tuit);
  }
}