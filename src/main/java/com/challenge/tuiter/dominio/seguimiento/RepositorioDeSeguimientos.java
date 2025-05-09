package com.challenge.tuiter.dominio.seguimiento;

import java.util.List;

public interface RepositorioDeSeguimientos {
  void guardar(Seguimiento seguimiento);

  boolean existe(Seguimiento seguimiento);

  List<String> seguidoresDe(String seguidoId);
}
