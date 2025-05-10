package com.challenge.tuiter.infraestructura.seguimiento.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SeguimientoJpaRepository extends JpaRepository<SeguimientoEntidad, UUID> {
  boolean existsBySeguidorIdAndSeguidoId(String seguidorId, String seguidoId);

  List<SeguimientoEntidad> findBySeguidorId(String seguidorId);
}
