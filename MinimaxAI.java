import java.util.List;

/**
 * Represents an AI player that uses the Minimax algorithm to make moves.
 * The depth of the Minimax search is fixed at 3.
 */
public class MinimaxAI extends AIPlayer {

    // Constructor that accepts whether the player is Player One
    public MinimaxAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    // Implement the abstract makeMove method from AIPlayer
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        return findBestMove(gameStatus);
    }

    // Main Minimax function with depth limit of 3
    private int minimax(PlayableLogic gameStatus, int depth, boolean maximizingPlayer) {
        // If max depth reached or game is over, return evaluation of the current state
        if (depth == 0 || gameStatus.isGameOver()) {
            return evaluate(gameStatus);
        }

        List<Move> validMoves = gameStatus.getValidMoves();
        int bestValue;

        if (maximizingPlayer) {
            bestValue = Integer.MIN_VALUE;  // Maximizing player's best score
            for (Move move : validMoves) {
                gameStatus.applyMove(move);
                int value = minimax(gameStatus, depth - 1, false); // Minimize the opponent's score
                gameStatus.undoMove(move);
                bestValue = Math.max(bestValue, value);  // Maximize the score
            }
        } else {
            bestValue = Integer.MAX_VALUE;  // Minimizing player's best score
            for (Move move : validMoves) {
                gameStatus.applyMove(move);
                int value = minimax(gameStatus, depth - 1, true); // Maximize the AI's score
                gameStatus.undoMove(move);
                bestValue = Math.min(bestValue, value);  // Minimize the score
            }
        }

        return bestValue;
    }

    // Evaluates the current game state
    private int evaluate(PlayableLogic gameStatus) {
        // Add your custom evaluation logic here, for example:
        // Return a high value if AI wins, low if the opponent wins, and 0 if it's a draw or neutral state
        if (gameStatus.isWinner(getIsPlayerOne() ? "PlayerOne" : "PlayerTwo")) {
            return 1;  // AI wins
        } else if (gameStatus.isWinner(getIsPlayerOne() ? "PlayerTwo" : "PlayerOne")) {
            return -1;  // Opponent wins
        } else {
            return 0;  // Draw or neutral state
        }
    }

    // This method returns the best move by calling minimax for each valid move and choosing the best one
    private Move findBestMove(PlayableLogic gameStatus) {
        List<Move> validMoves = gameStatus.getValidMoves();
        int bestValue = Integer.MIN_VALUE;
        Move bestMove = null;

        for (Move move : validMoves) {
            gameStatus.applyMove(move);
            int moveValue = minimax(gameStatus, 3 - 1, false); // Minimax with depth limit of 3
            gameStatus.undoMove(move);

            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }

        return bestMove;
    }
}
