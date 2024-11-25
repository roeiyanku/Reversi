/**
 * The HumanPlayer class represents a human player in the game.
 * It inherits from the Player class.
*/

 public class HumanPlayer extends Player{
    public HumanPlayer(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    boolean isHuman() {
        return true;
    }
}
