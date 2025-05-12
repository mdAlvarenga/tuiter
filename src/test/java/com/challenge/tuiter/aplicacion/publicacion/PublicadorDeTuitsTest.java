package com.challenge.tuiter.aplicacion.publicacion;

import com.challenge.tuiter.aplicacion.evento.DespachadorDeEventos;
import com.challenge.tuiter.dominio.comun.evento.ManejadorDeEvento;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.tuit.evento.EventoDeTuitPublicado;
import com.challenge.tuiter.dominio.tuit.excepcion.ContenidoInvalidoException;
import com.challenge.tuiter.infraestructura.memoria.GuardadoTuitsEnMemoria;
import com.challenge.tuiter.infraestructura.memoria.RepositorioDeTimelineEnMemoria;
import com.challenge.tuiter.infraestructura.memoria.SeguimientosEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PublicadorDeTuitsTest {
  private GuardadoTuitsEnMemoria repositorio;
  private PublicadorDeTuits publicador;
  private ManejadorDeEvento<EventoDeTuitPublicado> manejadorMock;

  @BeforeEach
  void setUp() {
    Clock fixedClock = Clock.fixed(Instant.parse("2025-05-01T12:00:00Z"), ZoneOffset.UTC);

    repositorio = new GuardadoTuitsEnMemoria();
    var timelineRepo = new RepositorioDeTimelineEnMemoria();
    var seguimientoRepo = new SeguimientosEnMemoria();
    manejadorMock = mock(ManejadorDeEvento.class);
    when(manejadorMock.tipoDeEvento()).thenReturn(EventoDeTuitPublicado.class);
    var despchador = new DespachadorDeEventos(List.of(manejadorMock));
    publicador = new PublicadorDeTuits(repositorio, timelineRepo, seguimientoRepo, despchador,
      fixedClock);
  }

  @Test
  void publicaUnTuitYSeGeneraCorrectamente() {
    var peticion = new PeticionDePublicarTuit("autor", "Hola mundo");

    Tuit tuit = publicador.publicar(peticion);

    assertEquals("autor", tuit.getAutorID());
    assertEquals("Hola mundo", tuit.getContenido());
    assertTrue(repositorio.buscarPorId(tuit.getId().toString()).isPresent());
  }

  @Test
  void unUsuarioPublicaUnTuitYSeAgregaAlTimelineDelUsuarioCorrectamente() {
    var peticion = new PeticionDePublicarTuit("autor", "Hola mundo");

    Tuit tuit = publicador.publicar(peticion);

    assertTrue(repositorio.buscarPorId(tuit.getId().toString()).isPresent());
  }

  @Test
  void unUsuarioPublicaUnTuitYSeAgregaAlTimelineDeSusSeguidoresCorrectamente() {
    var peticion = new PeticionDePublicarTuit("autor", "Hola mundo");

    Tuit tuit = publicador.publicar(peticion);

    assertTrue(repositorio.buscarPorId(tuit.getId().toString()).isPresent());
  }

  @Test
  void lanzaExcepcionSiElContenidoEsInvalido() {
    var largo = "a".repeat(281);
    var peticion = new PeticionDePublicarTuit("autor", largo);

    assertThrows(ContenidoInvalidoException.class, () -> publicador.publicar(peticion));
  }

  @Test
  void lanzaExcepcionSiElContenidoEstaEnBlanco() {
    var peticion = new PeticionDePublicarTuit("autor", " ");

    assertThrows(ContenidoInvalidoException.class, () -> publicador.publicar(peticion));
  }

  @Test
  void lanzaExcepcionSiElContenidoEsEstaVacio() {
    var peticion = new PeticionDePublicarTuit("autor", null);

    assertThrows(ContenidoInvalidoException.class, () -> publicador.publicar(peticion));
  }

  @Test
  void alPublicarUnTuitSeDespachaElEventoDeTuitPublicado() {
    var peticion = new PeticionDePublicarTuit("ana", "contenido");
    when(manejadorMock.tipoDeEvento()).thenReturn(EventoDeTuitPublicado.class);

    publicador.publicar(peticion);

    verify(manejadorMock, times(1)).manejar(
      argThat(evento -> evento.autorId().equals("ana") && evento.contenido().equals("contenido")));
  }
}
