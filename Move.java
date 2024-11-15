import javax.swing.text.Position;
import java.awt.*;

public class Move {

    private Disc disc;
    private Player player;
    private Position position;
    private List flippedDisc;


    public Move(Position position, Disc disc, Player player, List flippedDisc) {
        this.position = position;
        this.disc = disc;
        this.player = player;
        this.flippedDisc = flippedDisc;
        //matrix[position.getRow][position.getColumn] = disc
    }

    public Disc getDisc() {
        return disc;
    }

    public List getFlippedDisc() {
        return flippedDisc;
    }

    public Player getPlayer() {
        return player;
    }

    public Position getPosition() {
         return position;
    }

    @Override
    public String toString() {



}