package com.challenge.tuiter.aplicacion.timeline;

import com.challenge.tuiter.dominio.timeline.RepositorioDeTimeline;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;
import com.challenge.tuiter.infraestructura.memoria.TimelineEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExploradorDeTimelineTest {
  private ExploradorDeTimeline explorador;
  private TimelineEnMemoria repoDeTimeLine;

  @BeforeEach
  void setUp() {
    repoDeTimeLine = new TimelineEnMemoria();
    explorador = new ExploradorDeTimeline(repoDeTimeLine);
  }

  @Test
  void devuelveTimelineVacioParaUnUsuarioSiNoHayTuits() {
    var resultado = explorador.explorarPara(new Usuario("autor"));

    assertTrue(resultado.isEmpty());
  }

  @Test
  void devuelveTuitsDelUsuarioOrdenadosPorFechaDescendente() {
    String relojAntiguo = "2025-05-01T12:00:00Z";
    String relojReciente = "2025-05-02T12:00:00Z";
    Usuario autor = agregarTuits(relojAntiguo, relojReciente);
    explorador = new ExploradorDeTimeline(repoDeTimeLine);

    List<Tuit> resultado = explorador.explorarPara(autor);

    assertEquals(relojFake(relojReciente).instant(), resultado.get(0).getInstanteDeCreacion());
    assertEquals(relojFake(relojAntiguo).instant(), resultado.get(1).getInstanteDeCreacion());
  }

  @Test
  void devuelveSoloTuitsDeUsuariosSeguidos() {
    Usuario noSeguido = new Usuario("noSeguido");
    Usuario seguido1 = new Usuario("juan");
    Usuario seguido2 = new Usuario("pablo");
    Usuario seguidor = new Usuario("seguidor");

    Tuit tuitNoSeguido = tuitDe(noSeguido, "Hola soy leon", "2025-05-01T10:00:00Z");
    Tuit tuit1 = tuitDe(seguido1, "Hola soy juan", "2025-05-01T11:00:00Z");
    Tuit tuit2 = tuitDe(seguido2, "Hola soy pablo", "2025-05-01T12:00:00Z");

    RepositorioDeTimeline repo = usuario -> usuario.equals(seguidor) ? List.of(tuit1,
      tuit2) : List.of();

    ExploradorDeTimeline explorador = new ExploradorDeTimeline(repo);


    List<Tuit> resultado = explorador.explorarPara(seguidor);


    assertContieneSoloTuitsDe(resultado, seguido1, seguido2);
    assertTrue(resultado.stream().noneMatch(t -> t.perteneceA(noSeguido)));
  }

  void assertContieneSoloTuitsDe(List<Tuit> tuits, Usuario... autoresEsperados) {
    var autores = Arrays.stream(autoresEsperados).toList();
    assertEquals(autores.size(), tuits.size());
    for (Usuario autor : autores) {
      assertTrue(tuits.stream().anyMatch(t -> t.perteneceA(autor)), "Falta tuit de: " + autor);
    }
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
