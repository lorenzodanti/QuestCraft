package it.unicam.cs.mpgc.rpg126280.model.entity;

/**
 * Rappresenta l'eroe controllato dal giocatore.
 * Estende {@link AbstractEntity} aggiungendo la gestione del livello e dei punti esperienza.
 */
public class Player extends AbstractEntity {
    private int level;
    private int experience;

    /**
     * Crea un nuovo giocatore impostando il livello iniziale a 1 e l'esperienza a 0.
     *
     * @param name        Nome dell'eroe.
     * @param maxHealth   Punti vita massimi iniziali.
     * @param attackPower Potere di attacco base del giocatore.
     */
    public Player(String name, int maxHealth, int attackPower) {
        super(name, maxHealth, attackPower);
        this.level = 1;
        this.experience = 0;
    }

    /**
     * Restituisce il livello attuale del giocatore.
     *
     * @return Livello corrente.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Restituisce i punti esperienza accumulati dal giocatore.
     *
     * @return Punti esperienza totali.
     */
    public int getExperience() {
        return experience;
    }

    /**
     * Aggiunge punti esperienza al giocatore e gestisce il passaggio di livello
     * al raggiungimento della soglia richiesta ({@code level * 100}).
     *
     * @param exp Quantità di esperienza guadagnata (valori minori o uguali a zero vengono ignorati).
     */
    public void addExperience(int exp) {
        if (exp <= 0) return;
        this.experience += exp;
        // Aumenta il livello ogni volta che l'esperienza raggiunge la soglia proporzionale al livello attuale
        if (this.experience >= level * 100) {
            level++;
        }
    }
}