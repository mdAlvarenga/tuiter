package com.challenge.tuiter.aplicacion.publicacion;

import com.challenge.tuiter.aplicacion.evento.PublicadorDeEventos;
import com.challenge.tuiter.dominio.timeline.RepositorioDeEscrituraDeTimeline;
import com.challenge.tuiter.dominio.tuit.RepositorioDeGuardadoTuits;
import com.challenge.tuiter.dominio.tuit.evento.EventoDeTuitPublicado;
import com.challenge.tuiter.dominio.tuit.excepcion.ContenidoInvalidoException;
import com.challenge.tuiter.dominio.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PublicadorDeTuitsTest {
  private RepositorioDeEscrituraDeTimeline repoTimeline;
  private PublicadorDeEventos publicador;
  private PublicadorDeTuits publicadorDeTuits;

  @BeforeEach
  void setUp() {
    Clock fixedClock = Clock.fixed(Instant.parse("2025-05-01T12:00:00Z"), ZoneOffset.UTC);
    RepositorioDeGuardadoTuits repositorio = mock(RepositorioDeGuardadoTuits.class);
    repoTimeline = mock(RepositorioDeEscrituraDeTimeline.class);
    publicador = mock(PublicadorDeEventos.class);

    publicadorDeTuits = new PublicadorDeTuits(repositorio, repoTimeline, publicador, fixedClock);
  }

  @Test
  void publicaUnTuitYSeGeneraCorrectamente() {
    var peticion = new PeticionDePublicarTuit("autor", "Hola mundo");

    var tuit = publicadorDeTuits.publicar(peticion);

    assertEquals("autor", tuit.getAutorID());
    assertEquals("Hola mundo", tuit.getContenido());
  }

  @Test
  void unUsuarioPublicaUnTuitYSeAgregaAlTimelineDelUsuarioCorrectamente() {
    var peticion = new PeticionDePublicarTuit("autor", "Hola mundo");

    var tuit = publicadorDeTuits.publicar(peticion);

    verify(repoTimeline).publicarTuit(new Usuario("autor"), tuit);
  }

  @Test
  void lanzaExcepcionSiElContenidoEsInvalido() {
    var largo = "a".repeat(281);
    var peticion = new PeticionDePublicarTuit("autor", largo);

    assertThrows(ContenidoInvalidoException.class, () -> publicadorDeTuits.publicar(peticion));
  }

  @Test
  void lanzaExcepcionSiElContenidoEstaEnBlanco() {
    var peticion = new PeticionDePublicarTuit("autor", " ");
    assertThrows(ContenidoInvalidoException.class, () -> publicadorDeTuits.publicar(peticion));
  }

  @Test
  void lanzaExcepcionSiElContenidoEsEstaVacio() {
    var peticion = new PeticionDePublicarTuit("autor", null);
    assertThrows(ContenidoInvalidoException.class, () -> publicadorDeTuits.publicar(peticion));
  }

  @Test
  void alPublicarUnTuitSePublicaElEventoDeTuitPublicado() {
    var peticion = new PeticionDePublicarTuit("ana", "contenido");

    publicadorDeTuits.publicar(peticion);

    ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
    verify(publicador).publicar(captor.capture());

    Object eventoPublicado = captor.getValue();
    assertInstanceOf(EventoDeTuitPublicado.class, eventoPublicado);

    EventoDeTuitPublicado evento = (EventoDeTuitPublicado) eventoPublicado;
    assertEquals("ana", evento.autorId());
    assertEquals("contenido", evento.contenido());
  }
}
