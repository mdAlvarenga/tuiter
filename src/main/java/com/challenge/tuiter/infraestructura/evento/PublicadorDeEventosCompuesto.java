package com.challenge.tuiter.infraestructura.evento;

import com.challenge.tuiter.aplicacion.evento.PublicadorDeEventos;

import java.util.List;

public class PublicadorDeEventosCompuesto implements PublicadorDeEventos {
  private final List<PublicadorDeEventos> publicadores;

  public PublicadorDeEventosCompuesto(List<PublicadorDeEventos> publicadores) {
    this.publicadores = publicadores;
  }

  @Override
  public void publicar(Object evento) {
    for (PublicadorDeEventos p : publicadores) {
      p.publicar(evento);
    }
  }
}
