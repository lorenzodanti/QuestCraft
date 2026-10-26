package it.unicam.cs.mpgc.rpg126280.model.map;

import it.unicam.cs.mpgc.rpg126280.model.entity.Entity;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestisce la griglia di gioco, la posizione delle entità e i limiti del mondo.
 */
public class GameMap {
    private final int rows;
    private final int cols;
    private final Map<Position, Entity> entitiesOnMap;

    public GameMap(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.entitiesOnMap = new HashMap<>();
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    /**
     * Verifica se una posizione si trova all'interno dei confini della mappa.
     */
    public boolean isWithinBounds(Position position) {
        return position.getRow() >= 0 && position.getRow() < rows &&
                position.getCol() >= 0 && position.getCol() < cols;
    }

    /**
     * Posiziona un'entità sulla mappa se la posizione è valida.
     */
    public boolean placeEntity(Entity entity, Position position) {
        if (!isWithinBounds(position)) {
            return false;
        }
        entitiesOnMap.put(position, entity);
        return true;
    }

    /**
     * Restituisce l'entità presente in una determinata posizione (o null se vuota).
     */
    public Entity getEntityAt(Position position) {
        return entitiesOnMap.get(position);
    }

    /**
     * Rimuove un'entità da una determinata posizione.
     */
    public void removeEntityAt(Position position) {
        entitiesOnMap.remove(position);
    }
}
