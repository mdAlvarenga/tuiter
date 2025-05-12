package com.challenge.tuiter.dominio.tuit;

import com.challenge.tuiter.dominio.comun.evento.EventoDeDominio;
import com.challenge.tuiter.dominio.tuit.evento.EventoDeTuitPublicado;
import com.challenge.tuiter.dominio.tuit.excepcion.AutorInvalidoException;
import com.challenge.tuiter.dominio.tuit.excepcion.ContenidoInvalidoException;
import com.challenge.tuiter.dominio.tuit.excepcion.RelojInvalidoException;
import com.challenge.tuiter.dominio.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TuitTest {
  private Clock fixedClock;

  @BeforeEach
  void setUp() {
    fixedClock = Clock.fixed(Instant.parse("2025-05-01T12:00:00Z"), ZoneOffset.UTC);
  }

  @Test
  void creaUnTuitConAutorYContenidoValidos() {
    Tuit tuit = Tuit.nuevo(new Usuario("autor"), "Hola mundo", fixedClock);
    assertNotNull(tuit.getId());
    assertEquals("autor", tuit.getAutorID());
    assertEquals("Hola mundo", tuit.getContenido());
  }

  @Test
  void creaDosTuitsValidosConIdDiferente() {
    Usuario autor = new Usuario("autor");
    Tuit unTuit = Tuit.nuevo(autor, "Hola mundo", fixedClock);
    Tuit otroTuit = Tuit.nuevo(autor, "Hola mundo", fixedClock);

    assertNotEquals(unTuit.getId(), otroTuit.getId());
  }

  @Test
  void noPermiteContenidoVacio() {
    assertThrows(ContenidoInvalidoException.class,
      () -> Tuit.nuevo(new Usuario("autor"), "", fixedClock));
  }

  @Test
  void noPermiteContenidoConMasDe280Caracteres() {
    String largo = "a".repeat(281);
    assertThrows(ContenidoInvalidoException.class,
      () -> Tuit.nuevo(new Usuario("autor"), largo, fixedClock));
  }

  @Test
  void noPermiteAutorVacio() {
    assertThrows(AutorInvalidoException.class, () -> Tuit.nuevo(null, "contenido", fixedClock));
  }

  @Test
  void noPermiteRelojVacio() {
    assertThrows(RelojInvalidoException.class,
      () -> Tuit.nuevo(new Usuario("autor"), "contenido", null));
  }

  @Test
  void creaUnTuitConInstanteDeCreacionEsperado() {
    Instant fixedInstant = Instant.parse("2025-01-01T10:00:00Z");
    Clock clock = Clock.fixed(fixedInstant, ZoneOffset.UTC);
    Usuario autor = new Usuario("autor");
    String contenido = "contenido";

    Tuit tuit = Tuit.nuevo(autor, contenido, clock);

    assertEquals(fixedInstant, tuit.getInstanteDeCreacion());
  }

  @Test
  void alCrearseUnTuitRegistraUnEvento() {
    var ahora = Instant.parse("2025-05-12T03:00:00Z");
    var clock = Clock.fixed(ahora, ZoneOffset.UTC);
    var autor = new Usuario("ana");


    var tuit = Tuit.nuevo(autor, "hola mundo", clock);


    List<EventoDeDominio> eventos = tuit.eventosDominio();
    assertEquals(1, eventos.size());
    assertThat(eventos.get(0)).isInstanceOf(EventoDeTuitPublicado.class);

    var evento = (EventoDeTuitPublicado) eventos.get(0);
    assertThat(evento.tuitId()).isEqualTo(tuit.getId().toString());
    assertThat(evento.autorId()).isEqualTo("ana");
    assertThat(evento.contenido()).isEqualTo("hola mundo");
    assertThat(evento.instante()).isEqualTo(ahora);
  }
}
