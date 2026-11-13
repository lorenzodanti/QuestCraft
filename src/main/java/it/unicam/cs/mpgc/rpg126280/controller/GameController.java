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

/**
 * Controller principale del gioco.
 * Coordina lo stato della partita, il movimento sulla mappa, la gestione dell'inventario,
 * le meccaniche di combattimento in tempo reale e la persistenza dei dati.
 */
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

    /**
     * Costruisce un nuovo GameController e avvia una nuova partita.
     *
     * @param playerName Nome dell'eroe scelto dal giocatore.
     * @param mapRows    Numero di righe della mappa di gioco.
     * @param mapCols    Numero di colonne della mappa di gioco.
     */
    public GameController(String playerName, int mapRows, int mapCols) {
        this.saveManager = new JsonSaveManager();
        this.leaderboardManager = new LeaderboardManager();
        initGame(playerName, mapRows, mapCols);
    }

    /**
     * Inizializza o resetta lo stato del gioco, posizionando il giocatore,
     * popolando l'inventario iniziale e generando i nemici sulla mappa.
     *
     * @param playerName Nome del giocatore.
     * @param mapRows    Numero di righe della mappa.
     * @param mapCols    Numero di colonne della mappa.
     */
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

        // Posizionamento dei mostri sulla griglia
        this.map.placeEntity(MonsterFactory.createGoblin(), new Position(1, 2));
        this.map.placeEntity(MonsterFactory.createOrc(), new Position(3, 1));
        this.map.placeEntity(new Monster("Drago", 60, 18, 150), new Position(4, 3));
    }

    /**
     * Tenta di usare una pozione curativa dall'inventario per ripristinare i PV del giocatore.
     *
     * @return Messaggio descrittivo sull'esito dell'utilizzo della pozione.
     */
    public String usePotion() {
        if (!player.isAlive()) return "Sei stato sconfitto!";
        if (player.getHealth() >= player.getMaxHealth()) return "Salute già al massimo!";

        // Cerca la prima pozione disponibile nell'inventario
        for (Item item : inventory.getItems()) {
            if (item instanceof HealthPotion) {
                if (inventory.useItem(item, player)) {
                    return "🧪 Hai usato una Pozione! +25 PV. Pozioni rimaste: " + getPotionCount();
                }
            }
        }
        return "⚠️ Nessuna pozione disponibile nell'inventario!";
    }

    /**
     * Conta il numero di pozioni curative attualmente presenti nell'inventario.
     *
     * @return Quantità di {@link HealthPotion} disponibili.
     */
    public int getPotionCount() {
        int count = 0;
        for (Item item : inventory.getItems()) {
            if (item instanceof HealthPotion) count++;
        }
        return count;
    }

    /**
     * Verifica se il giocatore ha raggiunto la casella traguardo in basso a destra.
     *
     * @return {@code true} se il giocatore si trova sulla cella finale, {@code false} altrimenti.
     */
    public boolean checkVictory() {
        return playerPosition.getRow() == map.getRows() - 1 && playerPosition.getCol() == map.getCols() - 1;
    }

    /**
     * Gestisce lo spostamento del giocatore nella direzione indicata,
     * rilevando eventuali collisioni con i bordi, incontri con mostri o il raggiungimento della meta.
     *
     * @param direction Direzione di movimento scelta.
     * @return Codice o messaggio di stato ("COMBAT_START", "VICTORY", oppure descrizione del movimento).
     */
    public String movePlayer(Direction direction) {
        if (currentEncounter != null) return "Sei in combattimento!";

        Position newPos = playerPosition.move(direction);

        if (!map.isWithinBounds(newPos)) {
            return "Hai raggiunto il confine della mappa!";
        }

        var targetEntity = map.getEntityAt(newPos);

        // Avvia lo scontro se la casella di destinazione è occupata da un mostro
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

    /**
     * Esegue l'attacco del giocatore contro il mostro attualmente affrontato.
     * Se il mostro viene sconfitto, lo rimuove dalla mappa e assegna l'esperienza;
     * altrimenti scatena immediatamente la risposta del mostro.
     *
     * @return Esito dello scontro o descrizione dell'attacco nemico.
     */
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

    /**
     * Attiva la postura difensiva del giocatore, dimezzando il danno del prossimo attacco nemico.
     *
     * @return Risultato dell'attacco successivo del mostro.
     */
    public String playerDefend() {
        if (currentEncounter == null) return "";
        this.isDefending = true;
        return monsterTurn();
    }

    /**
     * Esegue l'attacco del mostro contro il giocatore.
     * Viene chiamato sia in risposta alle mosse del giocatore sia dal timer automatico
     * del combattimento in tempo reale.
     *
     * @return "GAME_OVER" se i PV del giocatore scendono a 0, altrimenti il log del danno inflitto.
     */
    public String monsterTurn() {
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

    /**
     * Calcola il punteggio finale della partita in base ai mostri sconfitti,
     * all'esperienza accumulata e ai PV rimanenti, salvandolo nella classifica.
     */
    public void recordFinalScore() {
        int finalScore = (monstersDefeatedCount * 50) + player.getExperience() + (player.getHealth() * 2);
        leaderboardManager.addScore(player.getName(), finalScore, monstersDefeatedCount, player.getLevel());
    }

    /**
     * Individua la direzione adiacente al giocatore in cui si trova il mostro attualmente affrontato.
     *
     * @return La {@link Direction} in cui si trova il nemico.
     */
    private Direction getEncounterDirection() {
        for (Direction d : Direction.values()) {
            Position check = playerPosition.move(d);
            if (map.getEntityAt(check) == currentEncounter) return d;
        }
        return Direction.RIGHT;
    }

    /**
     * Salva lo stato corrente della partita su un file JSON.
     *
     * @param filePath Percorso del file di destinazione.
     * @throws IOException Se si verifica un errore durante la scrittura su disco.
     */
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