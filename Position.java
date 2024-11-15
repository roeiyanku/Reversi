public class Position {
        private int row;
        private int column;

        // Constructor
        public Position(int row, int column) {
            this.row = row;
            this.column = column;
        }

        // Getter for row
        public int getRow() {
            return row;
        }

        // Getter for column
        public int getColumn() {
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
            return 31 * row + column;
        }

        // Method to get a neighboring position based on direction
        public Position getNeighbor(int rowOffset, int colOffset) {
            return new Position(this.row + rowOffset, this.column + colOffset);
        }

        // Method to represent the position as a string (useful for debugging)
        @Override
        public String toString() {
            return "(" + row + ", " + column + ")";
        }
    }

