package it.unicam.cs.mpgc.rpg126280.model.item;

import it.unicam.cs.mpgc.rpg126280.model.entity.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gestisce la collezione di oggetti del giocatore.
 */
public class Inventory {
    private final List<Item> items;
    private final int capacity;

    public Inventory(int capacity) {
        this.capacity = capacity;
        this.items = new ArrayList<>();
    }

    public boolean addItem(Item item) {
        if (items.size() >= capacity) {
            return false; // Inventario pieno
        }
        return items.add(item);
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    /**
     * Tenta di utilizzare un oggetto presente nell'inventario.
     */
    public boolean useItem(Item item, Player player) {
        if (items.contains(item) && item.use(player)) {
            items.remove(item); // Rimuove l'oggetto se è consumabile ed è stato usato
            return true;
        }
        return false;
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items); // Ritorna una lista non modificabile dall'esterno per incapsulamento
    }

    public int getCapacity() {
        return capacity;
    }
}
