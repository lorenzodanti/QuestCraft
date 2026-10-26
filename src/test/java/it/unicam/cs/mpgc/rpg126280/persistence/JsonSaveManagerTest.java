package it.unicam.cs.mpgc.rpg126280.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitario per verificare la serializzazione e deserializzazione JSON del salvataggio.
 */
class JsonSaveManagerTest {

    @Test
    void testSaveAndLoadGameState(@TempDir Path tempDir) throws IOException {
        JsonSaveManager saveManager = new JsonSaveManager();
        File saveFile = tempDir.resolve("test_save.json").toFile();

        // Creazione di uno stato di gioco di prova
        GameState originalState = new GameState("EroeTest", 80, 100, 20, 2, 150, 3, 4);

        // Salvataggio su file temporaneo
        saveManager.save(originalState, saveFile.getAbsolutePath());
        assertTrue(saveFile.exists());

        // Caricamento dallo stesso file
        GameState loadedState = saveManager.load(saveFile.getAbsolutePath());

        // Verifiche dei dati
        assertNotNull(loadedState);
        assertEquals("EroeTest", loadedState.getPlayerName());
        assertEquals(80, loadedState.getPlayerHealth());
        assertEquals(100, loadedState.getPlayerMaxHealth());
        assertEquals(2, loadedState.getPlayerLevel());
        assertEquals(3, loadedState.getPlayerRow());
        assertEquals(4, loadedState.getPlayerCol());
    }
}
