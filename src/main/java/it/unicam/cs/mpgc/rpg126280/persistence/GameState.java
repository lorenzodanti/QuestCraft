package it.unicam.cs.mpgc.rpg126280.persistence;

/**
 * Data Transfer Object (DTO) che rappresenta lo stato salvabile del gioco.
 * Viene convertito direttamente in JSON tramite Gson.
 */
public class GameState {
    private String playerName;
    private int playerHealth;
    private int playerMaxHealth;
    private int playerAttackPower;
    private int playerLevel;
    private int playerExperience;
    private int playerRow;
    private int playerCol;

    // Costruttore vuoto richiesto da Gson per la deserializzazione
    public GameState() {}

    public GameState(String playerName, int playerHealth, int playerMaxHealth,
                     int playerAttackPower, int playerLevel, int playerExperience,
                     int playerRow, int playerCol) {
        this.playerName = playerName;
        this.playerHealth = playerHealth;
        this.playerMaxHealth = playerMaxHealth;
        this.playerAttackPower = playerAttackPower;
        this.playerLevel = playerLevel;
        this.playerExperience = playerExperience;
        this.playerRow = playerRow;
        this.playerCol = playerCol;
    }

    // Getter e Setter per la serializzazione/deserializzazione
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getPlayerHealth() { return playerHealth; }
    public void setPlayerHealth(int playerHealth) { this.playerHealth = playerHealth; }

    public int getPlayerMaxHealth() { return playerMaxHealth; }
    public void setPlayerMaxHealth(int playerMaxHealth) { this.playerMaxHealth = playerMaxHealth; }

    public int getPlayerAttackPower() { return playerAttackPower; }
    public void setPlayerAttackPower(int playerAttackPower) { this.playerAttackPower = playerAttackPower; }

    public int getPlayerLevel() { return playerLevel; }
    public void setPlayerLevel(int playerLevel) { this.playerLevel = playerLevel; }

    public int getPlayerExperience() { return playerExperience; }
    public void setPlayerExperience(int playerExperience) { this.playerExperience = playerExperience; }

    public int getPlayerRow() { return playerRow; }
    public void setPlayerRow(int playerRow) { this.playerRow = playerRow; }

    public int getPlayerCol() { return playerCol; }
    public void setPlayerCol(int playerCol) { this.playerCol = playerCol; }
}
