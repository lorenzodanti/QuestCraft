package it.unicam.cs.mpgc.rpg126280.model.item;

import it.unicam.cs.mpgc.rpg126280.model.entity.Player;

/**
 * Interfaccia base per tutti gli oggetti dell'inventario.
 */
public interface Item {
    String getName();
    String getDescription();

    /**
     * Applica l'effetto dell'oggetto sul giocatore (es. cura, aumento attacco).
     * @param target Il giocatore che usa l'oggetto.
     * @return true se l'oggetto è stato usato con successo (es. consumato), false altrimenti.
     */
    boolean use(Player target);
}
