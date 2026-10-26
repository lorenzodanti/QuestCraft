package it.unicam.cs.mpgc.rpg126280.model.item;

import it.unicam.cs.mpgc.rpg126280.model.entity.Player;

/**
 * Rappresenta una pozione di cura consumabile.
 */
public class HealthPotion implements Item {
    private final String name;
    private final int healAmount;

    public HealthPotion(String name, int healAmount) {
        this.name = name;
        this.healAmount = healAmount;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "Ripristina " + healAmount + " punti vita.";
    }

    @Override
    public boolean use(Player target) {
        // Se il giocatore è già al massimo della vita o non è vivo, la pozione non viene usata
        if (!target.isAlive() || target.getHealth() >= target.getMaxHealth()) {
            return false;
        }
        target.heal(healAmount);
        return true; // Oggetto usato con successo (può essere rimosso dall'inventario)
    }
}
