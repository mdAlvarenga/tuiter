package com.challenge.tuiter.aplicacion.publicacion;

import com.challenge.tuiter.aplicacion.evento.PublicadorDeEventos;
import com.challenge.tuiter.dominio.timeline.RepositorioDeEscrituraDeTimeline;
import com.challenge.tuiter.dominio.tuit.RepositorioDeGuardadoTuits;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;

import java.time.Clock;

public class PublicadorDeTuits {
  private final RepositorioDeGuardadoTuits repositorio;
  private final RepositorioDeEscrituraDeTimeline timelineRepo;
  private final PublicadorDeEventos publicador;
  private final Clock clock;

  public PublicadorDeTuits(RepositorioDeGuardadoTuits repositorio, RepositorioDeEscrituraDeTimeline timelineRepo, PublicadorDeEventos publicador, Clock clock) {
    this.repositorio = repositorio;
    this.timelineRepo = timelineRepo;
    this.publicador = publicador;
    this.clock = clock;
  }

  public Tuit publicar(PeticionDePublicarTuit peticion) {
    Usuario autor = new Usuario(peticion.autorId());
    Tuit tuit = Tuit.nuevo(autor, peticion.contenido(), clock);
    this.repositorio.guardar(tuit);
    timelineRepo.publicarTuit(autor, tuit);
    tuit.eventosDominio().forEach(publicador::publicar);
    tuit.limpiarEventos();
    return tuit;
  }
}
