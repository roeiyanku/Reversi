import javax.swing.text.Position;
import java.util.List;

public class GreedyAI extends AIPlayer {
    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        // Get list of valid positions
        List<Position> validPositions = gameStatus.ValidMoves();
        if (validPositions == null || validPositions.isEmpty()) {
            return null; // No valid moves available
        }

        Move greedyMove = null;
        int mostFlips = -1;


        for (Position position : validPositions) {
            // Convert position to a move because validMoves in playable logic is a list of positions
            Move move = new Move(position);


            int flips = gameStatus.countFlips(move.getPosition());

            // If this move results in more flips, update the best move
            if (flips > mostFlips) {
                mostFlips = flips;
                greedyMove = move;
            }
        }

        return greedyMove;
    }
}
