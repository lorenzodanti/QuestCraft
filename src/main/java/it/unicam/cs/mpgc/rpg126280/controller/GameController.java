package it.unicam.cs.mpgc.rpg126280.controller;

import it.unicam.cs.mpgc.rpg126280.model.entity.Monster;
import it.unicam.cs.mpgc.rpg126280.model.entity.MonsterFactory;
import it.unicam.cs.mpgc.rpg126280.model.entity.Player;
import it.unicam.cs.mpgc.rpg126280.model.map.Direction;
import it.unicam.cs.mpgc.rpg126280.model.map.GameMap;
import it.unicam.cs.mpgc.rpg126280.model.map.Position;
import it.unicam.cs.mpgc.rpg126280.persistence.GameState;
import it.unicam.cs.mpgc.rpg126280.persistence.JsonSaveManager;

import java.io.IOException;

/**
 * Controller principale dell'applicazione (Architettura MVC).
 * Coordina l'interazione tra la mappa, le entità, la persistenza e l'interfaccia utente.
 */
public class GameController {
    private final Player player;
    private final GameMap map;
    private Position playerPosition;
    private final JsonSaveManager saveManager;

    public GameController(String playerName, int mapRows, int mapCols) {
        this.player = new Player(playerName, 100, 20);
        this.map = new GameMap(mapRows, mapCols);
        this.playerPosition = new Position(0, 0);
        this.map.placeEntity(player, playerPosition);
        this.saveManager = new JsonSaveManager();

        // Posizioniamo qualche nemico di prova sulla mappa
        this.map.placeEntity(MonsterFactory.createGoblin(), new Position(1, 1));
        this.map.placeEntity(MonsterFactory.createOrc(), new Position(3, 3));
    }

    /**
     * Tenta di muovere il giocatore nella direzione specificata.
     * Gestisce anche la logica degli scontri se la cella contiene un mostro.
     */
    public String movePlayer(Direction direction) {
        Position newPos = playerPosition.move(direction);

        if (!map.isWithinBounds(newPos)) {
            return "Hai raggiunto il limite della mappa!";
        }

        var targetEntity = map.getEntityAt(newPos);

        if (targetEntity instanceof Monster monster) {
            // Se c'è un mostro nella cella, il giocatore lo attacca
            player.attack(monster);
            if (!monster.isAlive()) {
                map.removeEntityAt(newPos);
                player.addExperience(monster.getExpReward());
                return "Hai sconfitto " + monster.getName() + "! Guadagnati " + monster.getExpReward() + " ESP.";
            } else {
                // Il mostro sopravvive e contrattacca
                monster.attack(player);
                return "Hai attaccato " + monster.getName() + "! Il nemico contrattacca. Tuoi PV: " + player.getHealth();
            }
        }

        // Spostamento libero
        map.removeEntityAt(playerPosition);
        playerPosition = newPos;
        map.placeEntity(player, playerPosition);
        return "Ti sei mosso verso " + direction + ". Nuova posizione: (" + playerPosition.getRow() + ", " + playerPosition.getCol() + ")";
    }

    /**
     * Salva lo stato del gioco su file JSON.
     */
    public void saveGame(String filePath) throws IOException {
        GameState state = new GameState(
                player.getName(),
                player.getHealth(),
                player.getMaxHealth(),
                player.getAttackPower(),
                player.getLevel(),
                player.getExperience(),
                playerPosition.getRow(),
                playerPosition.getCol()
        );
        saveManager.save(state, filePath);
    }

    // Getters per consentire alla View di leggere lo stato attuale
    public Player getPlayer() { return player; }
    public GameMap getMap() { return map; }
    public Position getPlayerPosition() { return playerPosition; }
}
