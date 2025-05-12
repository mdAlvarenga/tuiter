package com.challenge.tuiter.dominio.tuit.evento;

import com.challenge.tuiter.dominio.comun.evento.EventoDeDominio;

import java.time.Instant;

public record EventoDeTuitPublicado(String tuitId, String autorId, String contenido,
                                    Instant instante) implements EventoDeDominio {

  @Override
  public String nombre() {
    return "EventoDeTuitPublicado";
  }

  @Override
  public Instant ocurridoEn() {
    return instante;
  }
}
