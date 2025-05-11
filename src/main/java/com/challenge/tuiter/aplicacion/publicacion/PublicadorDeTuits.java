package com.challenge.tuiter.aplicacion.publicacion;

import com.challenge.tuiter.dominio.seguimiento.RepositorioDeConsultaDeSeguimientos;
import com.challenge.tuiter.dominio.timeline.RepositorioDeEscrituraDeTimeline;
import com.challenge.tuiter.dominio.tuit.RepositorioDeGuardadoTuits;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;

import java.time.Clock;

public class PublicadorDeTuits {
  private final RepositorioDeGuardadoTuits repositorio;
  private final RepositorioDeEscrituraDeTimeline timelineRepo;
  private final RepositorioDeConsultaDeSeguimientos seguimientoRepo;
  private final Clock clock;

  public PublicadorDeTuits(RepositorioDeGuardadoTuits repositorio, RepositorioDeEscrituraDeTimeline timelineRepo, RepositorioDeConsultaDeSeguimientos seguimientoRepo, Clock clock) {
    this.repositorio = repositorio;
    this.timelineRepo = timelineRepo;
    this.seguimientoRepo = seguimientoRepo;
    this.clock = clock;
  }

  public Tuit publicar(PeticionDePublicarTuit peticion) {
    Usuario autor = new Usuario(peticion.autorId());
    Tuit tuit = Tuit.nuevo(autor, peticion.contenido(), clock);
    this.repositorio.guardar(tuit);
    agregarATimelineDe(autor, tuit);
    avisarASeguidoresDe(autor, tuit);
    return tuit;
  }

  private void agregarATimelineDe(Usuario autor, Tuit tuit) {
    timelineRepo.publicarTuit(autor, tuit);
  }

  private void avisarASeguidoresDe(Usuario autor, Tuit tuit) {
    var seguidores = seguimientoRepo.seguidoresDe(autor);
    for (Usuario seguidor : seguidores) {
      agregarATimelineDe(seguidor, tuit);
    }
  }
}
