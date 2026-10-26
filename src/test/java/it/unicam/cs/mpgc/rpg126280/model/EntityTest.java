package it.unicam.cs.mpgc.rpg126280.model;

import it.unicam.cs.mpgc.rpg126280.model.entity.Monster;
import it.unicam.cs.mpgc.rpg126280.model.entity.MonsterFactory;
import it.unicam.cs.mpgc.rpg126280.model.entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per verificare la corretta logica delle entità e del combattimento.
 */
class EntityTest {

    @Test
    void testPlayerAttacksMonster() {
        Player player = new Player("Eroe", 100, 15);
        Monster goblin = MonsterFactory.createGoblin(); // Punti vita del Goblin: 30

        // Il giocatore attacca il goblin
        player.attack(goblin);

        // Ci aspettiamo che i punti vita del goblin siano scesi da 30 a 15 (30 - 15)
        assertEquals(15, goblin.getHealth());
        assertTrue(goblin.isAlive());
    }

    @Test
    void testMonsterDefeat() {
        Player player = new Player("Eroe", 100, 50); // Attacco forte per sconfiggere subito
        Monster goblin = MonsterFactory.createGoblin();

        player.attack(goblin);

        // Il goblin ha subito 50 danni su 30 PV, quindi deve essere morto (0 PV)
        assertEquals(0, goblin.getHealth());
        assertFalse(goblin.isAlive());
    }
}
