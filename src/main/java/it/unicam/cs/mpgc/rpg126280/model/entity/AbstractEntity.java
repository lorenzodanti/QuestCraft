package it.unicam.cs.mpgc.rpg126280.model.entity;

/**
 * Classe base astratta che implementa i comportamenti comuni a tutte le entità viventi
 * del gioco (giocatore e mostri), come gestione della salute, danni, cure e attacchi.
 */
public abstract class AbstractEntity implements Entity, Attacker {
    private final String name;
    private int health;
    private final int maxHealth;
    private final int attackPower;

    /**
     * Costruisce una nuova entità inizializzando i suoi punti vita al valore massimo.
     *
     * @param name        Nome dell'entità.
     * @param maxHealth   Punti vita massimi dell'entità.
     * @param attackPower Potere di attacco base dell'entità.
     */
    public AbstractEntity(String name, int maxHealth, int attackPower) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attackPower = attackPower;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public int getAttackPower() {
        return attackPower;
    }

    /**
     * Verifica se l'entità è ancora in vita.
     *
     * @return {@code true} se i punti vita attuali sono maggiori di zero, {@code false} altrimenti.
     */
    @Override
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Riduce i punti vita dell'entità in base al danno subito,
     * garantendo che la salute non scenda mai sotto lo zero.
     *
     * @param amount Quantità di danno da sottrarre (valori negativi vengono ignorati).
     */
    @Override
    public void takeDamage(int amount) {
        if (amount < 0) return;
        this.health = Math.max(0, this.health - amount);
    }

    /**
     * Ripristina i punti vita dell'entità fino al limite massimo consentito,
     * a condizione che l'entità sia ancora in vita.
     *
     * @param amount Quantità di punti vita da recuperare (valori negativi vengono ignorati).
     */
    @Override
    public void heal(int amount) {
        if (amount < 0 || !isAlive()) return;
        this.health = Math.min(maxHealth, this.health + amount);
    }

    /**
     * Esegue un attacco contro un'entità bersaglio infliggendo un danno pari al proprio {@code attackPower},
     * solo se sia l'attaccante che il bersaglio sono in vita.
     *
     * @param target Entità bersaglio dell'attacco.
     */
    @Override
    public void attack(Entity target) {
        if (this.isAlive() && target.isAlive()) {
            target.takeDamage(this.attackPower);
        }
    }
}