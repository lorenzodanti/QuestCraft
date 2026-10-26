package it.unicam.cs.mpgc.rpg126280.model.entity;

public abstract class AbstractEntity implements Entity, Attacker {
    private final String name;
    private int health;
    private final int maxHealth;
    private final int attackPower;

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

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void takeDamage(int amount) {
        if (amount < 0) return;
        this.health = Math.max(0, this.health - amount);
    }

    @Override
    public void heal(int amount) {
        if (amount < 0 || !isAlive()) return;
        this.health = Math.min(maxHealth, this.health + amount);
    }

    @Override
    public void attack(Entity target) {
        if (this.isAlive() && target.isAlive()) {
            target.takeDamage(this.attackPower);
        }
    }
}
