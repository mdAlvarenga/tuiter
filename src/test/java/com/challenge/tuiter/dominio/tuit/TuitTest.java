package com.challenge.tuiter.dominio.tuit;

import com.challenge.tuiter.dominio.tuit.excepcion.AutorInvalidoException;
import com.challenge.tuiter.dominio.tuit.excepcion.ContenidoInvalidoException;
import com.challenge.tuiter.dominio.usuario.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TuitTest {
  @Test
  void creaUnTuitConAutorYContenidoValidos() {
    Tuit tuit = Tuit.nuevo(new Usuario("autor"), "Hola mundo");
    assertNotNull(tuit.getId());
    assertEquals("autor", tuit.getAutorID());
    assertEquals("Hola mundo", tuit.getContenido());
  }

  @Test
  void creaDosTuitsValidosConIdDiferente() {
    Tuit unTuit = Tuit.nuevo(new Usuario("autor"), "Hola mundo");
    Tuit otroTuit = Tuit.nuevo(new Usuario("autor"), "Hola mundo");

    assertNotEquals(unTuit.getId(), otroTuit.getId());
  }

  @Test
  void noPermiteContenidoVacio() {
    assertThrows(ContenidoInvalidoException.class, () -> Tuit.nuevo(new Usuario("autor"), ""));
  }

  @Test
  void noPermiteContenidoConMasDe280Caracteres() {
    String largo = "a".repeat(281);
    assertThrows(ContenidoInvalidoException.class, () -> Tuit.nuevo(new Usuario("autor"), largo));
  }

  @Test
  void noPermiteAutorVacio() {
    assertThrows(AutorInvalidoException.class, () -> Tuit.nuevo(null, "contenido"));
  }
}
