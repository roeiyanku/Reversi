/**
 * The SimpleDisc class represents a standard disc used in the game.
 */

public class SimpleDisc implements Disc {

    private Player owner;

    //constructor
    public SimpleDisc(Player owner) {
        this.owner = owner;
    }

    @Override
    public Player getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(Player player) {
        this.owner = player;
    }

    @Override
    public String getType() {
        return "⬤";
    }
}