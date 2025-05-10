package com.challenge.tuiter.dominio.tuit;

import java.util.Optional;

public interface RepositorioDeBusquedaTuits {
  Optional<Tuit> buscarPorId(String id);
}
