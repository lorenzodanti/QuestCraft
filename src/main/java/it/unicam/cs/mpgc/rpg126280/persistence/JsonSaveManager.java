package it.unicam.cs.mpgc.rpg126280.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Gestisce il salvataggio e il caricamento del GameState su file JSON utilizzando la libreria Gson.
 */
public class JsonSaveManager {
    private final Gson gson;

    public JsonSaveManager() {
        // Usa setPrettyPrinting per generare file JSON ben formattati e leggibili
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Salva lo stato del gioco nel file specificato.
     * @param state L'oggetto GameState da salvare.
     * @param filePath Il percorso del file di destinazione (es. "savegame.json").
     */
    public void save(GameState state, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(state, writer);
        }
    }

    /**
     * Carica lo stato del gioco da un file JSON.
     * @param filePath Il percorso del file da leggere.
     * @return L'oggetto GameState caricato.
     */
    public GameState load(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, GameState.class);
        }
    }
}
