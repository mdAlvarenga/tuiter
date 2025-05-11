package com.challenge.tuiter.infraestructura.errores;

import com.challenge.tuiter.dominio.excepcion.ExcepcionDeDominio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class ManejadorDeErrores {
  @ExceptionHandler(ExcepcionDeDominio.class)
  public ResponseEntity<String> manejarExcepcionDeDominio(ExcepcionDeDominio ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> manejarErroresGenerales(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
      Map.of("error", "Ocurrió un error inesperado", "detalle", ex.getMessage(), "timestamp",
        Instant.now().toString()));
  }
}
