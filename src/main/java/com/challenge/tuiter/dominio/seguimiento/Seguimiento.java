package com.challenge.tuiter.dominio.seguimiento;

public record Seguimiento(String seguidorId, String seguidoId) {
  public Seguimiento {
    if (seguidorId.equals(seguidoId)) {
      throw new SeguimientoInvalidoException("No se puede seguir a uno mismo");
    }
  }
}
