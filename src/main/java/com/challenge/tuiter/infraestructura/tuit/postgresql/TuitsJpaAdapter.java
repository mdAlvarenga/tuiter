package com.challenge.tuiter.infraestructura.tuit.postgresql;

import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.dominio.tuit.RepositorioDeBusquedaTuits;
import com.challenge.tuiter.dominio.tuit.RepositorioDeGuardadoTuits;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class TuitsJpaAdapter implements RepositorioDeGuardadoTuits, RepositorioDeBusquedaTuits {
  private final TuitJpaRepository jpa;

  public TuitsJpaAdapter(TuitJpaRepository jpa) {
    this.jpa = jpa;
  }

  @Override
  public Tuit guardar(Tuit tuit) {
    TuitEntity entity = TuitMapper.aEntidad(tuit);
    return TuitMapper.aDominio(jpa.save(entity));
  }

  @Override
  public Optional<Tuit> buscarPorId(String id) {
    return jpa.findById(UUID.fromString(id)).map(TuitMapper::aDominio);
  }
}
