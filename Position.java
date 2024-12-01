/**
 * The Position class represents a specific location on the game board
 * using row and column coordinates.
 */
public class Position {
    private int row; // The row coordinate of the position.
    private int column; // The column coordinate of the position.

    /**
     * Constructor to initialize a Position object with specified row and column.
     *
     * @param row    The row coordinate.
     * @param column The column coordinate.
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Getter for the row coordinate.
     *
     * @return The row value.
     */
    public int row() {
        return row;
    }

    /**
     * Getter for the column coordinate.
     *
     * @return The column value.
     */
    public int col() {
        return column;
    }

    /**
     * Checks if this position is equal to another position.
     *
     * @param obj The object to compare with.
     * @return True if the positions have the same row and column values, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // If both references point to the same object.
        if (obj == null || getClass() != obj.getClass()) return false; // Null or different class.
        Position position = (Position) obj;
        return row == position.row && column == position.column; // Compare row and column values.
    }

    /**
     * Returns a string representation of the position in the format "(row, column)".
     *
     * @return A string representing the position.
     */
    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }
}
