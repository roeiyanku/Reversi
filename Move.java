import java.util.List;
/**
 * The bombDisc class represents the b
 */
public class Move {

    private Disc disc;
    private Player player;
    private Position position;
    private List flippedDiscs;
    ;


    //base constructor
    public Move(Position position) {
        this.position = position;
    }

    public Move(Position position, Disc disc, Player player, List flippedDiscs) {
        this.position = position;
        this.disc = disc;
        this.player = player;
        this.flippedDiscs = flippedDiscs;
    }
    public Move(Position position, Disc disc) {
        this.position = position;
        this.disc = disc;


    }
    public Position position() {
        return this.position;
    }
    public Disc disc() {
        return this.disc;
    }

    public Disc getDisc() {
        return disc;
    }

    public List<Position> getFlippedDisc() {
        return flippedDiscs;
    }

    public Player getPlayer() {
        return player;
    }

    public Position getPosition() {
        return position;
    }


}
