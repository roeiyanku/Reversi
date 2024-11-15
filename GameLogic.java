import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;
public class GameLogic implements PlayableLogic {

    private Player player1;
    private Player player2;
    private Boolean isPlayer1Turn;
    private Disc[][] board;
    private static final int BOARD_SIZE = 8;

    public GameLogic() {
        // Initialize the board
        board = new Disc[BOARD_SIZE][BOARD_SIZE];
        initial_Board();
    }

    public void initial_Board() {
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
    public boolean locate_disc(Position a, Disc disc) {
        return false;
    }

    @Override
    public Disc getDiscAtPosition(Position position) {

        return board[position.getRow()][position.getColumn()];
    }

    @Override
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    // ?
    @Override
    public List<Position> ValidMoves() {
        List<Position> validMoveList = new ArrayList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == null) {

                    Position position = new Position(row, col);

                    Disc tempDisc = new SimpleDisc(player1);

                    if (locate_disc(position, tempDisc)) {
                        validMoveList.add(position);
                    }
                }
            }
        }

        return validMoveList;
    }

    @Override
    public int countFlips(Position a) {
        return 0;
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
        return false;
    }

    @Override
    public void reset() {
        initial_Board();

    }

    @Override
    public void undoLastMove() {

    }

}
