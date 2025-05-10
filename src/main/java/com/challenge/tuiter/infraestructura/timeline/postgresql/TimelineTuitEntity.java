package com.challenge.tuiter.infraestructura.timeline.postgresql;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "timeline")
public class TimelineTuitEntity {
  @Id
  private UUID id;

  @Column(nullable = false)
  private String autorId;

  @Column(nullable = false)
  private String propietarioId;

  @Column(nullable = false)
  private String contenido;

  @Column(nullable = false)
  private Instant instanteDeCreacion;
}
