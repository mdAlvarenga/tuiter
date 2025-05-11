package com.challenge.tuiter.infraestructura.controlador.usuario.dto;

import java.time.Instant;

public record TuitDTO(String id, String contenido, String autorId, Instant fechaCreacion) {}
