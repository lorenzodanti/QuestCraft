package it.unicam.cs.mpgc.rpg126280.model.entity;

/**
 * Interfaccia base per tutte le entità del gioco (Personaggio, Mostri, NPC).
 */
public interface Entity {
    String getName();
    int getHealth();
    int getMaxHealth();
    int getAttackPower();
    boolean isAlive();

    void takeDamage(int amount);
    void heal(int amount);
}
