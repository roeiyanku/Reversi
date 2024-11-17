import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class GameLogic implements PlayableLogic {

    private Player player1;
    private Player player2;
    private Boolean isPlayer1Turn;
    private Disc[][] board;
    private static final int BOARD_SIZE = 8;
    private Stack<Move> moveHistory = new Stack<>();



    public GameLogic() {
        // Initialize the board
        board = new Disc[BOARD_SIZE][BOARD_SIZE];
        initial_Board();
    }

    private void initial_Board() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
        board[3][3] = new SimpleDisc(player1);
        board[4][4] = new SimpleDisc(player1);
        board[3][4] = new SimpleDisc(player2);
        board[4][3] = new SimpleDisc(player2);

    }


    @Override
    public boolean locate_disc(Position position, Disc disc) {
        int offset = position.getOffset();
        int row = offset / BOARD_SIZE;
        int column = offset % BOARD_SIZE;

        // בדוק אם התא ריק ובתוך גבולות
        if (row < 0 || row >= BOARD_SIZE || column < 0 || column >= BOARD_SIZE || board[row][column] != null) {
            return false;
        }

        // בדוק אם יש כיוונים חוקיים
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int[] dir : directions) {
            if (isValidDirection(row, column, dir[0], dir[1], disc)) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidDirection(int row, int col, int dRow, int dCol, Disc disc) {
        boolean foundOpponent = false;
        int currentRow = row + dRow;
        int currentCol = col + dCol;

        while (currentRow >= 0 && currentRow < BOARD_SIZE && currentCol >= 0 && currentCol < BOARD_SIZE) {
            Disc currentDisc = board[currentRow][currentCol];

            if (currentDisc == null) {
                return false;
            }

            if (!currentDisc.getOwner().equals(disc.getOwner())) {
                foundOpponent = true;
            } else {
                return foundOpponent;
            }

            currentRow += dRow;
            currentCol += dCol;
        }

        return false;
    }

    @Override
    public Disc getDiscAtPosition(Position position) {
        int offset = position.getOffset();
        int row = offset / BOARD_SIZE;
        int column = offset % BOARD_SIZE;

        // בדיקת חריגה
        if (offset < 0 || offset >= BOARD_SIZE * BOARD_SIZE) {
            throw new IllegalArgumentException("Position is out of bounds: offset=" + offset);
        }

        return board[row][column];
    }




    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    // ?

    @Override
    public List<Position> ValidMoves() {
        List<Position> validMoveList = new ArrayList<>();

        // קבע מי השחקן הנוכחי
        Disc currentDisc = isPlayer1Turn ? new SimpleDisc(player1) : new SimpleDisc(player2);

        // עבור על כל התאים בלוח
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                // צור מופע Position באמצעות offset
                Position position = createPositionFromRowAndCol(row, col);

                // בדוק אם המשבצת ריקה והמהלך חוקי
                if (board[row][col] == null && locate_disc(position, currentDisc)) {
                    validMoveList.add(position);
                }
            }
        }

        return validMoveList;
    }



    @Override
    public int countFlips(Position pos) {
        int totalFlips = 0;

        // קבע את המיקום
        int row = pos.getOffset() / BOARD_SIZE;
        int col = pos.getOffset() % BOARD_SIZE;

        // ודא שהמיקום ריק ובטווח
        if (board[row][col] != null || row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return 0;
        }

        // קבע מי השחקן הנוכחי
        Disc currentDisc = isPlayer1Turn ? new SimpleDisc(player1) : new SimpleDisc(player2);

        // בדוק את כל 8 הכיוונים
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int[] dir : directions) {
            totalFlips += countFlipsInDirection(row, col, dir[0], dir[1], currentDisc);
        }

        return totalFlips;
    }

    @Override
    public Player getFirstPlayer() {
        return player1;
    }

    @Override
    public Player getSecondPlayer() {
        return player2;
    }

    @Override
    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.isPlayer1Turn = true;

    }

    @Override
    public boolean isFirstPlayerTurn() {
        return isPlayer1Turn;
    }

    @Override
    public boolean isGameFinished() {
        // בדיקה אם הלוח מלא
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == null) { // אם נמצא תא ריק, המשחק לא הסתיים
                    return false;
                }
            }
        }

        // בדיקה אם יש מהלכים חוקיים לשני השחקנים
        boolean originalTurn = isPlayer1Turn; // שמור את התור הנוכחי

        isPlayer1Turn = true;
        boolean player1HasMoves = !ValidMoves().isEmpty();

        isPlayer1Turn = false;
        boolean player2HasMoves = !ValidMoves().isEmpty();

        isPlayer1Turn = originalTurn; // החזר את התור למצב המקורי

        return !(player1HasMoves || player2HasMoves); // המשחק נגמר אם אין מהלכים לאף אחד
    }

    @Override
    public void reset() {

        // אתחול הלוח
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = null;
            }
        }

        // הנחת דיסקים התחלתיים במרכז הלוח
        board[3][3] = new SimpleDisc(player1);
        board[3][4] = new SimpleDisc(player2);
        board[4][3] = new SimpleDisc(player2);
        board[4][4] = new SimpleDisc(player1);

        // איפוס תור השחקנים
        isPlayer1Turn = true;

        // איפוס משאבים מיוחדים לשחקנים
        player1.reset_bombs_and_unflippedable();
        player2.reset_bombs_and_unflippedable();
    }



    @Override
    public void undoLastMove() {
        // בדוק אם יש מהלכים להחזיר
        if (moveHistory.isEmpty()) {
            throw new IllegalStateException("No moves to undo.");
        }

        // שלוף את המהלך האחרון מהמחסנית
        Move lastMove = moveHistory.pop();

        // שחזר את המיקום שבו הונח הדיסק
        Position position = lastMove.getPosition();
        int row = position.getOffset() / BOARD_SIZE;
        int col = position.getOffset() % BOARD_SIZE;
        board[row][col] = null; // הסר את הדיסק מהמיקום

        // שחזר את הבעלים המקוריים של הדיסקים שהתהפכו
        List<Position> flippedDiscs = lastMove.getFlippedDisc();
        if (flippedDiscs != null) {
            for (Position flippedPos : flippedDiscs) {
                int flippedRow = flippedPos.getOffset() / BOARD_SIZE;
                int flippedCol = flippedPos.getOffset() % BOARD_SIZE;
                Disc flippedDisc = board[flippedRow][flippedCol];
                flippedDisc.setOwner(lastMove.getPlayer());
            }
        }

        // עדכן את תור השחקנים לפי המהלך האחרון
        isPlayer1Turn = lastMove.getPlayer().equals(player2);
    }

    Position createPositionFromRowAndCol(int row, int col) {
        int offset = row * BOARD_SIZE + col;
        return createPositionFromOffset(offset);
    }

    // מתודה עזר ליצירת Position מ-offset
    private Position createPositionFromOffset(int offset) {
        return new Position() {
            @Override
            public int getOffset() {
                return offset;
            }
        };
    }
    // לוגיקה לדיסק פצצה
    private int handleBombDisc(int row, int col) {
        int flipped = 0;

        // עובר על כל הסביבה של הדיסק הפצצה
        int[][] bombDirections = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int[] dir : bombDirections) {
            int r = row + dir[0];
            int c = col + dir[1];

            if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] != null) {
                flipped++; // ספר את כל הדיסקים המושפעים
            }
        }

        return flipped;
    }
    // פונקציה לספירת דיסקים שיתהפכו בכיוון מסוים
    private int countFlipsInDirection(int row, int col, int dRow, int dCol, Disc disc) {
        int flips = 0;
        int currentRow = row + dRow;
        int currentCol = col + dCol;

        while (currentRow >= 0 && currentRow < BOARD_SIZE && currentCol >= 0 && currentCol < BOARD_SIZE) {
            Disc currentDisc = board[currentRow][currentCol];

            if (currentDisc == null) {
                return 0; // רצף לא חוקי
            }

            // טיפול ב-UnflippableDisc
            if (currentDisc instanceof UnflippableDisc) {
                return 0; // דיסק שלא ניתן להפוך עוצר את הרצף
            }

            // טיפול ב-BombDisc
            if (currentDisc instanceof BombDisc) {
                flips += handleBombDisc(currentRow, currentCol); // ספירה ייחודית לדיסק פצצה
                return flips;
            }

            // ספירה רגילה לדיסקים רגילים
            if (!currentDisc.getOwner().equals(disc.getOwner())) {
                flips++;
            } else {
                return flips; // דיסק של השחקן סוגר את הרצף
            }

            currentRow += dRow;
            currentCol += dCol;
        }

        return 0; // אם לא נסגר רצף
    }
}
