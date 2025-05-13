package com.challenge.tuiter.infraestructura.controlador.timeline.dto;

import java.time.Instant;

public record TuitResponseDTO(String id, String contenido, String autorId, Instant fechaCreacion) {}
