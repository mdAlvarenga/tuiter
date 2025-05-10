package com.challenge.tuiter.infraestructura.memoria;

import com.challenge.tuiter.dominio.tuit.RepositorioDeBusquedaTuits;
import com.challenge.tuiter.dominio.tuit.RepositorioDeGuardadoTuits;
import com.challenge.tuiter.dominio.tuit.Tuit;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GuardadoTuitsEnMemoria implements RepositorioDeGuardadoTuits, RepositorioDeBusquedaTuits {
  private final Map<String, Tuit> store = new HashMap<>();

  @Override
  public Tuit guardar(Tuit tuit) {
    store.put(tuit.getId().toString(), tuit);
    return tuit;
  }

  @Override
  public Optional<Tuit> buscarPorId(String id) {
    return Optional.ofNullable(store.get(id));
  }
}
