package com.challenge.tuiter.dominio.seguimiento;

import com.challenge.tuiter.dominio.usuario.Usuario;

import java.util.List;

public interface RepositorioDeConsultaDeSeguimientos {

  boolean existe(RelacionDeSeguimiento relacion);

  List<Usuario> seguidosPor(String seguidorId);
}
