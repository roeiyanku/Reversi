import javax.swing.text.Position;

public class Move {
    private final int row;
    private final int col;
    private final Disc disc;

    public Move(int row, int col, Disc disc) {
        this.row = row;
        this.col = col;
        this.disc = disc;
    }
    //getter
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public Disc getDisc() {
        return disc;
    }

}
