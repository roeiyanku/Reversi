/**
 * The bombDisc class represents the b
 */

public class BombDisc implements Disc {


    private Player owner;

    //constructor
    public BombDisc(Player owner) {
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
        return "💣";
    }


    private void explode(){

    }

}
