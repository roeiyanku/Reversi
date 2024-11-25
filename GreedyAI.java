import java.util.List;
import java.util.Comparator;

/**
 * The GreedyAI class extends the AI player.
 * The Greedy player chooses the move that flips the most opponent discs (always selects a regular disc).
 * In cases where there are multiple such moves, it selects the rightmost square.
 * If there are still ties, it selects the bottommost square (using a comparator).
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

        //creates a comparator to sort all positions, and if there's a tie get the most right and then the most Bottom
        Comparator<Position> positionComparator = Comparator
                .comparingInt(Position::col) // Rightmost
                .thenComparingInt(Position::row); //BottomMost

        for (Position position : validPositions) {
            // Calculate the number of flips for the current position
            int flips = gameStatus.countFlips(position);

            // Update the optimal move if the number of flips is higher
            if (flips > mostFlips || (flips == mostFlips && positionComparator.compare(position, greedyMove.getPosition()) > 0)) {
                mostFlips = flips;
                greedyMove = new Move(position, currentDisc);
            }
        }

        return greedyMove;
    }



}
