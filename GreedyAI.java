import java.util.List;

public class GreedyAI extends AIPlayer {
    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        // קבלת רשימת מהלכים חוקיים
        List<Position> validPositions = gameStatus.ValidMoves();
        if (validPositions == null || validPositions.isEmpty()) {
            return null; // אין מהלכים חוקיים
        }

        Move greedyMove = null;
        int mostFlips = -1;

        // יצירת דיסק שמייצג את השחקן הנוכחי
        Disc currentDisc = isPlayerOne() ? new SimpleDisc(gameStatus.getFirstPlayer()) : new SimpleDisc(gameStatus.getSecondPlayer());

        for (Position position : validPositions) {
            // חישוב מספר ההפיכות עבור המיקום הנוכחי
            int flips = gameStatus.countFlips(position);

            // עדכון המהלך המיטבי אם מספר ההפיכות גבוה יותר
            if (flips > mostFlips) {
                mostFlips = flips;
                greedyMove = new Move(position, currentDisc); // יצירת מהלך חדש עם המיקום והדיסק
            }
        }

        return greedyMove; // החזרת המהלך המיטבי
    }
}