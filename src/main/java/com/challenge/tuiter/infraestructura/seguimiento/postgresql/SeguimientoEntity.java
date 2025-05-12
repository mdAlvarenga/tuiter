package com.challenge.tuiter.infraestructura.seguimiento.postgresql;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seguimientos")
public class SeguimientoEntity {
  @Id
  private UUID id;

  @Column(nullable = false)
  private String seguidorId;

  @Column(nullable = false)
  private String seguidoId;

}
