import javax.crypto.BadPaddingException;
import java.util.List;
import java.util.ArrayList;

public class Position {
        private static int row;
        private static int column;

        // Constructor
        public Position(int row, int column) {
            this.row = row;
            this.column = column;
        }

        // Getter for row
        public static int getRow() {
            return row;
        }

        // Getter for column
        public static int getColumn() {
            return column;
        }

        // Method to check if two positions are equal
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Position position = (Position) obj; //we'll see about this
            return row == position.row && column == position.column;
        }

        // Method to get a hash code for the position (useful for HashMaps or HashSets)
        @Override
        public int hashCode() {
            return 27 * row + column;
        }

        // Method to get a neighboring position based on direction
        public Position getNeighbor(int rowOffset, int colOffset) {
            return new Position(this.row + rowOffset, this.column + colOffset);
        }

        /**
        * gives a list of all adjacent positions.
         *
         * @param row of row of the position
         * @param col of column  of the position
         * @return a list of all the adjacent positions to the current position
         */

        public List<Position> getAdjacentPositions(int row, int col, int BoardSize){
            List<Position> adjacentPositions = new ArrayList<>();
            for (int r = row - 1; r <= row + 1; r++) {
                for (int c = col - 1; c <= col + 1; c++){
                    //ignore the middle position as well as if out of bounds
                    if(!(r == row && c == col) ||  r >= 0 || r <BoardSize || c >= 0 || c < BoardSize){
                        adjacentPositions.add(new Position(r,c));
                    }
                }
            }
            return adjacentPositions;
        }

        // Method to represent the position as a string (useful for debugging)
        @Override
        public String toString() {
            return "(" + row + ", " + column + ")";
        }
    }

