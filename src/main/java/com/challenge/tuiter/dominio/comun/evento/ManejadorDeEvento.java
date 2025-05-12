package com.challenge.tuiter.dominio.comun.evento;

public interface ManejadorDeEvento<T extends EventoDeDominio> {
  void manejar(T evento);

  Class<T> tipoDeEvento();
}
