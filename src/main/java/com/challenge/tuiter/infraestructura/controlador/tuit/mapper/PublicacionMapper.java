package com.challenge.tuiter.infraestructura.controlador.tuit.mapper;

import com.challenge.tuiter.aplicacion.publicacion.PeticionDePublicarTuit;
import com.challenge.tuiter.infraestructura.controlador.tuit.dto.PeticionDePublicacionDTO;
import com.challenge.tuiter.infraestructura.controlador.tuit.dto.RespuestaDePublicacionDTO;

import java.util.UUID;

public class PublicacionMapper {
  public static PeticionDePublicarTuit aDominio(PeticionDePublicacionDTO dto) {
    return new PeticionDePublicarTuit(dto.autorId(), dto.contenido());
  }

  public static RespuestaDePublicacionDTO aDto(UUID idGenerado) {
    return new RespuestaDePublicacionDTO(idGenerado.toString());
  }
}
