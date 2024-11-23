import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class GameLogic implements PlayableLogic{

    private Player player1;
    private Player player2;
    private boolean isPlayer1Turn;
    private Disc[][] board;
    private static final int BOARD_SIZE = 8;
    private Stack<Move> moveHistory = new Stack<>();
    private final int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // אופקי ואנכי
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // אלכסונים
    };

    public GameLogic(){
        board = new Disc[BOARD_SIZE][BOARD_SIZE];
    }
    private void initializeBoard() {
        System.out.println("Initializing board...");
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = null;
            }
        }

        if (player1 == null || player2 == null) {
            return;
        }

        board[3][3] = new SimpleDisc(player1);
        board[4][4] = new SimpleDisc(player1);
        board[3][4] = new SimpleDisc(player2);
        board[4][3] = new SimpleDisc(player2);


        isPlayer1Turn = true;
    }


    @Override
    public boolean locate_disc(Position position, Disc disc) {
        int row = position.row();
        int col = position.col();

        // בדיקה אם המיקום מחוץ לגבולות או תפוס
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE || board[row][col] != null) {
            return false;
        }

        boolean validMove = false;
        List<Position> discsToFlip = new ArrayList<>();

        // בדיקת כל הכיוונים
        for (int[] direction : directions) {
            List<Position> potentialFlips = getFlippableDiscsInDirection(position, direction[0], direction[1], disc);

            if (!potentialFlips.isEmpty()) {
                validMove = true;
                discsToFlip.addAll(potentialFlips);
            }
        }

        // אם אין כיוונים חוקיים, המהלך אינו חוקי
        if (!validMove) {
            return false;
        }

        // זיהוי סוג הדיסק
        System.out.println("Player " + (disc.getOwner().equals(player1) ? "1" : "2") +
                " placed a " + disc.getType() + " at (" + row + ", " + col + ")");

        // ביצוע המהלך - הנחת הדיסק
        board[row][col] = disc;

        // הפיכת דיסקים
        for (Position flipPosition : discsToFlip) {
            int flipRow = flipPosition.row();
            int flipCol = flipPosition.col();
            board[flipRow][flipCol] = new SimpleDisc(disc.getOwner());

            System.out.println("Player " + (disc.getOwner().equals(player1) ? "1" : "2") +
                    " flipped a disc at (" + flipRow + ", " + flipCol + ")");
        }

        // שמירת המהלך להיסטוריה
        moveHistory.push(new Move(position, disc));

        // מעבר תור
        isPlayer1Turn = !isPlayer1Turn;

        return true;
    }



    @Override
    public Disc getDiscAtPosition(Position position) {

        int row = position.row();
        int col = position.col();

        // בדיקת גבולות נכונה
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return null;
        }
        return board[row][col];
    }


    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    @Override
    public List<Position> ValidMoves() {
        List<Position> validMoves = new ArrayList<>();

        // בדיקה מי השחקן הנוכחי
        Disc currentDisc = isPlayer1Turn ? new SimpleDisc(player1) : new SimpleDisc(player2);

        // מעבר על כל משבצת בלוח
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Position position = new Position(row, col);

                // אם המיקום פנוי ויש לפחות כיוון חוקי אחד
                if (board[row][col] == null && hasValidDirection(position, currentDisc)) {
                    validMoves.add(position);
                }
            }
        }

        return validMoves;
    }




    @Override
    public int countFlips(Position a) {
        int totalFlips = 0;

        int row = a.row();
        int col = a.col();

        // אם המיקום מחוץ לגבולות או תפוס, אין מה לחשב
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE || board[row][col] != null) {
            return 0;
        }

        // קבע מי השחקן הנוכחי
        Disc currentDisc = isPlayer1Turn ? new SimpleDisc(player1) : new SimpleDisc(player2);

        // בדוק את כל 8 הכיוונים
        for (int[] direction : directions) {
            int flips = 0;
            int currentRow = row + direction[0];
            int currentCol = col + direction[1];
            boolean foundOpponent = false;

            // התקדמות בכיוון
            while (currentRow >= 0 && currentRow < BOARD_SIZE &&
                    currentCol >= 0 && currentCol < BOARD_SIZE) {
                Disc currentDiscInDirection = board[currentRow][currentCol];

                // אם המיקום ריק, אין מה להפוך
                if (currentDiscInDirection == null) {
                    flips = 0;
                    break;
                }

                // טיפול בדיסקים מסוגים שונים
                if (currentDiscInDirection instanceof UnflippableDisc) {
                    flips = 0; // דיסק שאי אפשר להפוך עוצר את הרצף
                    break;
                }

                if (currentDiscInDirection instanceof BombDisc) {
                    flips += handleBombDisc(new Position(currentRow, currentCol)); // ספירת דיסקים מושפעים מדיסק פצצה
                    break; // דיסק פצצה עוצר את הבדיקה בכיוון זה
                }

                // ספירה רגילה לדיסקים רגילים
                if (!currentDiscInDirection.getOwner().equals(currentDisc.getOwner())) {
                    foundOpponent = true;
                    flips++;
                } else {
                    // אם מצאנו דיסק של השחקן הנוכחי, ונמצא יריב קודם, סיים את הבדיקה בכיוון זה
                    if (foundOpponent) {
                        break;
                    } else {
                        flips = 0; // אם אין יריב ברצף, אין מה להפוך
                        break;
                    }
                }

                // המשך לכיוון הבא
                currentRow += direction[0];
                currentCol += direction[1];
            }

            // הוסף את מספר ההיפוכים הכולל
            totalFlips += flips;
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
        initializeBoard();
    }

    @Override
    public boolean isFirstPlayerTurn() {
        return isPlayer1Turn;
    }

    @Override
    public boolean isGameFinished() {
        boolean originalTurn = isPlayer1Turn;

        isPlayer1Turn = true;
        boolean player1HasMoves = !ValidMoves().isEmpty();

        isPlayer1Turn = false;
        boolean player2HasMoves = !ValidMoves().isEmpty();

        isPlayer1Turn = originalTurn;

        if (!(player1HasMoves || player2HasMoves)) {
            // המשחק נגמר
            printWinner();
            return true;
        }

        return false;
    }

    public void printWinner(){
        if (isGameFinished()){

            int countPlayer1Disks = 0;
            int countPlayer2Disks = 0;

            String winnerNumber;
            String loserNumber;

            int winnerCountDisks = 0;
            int loserCountDisks = 0;



            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    Disc currentDisc = board[row][col];

                    if (currentDisc.getOwner().equals(player1)){ countPlayer1Disks++ ;}
                    if (currentDisc.getOwner().equals(player2)){ countPlayer2Disks++ ;}
                }
            }



            if(countPlayer1Disks > countPlayer2Disks ){

                winnerNumber = "1";
                loserNumber = "2";
                winnerCountDisks = countPlayer1Disks;
                loserCountDisks = countPlayer2Disks;
            }
            else {
                winnerNumber = "2";
                loserNumber = "1";
                winnerCountDisks = countPlayer2Disks;
                loserCountDisks = countPlayer1Disks;}

            System.out.println("Player " + winnerNumber + " wins with " + winnerCountDisks+ "discs! Player " + loserNumber + "had " + loserCountDisks + " discs. ");
        }

    }



    @Override
    public void reset() {
        System.out.println("Resetting game.");
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = null;
            }
        }

        board[3][3] = new SimpleDisc(player1);
        board[3][4] = new SimpleDisc(player2);
        board[4][3] = new SimpleDisc(player2);
        board[4][4] = new SimpleDisc(player1);

        isPlayer1Turn = true;
    }




    @Override
    public void undoLastMove() {

    }
    private int handleBombDisc(Position position) {
        int flipped = 0;

        // עובר על כל הסביבה של הדיסק הפצצה
        int[][] bombDirections = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int[] dir : bombDirections) {
            int adjacentRow = position.row() + dir[0];
            int adjacentCol = position.col() + dir[1];

            if (adjacentRow >= 0 && adjacentRow < BOARD_SIZE &&
                    adjacentCol >= 0 && adjacentCol < BOARD_SIZE &&
                    board[adjacentRow][adjacentCol] != null) {
                        board[adjacentRow][adjacentCol] = null;
                        flipped++; // ספר את כל הדיסקים המושפעים
            }
        }

        return flipped;
    }
    private boolean preform_move(Position move, Disc disc) {
        System.out.println("Attempting to perform move at: " + move);

        // בדיקה אם המהלך חוקי
        if (!locate_disc(move, disc)) {
            System.out.println("Move is invalid.");
            return false;
        }

        // הנח את הדיסק
        board[move.row()][move.col()] = disc;
        System.out.println("Move placed at: (" + move.row() + ", " + move.col() + ")");

        // כאן ניתן להוסיף פונקציה להיפוך דיסקים, אם יש צורך
        // flipDiscs(move, disc);

        return true;
    }



    private boolean canCaptureInDirection(int startRow, int startCol, int dRow, int dCol, Disc disc) {
        int currentRow = startRow + dRow;
        int currentCol = startCol + dCol;

        boolean foundOpponent = false;

        while (currentRow >= 0 && currentRow < BOARD_SIZE &&
                currentCol >= 0 && currentCol < BOARD_SIZE) {
            Disc currentDisc = board[currentRow][currentCol];

            if (currentDisc == null) {
                // אין רצף בכיוון הזה
                return false;
            }

            if (!currentDisc.getOwner().equals(disc.getOwner())) {
                // מצאנו דיסק של היריב
                foundOpponent = true;
            } else {
                // אם מצאנו דיסק של השחקן הנוכחי אחרי דיסק של היריב
                return foundOpponent;
            }

            // התקדמות בכיוון
            currentRow += dRow;
            currentCol += dCol;
        }

        // אם הלולאה הסתיימה ללא מציאת דיסק של השחקן הנוכחי, זה לא כיוון תקף
        return false;
    }

    private List<Position> getFlippableDiscsInDirection(Position position, int dRow, int dCol, Disc disc) {
        List<Position> flippableDiscs = new ArrayList<>();
        int currentRow = position.row() + dRow;
        int currentCol = position.col() + dCol;
        boolean foundOpponent = false;

        while (currentRow >= 0 && currentRow < BOARD_SIZE &&
                currentCol >= 0 && currentCol < BOARD_SIZE) {
            Disc currentDisc = board[currentRow][currentCol];

            if (currentDisc == null) {
                // אין רצף בכיוון הזה
                return new ArrayList<>();
            }

            if (!currentDisc.getOwner().equals(disc.getOwner())) {
                // מצאנו דיסק של היריב
                foundOpponent = true;
                flippableDiscs.add(new Position(currentRow, currentCol));
            } else {
                // מצאנו דיסק של השחקן הנוכחי
                return foundOpponent ? flippableDiscs : new ArrayList<>();
            }

            // התקדמות בכיוון
            currentRow += dRow;
            currentCol += dCol;
        }

        // אם לא מצאנו דיסק של השחקן הנוכחי, הרצף לא חוקי
        return new ArrayList<>();
    }

    private boolean hasValidDirection(Position position, Disc disc) {
        for (int[] direction : directions) {
            int dRow = direction[0];
            int dCol = direction[1];

            // בדיקת דיסקים שניתן להפוך בכיוון זה
            if (!getFlippableDiscsInDirection(position, dRow, dCol, disc).isEmpty()) {
                return true; // אם לפחות כיוון אחד חוקי
            }
        }
        return false; // אין כיוונים חוקיים
    }


}
