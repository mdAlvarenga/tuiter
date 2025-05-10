package com.challenge.tuiter.infraestructura.timeline.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TimelineJpaRepository extends JpaRepository<TimelineTuitEntity, UUID> {
  List<TimelineTuitEntity> findAllByPropietarioIdIn(List<String> ids);
}
