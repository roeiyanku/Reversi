import java.util.List;
import java.util.Random;

public class RandomAI extends AIPlayer {
    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        // קבלת רשימת עמדות חוקיות
        List<Position> validPositions = gameStatus.ValidMoves();
        if (validPositions == null || validPositions.isEmpty()) {
            return null; // אין מהלכים חוקיים
        }

        // יצירת דיסק שמייצג את השחקן הנוכחי
        Disc currentDisc = isPlayerOne() ? new SimpleDisc(gameStatus.getFirstPlayer()) : new SimpleDisc(gameStatus.getSecondPlayer());

        // בחירת מהלך אקראי מתוך הרשימה
        Random random = new Random();
        int randomIndex = random.nextInt(validPositions.size());
        Position randomPosition = validPositions.get(randomIndex);

        // יצירת המהלך האקראי
        return new Move(randomPosition, currentDisc);
    }
}