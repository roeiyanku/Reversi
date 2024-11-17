import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAI extends AIPlayer {
    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {

        List<Move> validMoves = new ArrayList<>();

        // Get list of valid positions
        List<Position> validPositions = gameStatus.ValidMoves();
        if (validPositions == null || validPositions.isEmpty()) {
            return null; // No valid moves available
        }

        for (Position position : validPositions) {
                // Convert position to a move because validMoves in playable logic is a list of positions
                Move move = new Move(position);
                validMoves.add(move);
        }
        Random random = new Random();
        int randomIndex = random.nextInt(validMoves.size());
        Move randomMove = validMoves.get(randomIndex);
        return randomMove;

    }
}
