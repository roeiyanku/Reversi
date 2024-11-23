import java.util.List;
import java.util.Random;

/**
 * The bombDisc class represents the b
 */

public class RandomAI extends AIPlayer {
    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        // Getting all valid positions in a list
        List<Position> validPositions = gameStatus.ValidMoves();
        if (validPositions == null || validPositions.isEmpty()) {
            return null; // if no legal moves, return null
        }

        Disc currentDisc = isPlayerOne() ? new SimpleDisc(gameStatus.getFirstPlayer()) : new SimpleDisc(gameStatus.getSecondPlayer());

        // choosing a random move
        Random random = new Random();
        int randomIndex = random.nextInt(validPositions.size());
        Position randomPosition = validPositions.get(randomIndex);


        return new Move(randomPosition, currentDisc);
    }
}