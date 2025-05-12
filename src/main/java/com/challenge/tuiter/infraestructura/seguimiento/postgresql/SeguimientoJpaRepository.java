package com.challenge.tuiter.infraestructura.seguimiento.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SeguimientoJpaRepository extends JpaRepository<SeguimientoEntity, UUID> {
  boolean existsBySeguidorIdAndSeguidoId(String seguidorId, String seguidoId);

  List<SeguimientoEntity> findBySeguidoId(String seguidoId);

  List<SeguimientoEntity> findBySeguidorId(String seguidorId);
}
