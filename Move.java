import java.util.List;
import java.util.ArrayList;

/**
 * The Move class represents a move in the game.
 */
public class Move {

    private Disc disc;
    private Player player;
    private Position position;
    public List<Move> flippedDiscs;

    public Move(Position position, Disc disc) {
        this.position = position;
        this.disc = disc;
    }



    public Move(Position position, Disc disc, Player player, List<Move> flippedDiscs) {
        this.position = position;
        this.disc = disc;
        this.player = player;
        //If flipped list is null then return empty list. This is mostly for undoLastMove
        this.flippedDiscs = (flippedDiscs != null) ? flippedDiscs : new ArrayList<>();

    }

    public Position position() {
        return this.position;
    }
    public Disc disc() {
        return this.disc;
    }


    /**
     * Getter for the disc.
     *
     * @return The disc associated with the move.
     */
    public Disc getDisc() {
        return disc;
    }
    /**
     * Getter for the list of flipped discs.
     *
     * @return A list of discs flipped as a result of this move.
     */
    public List<Move> getFlippedDisc() {
        return flippedDiscs;
    }

    public Player getPlayer() {
        return player;
    }

    public Position getPosition() {
        return position;
    }



