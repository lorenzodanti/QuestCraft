package it.unicam.cs.mpgc.rpg126280.model.map;

import java.util.Objects;

/**
 * Rappresenta una posizione immutabile (riga, colonna) sulla griglia di gioco.
 */
public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /**
     * Calcola la nuova posizione applicando uno spostamento.
     */
    public Position move(Direction direction) {
        return new Position(this.row + direction.getDeltaRow(), this.col + direction.getDeltaCol());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
