package com.challenge.tuiter.dominio.tuit.evento;

import com.challenge.tuiter.dominio.comun.evento.EventoDeDominio;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.usuario.Usuario;

import java.time.Instant;
import java.util.UUID;

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

  public Tuit aTuit() {
    return Tuit.desde(UUID.fromString(tuitId), new Usuario(autorId), contenido, instante);
  }
}
