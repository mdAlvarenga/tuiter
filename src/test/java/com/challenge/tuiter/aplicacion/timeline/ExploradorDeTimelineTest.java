package com.challenge.tuiter.aplicacion.timeline;

import com.challenge.tuiter.dominio.seguimiento.Seguimiento;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.memoria.SeguimientosEnMemoriaDeConsulta;
import com.challenge.tuiter.infraestructura.memoria.TimelineEnMemoriaDeConsulta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExploradorDeTimelineTest {
  private ExploradorDeTimeline explorador;
  private TimelineEnMemoriaDeConsulta repoDeTimeLine;
  private SeguimientosEnMemoriaDeConsulta repoDeSeguimientos;

  @BeforeEach
  void setUp() {
    repoDeTimeLine = new TimelineEnMemoriaDeConsulta();
    repoDeSeguimientos = new SeguimientosEnMemoriaDeConsulta();
    explorador = new ExploradorDeTimeline(repoDeTimeLine, repoDeSeguimientos);
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
  void devuelveSoloTuitsDeUsuariosSeguidosOrdenadosDeManeraDescendente() {
    Usuario noSeguido = new Usuario("noSeguido");
    Usuario seguido1 = new Usuario("juan");
    Usuario seguido2 = new Usuario("pablo");
    Usuario seguidor = new Usuario("seguidor");
    guardarTuitsPara(noSeguido, seguido1, seguido2);

    seguir(seguidor, seguido1);
    seguir(seguidor, seguido2);

    ExploradorDeTimeline explorador = new ExploradorDeTimeline(repoDeTimeLine, repoDeSeguimientos);


    List<Tuit> resultado = explorador.explorarPara(seguidor);


    assertContieneOrdenadosExactamenteTuitsDe(resultado, seguido2, seguido1);
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
    agregarTuits(tuitJuan, tuitPablo);

    List<Tuit> resultado = explorador.explorarPara(seguidor);

    assertEquals(2, resultado.size());
    assertTrue(resultado.contains(tuitJuan));
    assertTrue(resultado.contains(tuitPablo));
  }


  private void guardarTuitsPara(Usuario noSeguido, Usuario seguido1, Usuario seguido2) {
    Tuit tuitNoSeguido = tuitDe(noSeguido, "Hola soy leon", "2025-05-01T10:00:00Z");
    Tuit tuitAntiguo = tuitDe(seguido1, "Hola soy juan", "2025-05-01T11:00:00Z");
    Tuit tuitReciente = tuitDe(seguido2, "Hola soy pablo", "2025-05-01T12:00:00Z");
    agregarTuits(tuitNoSeguido, tuitAntiguo, tuitReciente);
  }

  void assertContieneOrdenadosExactamenteTuitsDe(List<Tuit> tuits, Usuario... autoresEsperados) {
    assertThat(tuits).isSortedAccordingTo(
      (t1, t2) -> t2.getInstanteDeCreacion().compareTo(t1.getInstanteDeCreacion()));

    assertThat(tuits).extracting(Tuit::getAutor).containsExactly(autoresEsperados);
  }

  private void seguir(Usuario seguidor, Usuario seguido) {
    repoDeSeguimientos.registrar(Seguimiento.nuevo(seguidor.id(), seguido.id()));
  }

  private void agregarTuits(Tuit... tuits) {
    for (Tuit t : tuits)
      repoDeTimeLine.agregarTuit(t);
  }

  private Tuit tuitDe(Usuario autor, String contenido, String instante) {
    return Tuit.nuevo(autor, contenido, relojFake(instante));
  }

  private Usuario agregarTuits(String relojAntiguo, String relojReciente) {
    Usuario autor = new Usuario("autor");
    Tuit antiguo = tuitDe(autor, "Tuit viejo", relojAntiguo);
    Tuit reciente = tuitDe(autor, "Tuit nuevo", relojReciente);
    repoDeTimeLine.agregarTuit(antiguo);
    repoDeTimeLine.agregarTuit(reciente);
    return autor;
  }

  private Clock relojFake(String text) {
    return Clock.fixed(Instant.parse(text), ZoneOffset.UTC);
  }
}
