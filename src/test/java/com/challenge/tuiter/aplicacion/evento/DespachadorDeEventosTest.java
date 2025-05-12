package com.challenge.tuiter.aplicacion.evento;

import com.challenge.tuiter.dominio.comun.evento.EventoDeDominio;
import com.challenge.tuiter.dominio.comun.evento.ManejadorDeEvento;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DespachadorDeEventosTest {
  @Test
  void despachaUnEventoConUnManejadorDisponible() {
    EventoDummy evento = new EventoDummy();
    @SuppressWarnings("unchecked") ManejadorDeEvento<EventoDummy> manejador = mock(
      ManejadorDeEvento.class);
    when(manejador.tipoDeEvento()).thenReturn(EventoDummy.class);

    var despachador = new DespachadorDeEventos(List.of(manejador));
    despachador.despachar(List.of(evento));

    verify(manejador, times(1)).manejar(evento);
  }

  @Test
  void noHaceNadaSiNoHayManejadorDisponible() {
    var despachador = new DespachadorDeEventos(List.of());
    assertDoesNotThrow(() -> despachador.despachar(List.of(new EventoDummy())));
  }

  @Test
  void manejaMultiplesEventosConSusRespectivosManejadores() {
    var evento1 = new EventoDummy();
    var evento2 = new OtroEventoDummy();

    ManejadorDeEvento<EventoDummy> manejador1 = mock(ManejadorDeEvento.class);
    ManejadorDeEvento<OtroEventoDummy> manejador2 = mock(ManejadorDeEvento.class);

    when(manejador1.tipoDeEvento()).thenReturn(EventoDummy.class);
    when(manejador2.tipoDeEvento()).thenReturn(OtroEventoDummy.class);

    var despachador = new DespachadorDeEventos(List.of(manejador1, manejador2));
    despachador.despachar(List.of(evento1, evento2));

    verify(manejador1).manejar(evento1);
    verify(manejador2).manejar(evento2);
  }

  static class EventoDummy implements EventoDeDominio {
    public String nombre() {return "EventoDummy";}

    public Instant ocurridoEn() {return Instant.now();}
  }

  static class OtroEventoDummy implements EventoDeDominio {
    public String nombre() {return "OtroEventoDummy";}

    public Instant ocurridoEn() {return Instant.now();}
  }
}
