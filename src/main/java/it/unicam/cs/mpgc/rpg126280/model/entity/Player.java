package it.unicam.cs.mpgc.rpg126280.model.entity;

public class Player extends AbstractEntity {
    private int level;
    private int experience;

    public Player(String name, int maxHealth, int attackPower) {
        super(name, maxHealth, attackPower);
        this.level = 1;
        this.experience = 0;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public void addExperience(int exp) {
        if (exp <= 0) return;
        this.experience += exp;
        // Semplice meccanica di level up
        if (this.experience >= level * 100) {
            level++;
        }
    }
}
