package com.challenge.tuiter.dominio.seguimiento;

import java.util.List;

public interface RepositorioDeSeguimientos {
  void guardar(Seguimiento seguimiento);

  boolean existe(RelacionDeSeguimiento relacion);

  List<String> seguidoresDe(String seguidoId);
}
