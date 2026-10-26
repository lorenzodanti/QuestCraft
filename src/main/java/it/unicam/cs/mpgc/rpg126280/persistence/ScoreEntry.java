package it.unicam.cs.mpgc.rpg126280.persistence;

/**
 * Rappresenta una voce dettagliata della classifica.
 */
public class ScoreEntry {
    private String playerName;
    private int score;
    private int monstersDefeated;
    private int levelReached;

    public ScoreEntry() {}

    public ScoreEntry(String playerName, int score, int monstersDefeated, int levelReached) {
        this.playerName = playerName;
        this.score = score;
        this.monstersDefeated = monstersDefeated;
        this.levelReached = levelReached;
    }

    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public int getMonstersDefeated() { return monstersDefeated; }
    public int getLevelReached() { return levelReached; }
}
