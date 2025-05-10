package com.challenge.tuiter.infraestructura.timeline.postgresql;

import com.challenge.tuiter.dominio.timeline.RepositorioDeConsultaDeTimeline;
import com.challenge.tuiter.dominio.timeline.RepositorioDeEscrituraDeTimeline;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TimelineJpaAdapter implements RepositorioDeConsultaDeTimeline, RepositorioDeEscrituraDeTimeline {
  private final TimelineJpaRepository jpa;

  public TimelineJpaAdapter(TimelineJpaRepository jpa) {
    this.jpa = jpa;
  }

  @Override
  public void agregarTuit(Tuit tuit) {
    TimelineTuitEntity entidad = TimelineMapper.aEntidad(tuit, tuit.getAutor());
    jpa.save(entidad);
  }

  @Override
  public List<Tuit> timelineDe(List<Usuario> autoresIds) {
    List<String> ids = autoresIds.stream().map(Usuario::id).toList();
    return jpa.findAllByPropietarioIdIn(ids).stream().map(TimelineMapper::aDominio).toList();

  }
}
