package com.challenge.tuiter.aplicacion.seguimiento;

public record PeticionDeSeguimiento(String seguidorId, String seguidoId) {
  public boolean sigueASiMimo() {
    return seguidorId.equals(seguidoId);
  }
}
