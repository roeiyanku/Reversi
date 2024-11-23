import java.util.List;

/**
 * The bombDisc class represents the b
 */

public class GreedyAI extends AIPlayer {
    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        // Get a list of valid moves
        List<Position> validPositions = gameStatus.ValidMoves();
        if (validPositions == null || validPositions.isEmpty()) {
            return null;
        }

        Move greedyMove = null;
        int mostFlips = -1;

        // Create a disc representing the current player
        Disc currentDisc = isPlayerOne() ? new SimpleDisc(gameStatus.getFirstPlayer()) : new SimpleDisc(gameStatus.getSecondPlayer());

        for (Position position : validPositions) {
            // Calculate the number of flips for the current position
            int flips = gameStatus.countFlips(position);

            // Update the optimal move if the number of flips is higher
            if (flips > mostFlips) {
                mostFlips = flips;
                greedyMove = new Move(position, currentDisc);
            }
        }

        return greedyMove;
    }
}
