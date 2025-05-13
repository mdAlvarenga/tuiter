package com.challenge.tuiter.aplicacion.timeline;

import com.challenge.tuiter.dominio.seguimiento.Seguimiento;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.memoria.RepositorioDeTimelineEnMemoria;
import com.challenge.tuiter.infraestructura.memoria.SeguimientosEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExploradorDeTimelineTest {
  private ExploradorDeTimeline explorador;
  private RepositorioDeTimelineEnMemoria repoDeTimeLine;
  private SeguimientosEnMemoria repoDeSeguimientos;

  @BeforeEach
  void setUp() {
    repoDeTimeLine = new RepositorioDeTimelineEnMemoria();
    repoDeSeguimientos = new SeguimientosEnMemoria();
    explorador = new ExploradorDeTimeline(repoDeTimeLine);
  }

  @Test
  void devuelveTimelineVacioParaUnUsuarioQueNoSigueANadie() {
    var resultado = explorador.explorarPara(new Usuario("autor"));

    assertTrue(resultado.isEmpty());
  }

  @Test
  void devuelveTimelineVacioParaUnUsuarioSiLosSeguidosNoTienenTuits() {
    Usuario seguidor = new Usuario("seguidor");
    Usuario seguido = new Usuario("seguido");
    seguir(seguidor, seguido);

    var resultado = explorador.explorarPara(seguidor);

    assertTrue(resultado.isEmpty());
  }

  @Test
  void unUsuarioTieneSusTuitsComoParteDeSuTimelineOrdenadosDeManeraDescendente() {
    Usuario tuitero = new Usuario("tuitero");
    Tuit tuitAntiguo = tuitDe(tuitero, "tuit viejo", "2025-05-01T11:00:00Z");
    Tuit tuitReciente = tuitDe(tuitero, "tuit nuevo", "2025-05-01T12:00:00Z");
    publicarTodosEnTimelineDe(tuitero, tuitAntiguo, tuitReciente);

    List<Tuit> resultado = explorador.explorarPara(tuitero);

    assertOrdenadosDescendentementePorFecha(resultado);
    assertAutoresEnOrdenEsperado(resultado, tuitero, tuitero);
  }

  @Test
  void devuelveSoloTuitsDeUsuariosSeguidosOrdenadosDeManeraDescendente() {
    Usuario noSeguido = new Usuario("noSeguido");
    Usuario seguido1 = new Usuario("juan");
    Usuario seguido2 = new Usuario("pablo");
    Usuario seguidor = new Usuario("seguidor");
    Tuit tuitNoSeguido = tuitDe(noSeguido, "Hola soy leon", "2025-05-01T10:00:00Z");
    Tuit tuitAntiguo = tuitDe(seguido1, "Hola soy juan", "2025-05-01T11:00:00Z");
    Tuit tuitReciente = tuitDe(seguido2, "Hola soy pablo", "2025-05-01T12:00:00Z");
    publicarTodosEnTimelineDe(noSeguido, tuitNoSeguido);
    publicarTodosEnTimelineDe(seguidor, tuitAntiguo, tuitReciente);
    seguir(seguidor, seguido1);
    seguir(seguidor, seguido2);


    List<Tuit> resultado = explorador.explorarPara(seguidor);

    assertOrdenadosDescendentementePorFecha(resultado);
    assertAutoresEnOrdenEsperado(resultado, seguido2, seguido1);
    assertTrue(resultado.stream().noneMatch(t -> t.perteneceA(noSeguido)));
  }

  @Test
  void tuitsConMismaFechaSeDevuelvenSinError() {
    Usuario juan = new Usuario("juan");
    Usuario pablo = new Usuario("pablo");
    Usuario seguidor = new Usuario("seguidor");
    Instant fecha = Instant.parse("2025-05-10T12:00:00Z");
    Tuit tuitJuan = Tuit.nuevo(juan, "hola", Clock.fixed(fecha, ZoneOffset.UTC));
    Tuit tuitPablo = Tuit.nuevo(pablo, "chau", Clock.fixed(fecha, ZoneOffset.UTC));
    seguir(seguidor, juan);
    seguir(seguidor, pablo);
    publicarTodosEnTimelineDe(seguidor, tuitJuan, tuitPablo);


    List<Tuit> resultado = explorador.explorarPara(seguidor);


    assertEquals(2, resultado.size());
    assertTrue(resultado.contains(tuitJuan));
    assertTrue(resultado.contains(tuitPablo));
  }

  private void assertAutoresEnOrdenEsperado(List<Tuit> tuits, Usuario... autoresEsperados) {
    assertThat(tuits).extracting(Tuit::getAutor).containsExactly(autoresEsperados);
  }

  private void assertOrdenadosDescendentementePorFecha(List<Tuit> tuits) {
    assertThat(tuits).isSortedAccordingTo(
      (t1, t2) -> t2.getInstanteDeCreacion().compareTo(t1.getInstanteDeCreacion()));
  }

  private void seguir(Usuario seguidor, Usuario seguido) {
    repoDeSeguimientos.registrar(Seguimiento.nuevo(seguidor.id(), seguido.id()));
  }

  private void publicarTodosEnTimelineDe(Usuario propietario, Tuit... tuits) {
    Stream.of(tuits).forEach(t -> repoDeTimeLine.publicarTuit(propietario, t));
  }

  private Tuit tuitDe(Usuario autor, String contenido, String instante) {
    return Tuit.nuevo(autor, contenido, relojFijoEn(instante));
  }

  private Clock relojFijoEn(String text) {
    return Clock.fixed(Instant.parse(text), ZoneOffset.UTC);
  }
}
