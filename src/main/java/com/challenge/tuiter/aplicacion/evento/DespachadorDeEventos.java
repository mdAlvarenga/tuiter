package com.challenge.tuiter.aplicacion.evento;

import com.challenge.tuiter.dominio.comun.evento.EventoDeDominio;
import com.challenge.tuiter.dominio.comun.evento.ManejadorDeEvento;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DespachadorDeEventos {
  private final Map<Class<? extends EventoDeDominio>, ManejadorDeEvento<?>> manejadores = new HashMap<>();

  public DespachadorDeEventos(List<ManejadorDeEvento<?>> manejadores) {
    for (var manejador : manejadores) {
      this.manejadores.put(manejador.tipoDeEvento(), manejador);
    }
  }

  public void despachar(List<? extends EventoDeDominio> eventos) {
    for (EventoDeDominio evento : eventos) {
      var manejador = getManejador(evento);
      if (manejador != null) {
        manejador.manejar(evento);
      } else {
        log.warn("Evento sin manejador: {}", evento.getClass().getSimpleName());
      }
    }
  }

  @SuppressWarnings("unchecked")
  private <T extends EventoDeDominio> ManejadorDeEvento<T> getManejador(EventoDeDominio evento) {
    return (ManejadorDeEvento<T>) manejadores.get(evento.getClass());
  }
}
