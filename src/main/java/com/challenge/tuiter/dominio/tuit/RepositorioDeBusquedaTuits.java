package com.challenge.tuiter.dominio.tuit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepositorioDeBusquedaTuits {
  Optional<Tuit> buscarPorId(String id);

  List<Tuit> buscarTodosPorId(List<UUID> uuids);
}
