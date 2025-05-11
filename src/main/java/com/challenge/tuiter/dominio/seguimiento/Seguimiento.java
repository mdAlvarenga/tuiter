package com.challenge.tuiter.dominio.seguimiento;

import com.challenge.tuiter.dominio.seguimiento.excepcion.SeguimientoInvalidoException;
import lombok.Value;

import java.util.UUID;

@Value
public class Seguimiento {
  UUID id;
  String seguidorId;
  String seguidoId;

  private Seguimiento(UUID id, String seguidorId, String seguidoId) {
    if (seguidorId.equals(seguidoId)) {
      throw new SeguimientoInvalidoException("No se puede seguir a uno mismo");
    }
    this.id = id;
    this.seguidorId = seguidorId;
    this.seguidoId = seguidoId;
  }

  public static Seguimiento nuevo(String seguidorId, String seguidoId) {
    return new Seguimiento(UUID.randomUUID(), seguidorId, seguidoId);
  }

  public static Seguimiento desde(UUID id, String seguidorId, String seguidoId) {
    return new Seguimiento(id, seguidorId, seguidoId);
  }

  public String seguimientoId() {
    return id.toString();
  }

  public boolean esSeguidorDe(String otroSeguidoId) {
    return seguidoId.equals(otroSeguidoId);
  }

  public boolean esSeguidoPor(String otroSeguidorId) {
    return seguidorId.equals(otroSeguidorId);
  }
}