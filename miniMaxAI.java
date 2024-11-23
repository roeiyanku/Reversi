import java.util.List;
    /**
    *The bombDisc class represents the b
    */
public class miniMaxAI extends AIPlayer {

    private static final int MAX_DEPTH = 3;  // Limit to 3 moves ahead

    public miniMaxAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        List<Position> validPositions = gameStatus.ValidMoves();

        if (validPositions == null || validPositions.isEmpty()) {
            return null;  // No valid moves, return null
        }

        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;  // Start with a very low value

        // Try all valid moves to find the best one
        for (Position position : validPositions) {
            Move move = new Move(position);
            int moveValue = minValue(gameStatus.result(move), 1); // Start with depth 1

            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;  // Update the best move found so far
            }
        }

        return bestMove;  // Return the best move found
    }

    // Min value: Try to minimize the score (for opponent)
    private int minValue(PlayableLogic gameStatus, int depth) {
        if (gameStatus.terminal() || depth >= MAX_DEPTH) {
            return gameStatus.utility();  // Return the utility if game ends or depth limit is reached
        }

        int minValue = Integer.MAX_VALUE;  // Start with a very high value
        List<Position> possibleMoves = gameStatus.ValidMoves();

        // Try all possible moves
        for (Move move : possibleMoves) {
            int currentValue = maxValue(gameStatus.result(move), depth + 1); // Go to the next depth
            minValue = Math.min(minValue, currentValue);  // Minimize the value
        }

        return minValue;
    }

    // Max value: Try to maximize the score (for the AI)
    private int maxValue(PlayableLogic gameStatus, int depth) {
        if (gameStatus.terminal() || depth >= MAX_DEPTH) {
            return gameStatus.utility();  // Return the utility if game ends or depth limit is reached
        }

        int maxValue = Integer.MIN_VALUE;  // Start with a very low value
        List<Move> possibleMoves = gameStatus.getValidMoves();

        // Try all possible moves
        for (Move move : possibleMoves) {
            int currentValue = minValue(gameStatus.result(move), depth + 1); // Go to the next depth
            maxValue = Math.max(maxValue, currentValue);  // Maximize the value
        }

        return maxValue;
    }
}
