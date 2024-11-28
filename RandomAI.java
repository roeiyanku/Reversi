import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The RandomAI class represents an AI player that
 * selects a possible move based on a normal distribution and chooses a disc at random (provided it has enough discs).
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

        // Creating a list of valid disc types
        List<String> validDiscTypes = new ArrayList<>();
        validDiscTypes.add("SimpleDisc"); // Always valid
        if (number_of_bombs > 0) { // Check availability of bombs
            validDiscTypes.add("BombDisc");
        }
        if (number_of_unflippedable > 0) { // Check availability of unflippable discs
            validDiscTypes.add("UnflippableDisc");
        }

        // Randomly selecting a disc type
        Random random = new Random();
        int validDiscIndex = random.nextInt(validDiscTypes.size());
        String selectedDiscType = validDiscTypes.get(validDiscIndex);

        // Determine the current player
        Player currentPlayer = isPlayerOne() ? gameStatus.getFirstPlayer() : gameStatus.getSecondPlayer();

        // Instantiate the selected disc type with simple if-else logic
        Disc selectedDisc = null;
        if (selectedDiscType.equals("SimpleDisc")) {
            selectedDisc = new SimpleDisc(currentPlayer);
        } else if (selectedDiscType.equals("BombDisc")) {
            selectedDisc = new BombDisc(currentPlayer);
        } else if (selectedDiscType.equals("UnflippableDisc")) {
            selectedDisc = new UnflippableDisc(currentPlayer);
        }

        // Choosing a random move
        int randomIndex = random.nextInt(validPositions.size());
        Position randomPosition = validPositions.get(randomIndex);

        // Returning the random move
        return new Move(randomPosition, selectedDisc);
    }
}
