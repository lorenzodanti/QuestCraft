package it.unicam.cs.mpgc.rpg126280.controller;

import it.unicam.cs.mpgc.rpg126280.model.entity.Monster;
import it.unicam.cs.mpgc.rpg126280.model.entity.MonsterFactory;
import it.unicam.cs.mpgc.rpg126280.model.entity.Player;
import it.unicam.cs.mpgc.rpg126280.model.item.HealthPotion;
import it.unicam.cs.mpgc.rpg126280.model.item.Inventory;
import it.unicam.cs.mpgc.rpg126280.model.item.Item;
import it.unicam.cs.mpgc.rpg126280.model.map.Direction;
import it.unicam.cs.mpgc.rpg126280.model.map.GameMap;
import it.unicam.cs.mpgc.rpg126280.model.map.Position;
import it.unicam.cs.mpgc.rpg126280.persistence.GameState;
import it.unicam.cs.mpgc.rpg126280.persistence.JsonSaveManager;
import it.unicam.cs.mpgc.rpg126280.persistence.LeaderboardManager;

import java.io.IOException;

public class GameController {
    private Player player;
    private GameMap map;
    private Position playerPosition;
    private Inventory inventory;
    private final JsonSaveManager saveManager;
    private final LeaderboardManager leaderboardManager;
    private Monster currentEncounter;
    private boolean isDefending;
    private int monstersDefeatedCount;

    public GameController(String playerName, int mapRows, int mapCols) {
        this.saveManager = new JsonSaveManager();
        this.leaderboardManager = new LeaderboardManager();
        initGame(playerName, mapRows, mapCols);
    }

    public void initGame(String playerName, int mapRows, int mapCols) {
        this.player = new Player(playerName, 100, 30);
        this.map = new GameMap(mapRows, mapCols);
        this.playerPosition = new Position(0, 0);
        this.map.placeEntity(player, playerPosition);

        // Inizializza l'inventario con capacità 5 e aggiunge 2 Pozioni di Cura iniziali
        this.inventory = new Inventory(5);
        this.inventory.addItem(new HealthPotion("Pozione Piccola", 25));
        this.inventory.addItem(new HealthPotion("Pozione Piccola", 25));

        this.currentEncounter = null;
        this.isDefending = false;
        this.monstersDefeatedCount = 0;

        this.map.placeEntity(MonsterFactory.createGoblin(), new Position(1, 2));
        this.map.placeEntity(MonsterFactory.createOrc(), new Position(3, 1));
        this.map.placeEntity(new Monster("Drago", 60, 18, 150), new Position(4, 3));
    }

    /**
     * Tenta di usare una pozione dall'inventario.
     */
    public String usePotion() {
        if (!player.isAlive()) return "Sei stato sconfitto!";
        if (player.getHealth() >= player.getMaxHealth()) return "Salute già al massimo!";

        // Cerca una pozione nell'inventario
        for (Item item : inventory.getItems()) {
            if (item instanceof HealthPotion) {
                if (inventory.useItem(item, player)) {
                    return "🧪 Hai usato una Pozione! +25 PV. Pozioni rimaste: " + getPotionCount();
                }
            }
        }
        return "⚠️ Nessuna pozione disponibile nell'inventario!";
    }

    public int getPotionCount() {
        int count = 0;
        for (Item item : inventory.getItems()) {
            if (item instanceof HealthPotion) count++;
        }
        return count;
    }

    public boolean checkVictory() {
        return playerPosition.getRow() == map.getRows() - 1 && playerPosition.getCol() == map.getCols() - 1;
    }

    public String movePlayer(Direction direction) {
        if (currentEncounter != null) return "Sei in combattimento!";

        Position newPos = playerPosition.move(direction);

        if (!map.isWithinBounds(newPos)) {
            return "Hai raggiunto il confine della mappa!";
        }

        var targetEntity = map.getEntityAt(newPos);

        if (targetEntity instanceof Monster monster) {
            this.currentEncounter = monster;
            return "COMBAT_START";
        }

        map.removeEntityAt(playerPosition);
        playerPosition = newPos;
        map.placeEntity(player, playerPosition);

        if (checkVictory()) {
            recordFinalScore();
            return "VICTORY";
        }

        return "Ti sei mosso verso " + direction;
    }

    public String playerAttack() {
        if (currentEncounter == null) return "";
        player.attack(currentEncounter);
        if (!currentEncounter.isAlive()) {
            map.removeEntityAt(playerPosition.move(getEncounterDirection()));
            player.addExperience(currentEncounter.getExpReward());
            monstersDefeatedCount++;
            Monster defeated = currentEncounter;
            currentEncounter = null;
            return "Hai sconfitto " + defeated.getName() + "! Guadagnati " + defeated.getExpReward() + " EXP.";
        }
        return monsterTurn();
    }

    public String playerDefend() {
        if (currentEncounter == null) return "";
        this.isDefending = true;
        return monsterTurn();
    }

    private String monsterTurn() {
        int damage = currentEncounter.getAttackPower();
        if (isDefending) {
            damage = Math.max(1, damage / 2);
            isDefending = false;
        }
        player.takeDamage(damage);
        if (!player.isAlive()) {
            recordFinalScore();
            return "GAME_OVER";
        }
        return currentEncounter.getName() + " ti attacca infliggendo " + damage + " danni!";
    }

    public void recordFinalScore() {
        int finalScore = (monstersDefeatedCount * 50) + player.getExperience() + (player.getHealth() * 2);
        leaderboardManager.addScore(player.getName(), finalScore, monstersDefeatedCount, player.getLevel());
    }

    private Direction getEncounterDirection() {
        for (Direction d : Direction.values()) {
            Position check = playerPosition.move(d);
            if (map.getEntityAt(check) == currentEncounter) return d;
        }
        return Direction.RIGHT;
    }

    public void saveGame(String filePath) throws IOException {
        GameState state = new GameState(
                player.getName(), player.getHealth(), player.getMaxHealth(),
                player.getAttackPower(), player.getLevel(), player.getExperience(),
                playerPosition.getRow(), playerPosition.getCol()
        );
        saveManager.save(state, filePath);
    }

    public Player getPlayer() { return player; }
    public GameMap getMap() { return map; }
    public Position getPlayerPosition() { return playerPosition; }
    public Monster getCurrentEncounter() { return currentEncounter; }
    public LeaderboardManager getLeaderboardManager() { return leaderboardManager; }
    public int getMonstersDefeatedCount() { return monstersDefeatedCount; }
    public Inventory getInventory() { return inventory; }
}