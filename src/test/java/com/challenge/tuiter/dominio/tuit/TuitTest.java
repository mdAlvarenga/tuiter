package com.challenge.tuiter.dominio.tuit;

import com.challenge.tuiter.dominio.tuit.excepcion.AutorInvalidoException;
import com.challenge.tuiter.dominio.tuit.excepcion.ContenidoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TuitTest {
  @Test
  void creaUnTuitConAutorYContenidoValidos() {
    Tuit tuit = Tuit.nuevo("autor", "Hola mundo");
    assertNotNull(tuit.getId());
    assertEquals("autor", tuit.getAutor());
    assertEquals("Hola mundo", tuit.getContenido());
  }

  @Test
  void noPermiteContenidoVacio() {
    assertThrows(ContenidoInvalidoException.class, () -> Tuit.nuevo("autor", ""));
  }

  @Test
  void noPermiteContenidoConMasDe280Caracteres() {
    String largo = "a".repeat(281);
    assertThrows(ContenidoInvalidoException.class, () -> Tuit.nuevo("autor", largo));
  }

  @Test
  void noPermiteAutorVacio() {
    assertThrows(AutorInvalidoException.class, () -> Tuit.nuevo("", "contenido"));
  }
}
