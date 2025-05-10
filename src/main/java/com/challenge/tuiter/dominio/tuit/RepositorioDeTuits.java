package com.challenge.tuiter.dominio.tuit;

import java.util.Optional;

public interface RepositorioDeTuits {
  Tuit guardar(Tuit tuit);

  Optional<Tuit> findById(String id);
}
