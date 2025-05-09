package com.challenge.tuiter.dominio.tuit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TuitTest {
  @Test
  void creaUnTuitConAutorYContenidoValidos() {
    Tuit tuit = new Tuit("autor", "Hola mundo");
    assertEquals("autor", tuit.autor());
    assertEquals("Hola mundo", tuit.contenido());
  }

  @Test
  void noPermiteContenidoVacio() {
    assertThrows(ContenidoInvalidoException.class, () -> new Tuit("autor", ""));
  }

  @Test
  void noPermiteContenidoConMasDe280Caracteres() {
    String largo = "a".repeat(281);
    assertThrows(ContenidoInvalidoException.class, () -> new Tuit("autor", largo));
  }
}
