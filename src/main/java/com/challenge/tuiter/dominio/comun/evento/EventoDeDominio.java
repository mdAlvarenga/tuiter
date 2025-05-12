package com.challenge.tuiter.dominio.comun.evento;

import java.time.Instant;

public interface EventoDeDominio {
  String nombre();

  Instant ocurridoEn();
}
