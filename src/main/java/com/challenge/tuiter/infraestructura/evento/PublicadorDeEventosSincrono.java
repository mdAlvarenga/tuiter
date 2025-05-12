package com.challenge.tuiter.infraestructura.evento;

import com.challenge.tuiter.aplicacion.evento.DespachadorDeEventos;
import com.challenge.tuiter.aplicacion.evento.PublicadorDeEventos;
import com.challenge.tuiter.dominio.comun.evento.EventoDeDominio;

import java.util.List;

public class PublicadorDeEventosSincrono implements PublicadorDeEventos {
  private final DespachadorDeEventos despachador;

  public PublicadorDeEventosSincrono(DespachadorDeEventos despachador) {
    this.despachador = despachador;
  }

  @Override
  public void publicar(Object evento) {
    if (evento instanceof EventoDeDominio e) {
      despachador.despachar(List.of(e));
    } else {
      throw new IllegalArgumentException("Evento inválido: " + evento);
    }
  }
}
