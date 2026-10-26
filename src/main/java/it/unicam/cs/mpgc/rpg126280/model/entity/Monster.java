package it.unicam.cs.mpgc.rpg126280.model.entity;

/**
 * Rappresenta un nemico/mostro nel gioco.
 * Estende AbstractEntity ed eredita la logica di attacco e punti vita.
 */
public class Monster extends AbstractEntity {
    private final int expReward; // Esperienza data al giocatore quando il mostro viene sconfitto

    public Monster(String name, int maxHealth, int attackPower, int expReward) {
        super(name, maxHealth, attackPower);
        this.expReward = expReward;
    }

    /**
     * Ritorna la quantità di punti esperienza rilasciati dal mostro.
     */
    public int getExpReward() {
        return expReward;
    }
}
