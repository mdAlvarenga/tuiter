package com.challenge.tuiter.aplicacion.publicacion;

import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.tuit.excepcion.ContenidoInvalidoException;
import com.challenge.tuiter.infraestructura.memoria.GuardadoTuitsEnMemoria;
import com.challenge.tuiter.infraestructura.memoria.RepositorioDeTimelineEnMemoria;
import com.challenge.tuiter.infraestructura.memoria.SeguimientosEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicadorDeTuitsTest {
  private GuardadoTuitsEnMemoria repositorio;
  private PublicadorDeTuits publicador;

  @BeforeEach
  void setUp() {
    Clock fixedClock = Clock.fixed(Instant.parse("2025-05-01T12:00:00Z"), ZoneOffset.UTC);

    repositorio = new GuardadoTuitsEnMemoria();
    var timelineRepo = new RepositorioDeTimelineEnMemoria();
    var seguimientoRepo = new SeguimientosEnMemoria();
    publicador = new PublicadorDeTuits(repositorio, timelineRepo, seguimientoRepo, fixedClock);
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
}
