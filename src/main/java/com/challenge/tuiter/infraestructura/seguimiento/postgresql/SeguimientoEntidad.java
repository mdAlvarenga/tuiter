package com.challenge.tuiter.infraestructura.seguimiento.postgresql;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seguimientos")
public class SeguimientoEntidad {
  @Id
  private UUID id;
  private String seguidorId;
  private String seguidoId;

}
