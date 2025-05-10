package com.challenge.tuiter.infraestructura.tuit.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TuitJpaRepository extends JpaRepository<TuitEntity, UUID> {}
