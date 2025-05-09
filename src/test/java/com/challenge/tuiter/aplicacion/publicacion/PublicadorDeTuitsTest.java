package com.challenge.tuiter.aplicacion.publicacion;

import com.challenge.tuiter.dominio.tuit.excepcion.ContenidoInvalidoException;
import com.challenge.tuiter.dominio.tuit.Tuit;
import com.challenge.tuiter.infraestructura.memoria.TuitsEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicadorDeTuitsTest {
  private TuitsEnMemoria repositorio;
  private PublicadorDeTuits publicador;

  @BeforeEach
  void setUp() {
    repositorio = new TuitsEnMemoria();
    publicador = new PublicadorDeTuits(repositorio);
  }

  @Test
  void publicaUnTuitCorrectamente() {
    var peticion = new PeticionDePublicarTuit("autor", "Hola mundo");

    Tuit tuit = publicador.publicar(peticion);

    assertEquals("autor", tuit.getAutorID());
    assertEquals("Hola mundo", tuit.getContenido());
    assertTrue(repositorio.findById(tuit.getAutorID()).isPresent());
  }

  @Test
  void lanzaExcepcionSiElContenidoEsInvalido() {
    var largo = "a".repeat(281);
    var peticion = new PeticionDePublicarTuit("autor", largo);

    assertThrows(ContenidoInvalidoException.class, () -> publicador.publicar(peticion));
  }
}
