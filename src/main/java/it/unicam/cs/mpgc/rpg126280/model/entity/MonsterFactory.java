package it.unicam.cs.mpgc.rpg126280.model.entity;

/**
 * Factory per la creazione centralizzata di mostri.
 * Applica il pattern Factory Method per isolare la logica di creazione delle entità.
 */
public class MonsterFactory {

    /**
     * Crea un Goblin di livello base.
     */
    public static Monster createGoblin() {
        return new Monster("Goblin", 30, 8, 25);
    }

    /**
     * Crea un Orco più resistente e forte.
     */
    public static Monster createOrc() {
        return new Monster("Orco", 60, 15, 60);
    }

    /**
     * Crea un Drago (Boss).
     */
    public static Monster createDragon() {
        return new Monster("Drago", 150, 25, 200);
    }
}
