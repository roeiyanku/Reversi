/**
 * The Position class represents a specific location on the game board
 * using row and column coordinates.
 */

public class Position {
    private int row;
    private int column;

    // Constructor
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }
    // Getter for row
    public int row() {
        return row;
    }

    // Getter for column
    public int col() {
        return column;
    }

    // Method to check if two positions are equal
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && column == position.column;
    }

    // Method to represent the position as a string (useful for debugging)
    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }
}
