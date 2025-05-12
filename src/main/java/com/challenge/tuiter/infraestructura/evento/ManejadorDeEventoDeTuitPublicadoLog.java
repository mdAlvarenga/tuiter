package com.challenge.tuiter.infraestructura.evento;

import com.challenge.tuiter.dominio.comun.evento.ManejadorDeEvento;
import com.challenge.tuiter.dominio.tuit.evento.EventoDeTuitPublicado;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ManejadorDeEventoDeTuitPublicadoLog implements ManejadorDeEvento<EventoDeTuitPublicado> {
  @Override
  public void manejar(EventoDeTuitPublicado evento) {
    log.info("Evento TuitPublicado - id={}, autor={}, contenido={}, instante={}", evento.tuitId(),
      evento.autorId(), evento.contenido(), evento.instante());
  }

  @Override
  public Class<EventoDeTuitPublicado> tipoDeEvento() {
    return EventoDeTuitPublicado.class;
  }
}
