package com.challenge.tuiter.infraestructura.controlador.timeline.dto;

import java.time.Instant;

public record TuitDTO(String id, String contenido, String autorId, Instant fechaCreacion) {}
