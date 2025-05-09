package com.challenge.tuiter.infraestructura.memoria;

import com.challenge.tuiter.dominio.tuit.RepositorioDeTuits;
import com.challenge.tuiter.dominio.tuit.Tuit;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TuitsEnMemoria implements RepositorioDeTuits {
  private final Map<String, Tuit> store = new HashMap<>();

  @Override
  public Tuit guardar(Tuit tuit) {
    store.put(tuit.getAutor(), tuit);
    return tuit;
  }

  @Override
  public Optional<Tuit> findById(String id) {
    return Optional.ofNullable(store.get(id));
  }
}
