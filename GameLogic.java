import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * The GameLogic class implements the PlayableLogic. It contains the core rules and mechanics of the game,
 * including move validation, player turns, and game state management.
 */


public class GameLogic implements PlayableLogic{

    private Player player1;
    private Player player2;
    private boolean isPlayer1Turn = true;
    private Disc[][] board = new Disc[getBoardSize()][getBoardSize()];
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

    //Meant to place the disc at the right position on the board.
    @Override
    public boolean locate_disc(Position position, Disc disc) {
        int row = position.row();
        int col = position.col();

        // Check if the location is out of bounds or occupied
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

        // If there are no valid directions, the move is invalid
        if (!validMove) {
            return false;
        }

        // Making the move - placing the disc
        //Tomer: Please add that:
        //if("disk is in the valid type of disk list"){
            board[row][col] = disc;
            Player currentPlayer = isPlayer1Turn ? player1 : player2;
            List<Position> flippedDiscs = new ArrayList<>();


        //}

        if(disc instanceof BombDisc){
            disc.getOwner().reduce_bomb();
            }

        if(disc instanceof UnflippableDisc){
            disc.getOwner().reduce_unflippedable();
        }

        // Identifying the disk type
        System.out.println("Player " + (disc.getOwner().equals(player1) ? "1" : "2") +
                " placed a " + disc.getType() + " in " + position.toString());



        // flipping the disks
        for (Position flipPosition : discsToFlip) {

            int flipRow = flipPosition.row();
            int flipCol = flipPosition.col();


            if(board[flipRow][flipCol] instanceof UnflippableDisc){
                continue;} //This makes the Unflippable Disk never be flipped.

            if(board[flipRow][flipCol] instanceof BombDisc){
                board[flipRow][flipCol] = new BombDisc(disc.getOwner());
                BombDiscResult result =handleBombDisc(flipPosition);
              //  int flippedCount = result.getFlippedCount();
                List<Position> flippedBombDiscs = result.getFlippedBombDiscs();
                flippedDiscs.addAll(flippedBombDiscs);
                flippedDiscs.add(flipPosition);
            }

            else {board[flipRow][flipCol] = new SimpleDisc(disc.getOwner());
                flippedDiscs.add(flipPosition);
            }

            System.out.println("Player " + (disc.getOwner().equals(player1) ? "1" : "2") +
                    " flipped the " + disc.getType() + " in " + flipPosition.toString());
        }
        System.out.println();


        // saving the move in the history
        Move newMove = new Move(position, disc, currentPlayer, flippedDiscs);
        moveHistory.push(newMove);


        isPlayer1Turn = !isPlayer1Turn;

        return true;
    }



    @Override
    public Disc getDiscAtPosition(Position position) {

        int row = position.row();
        int col = position.col();

        // Correct bounds checking
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

        // Check who the current player is
        Disc currentDisc = isPlayer1Turn ? new SimpleDisc(player1) : new SimpleDisc(player2);

        // Go over each position in the board
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Position position = new Position(row, col);

                // If the position is free and there is at least one valid direction
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

        // If the location is out of bounds or occupied, nothing to calculate
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE || board[row][col] != null) {
            return 0;
        }

        // Determine who the current player is
        Disc currentDisc = isPlayer1Turn ? new SimpleDisc(player1) : new SimpleDisc(player2);

        // check all 8 directions
        for (int[] direction : directions) {
            int flips = 0;
            int currentRow = row + direction[0];
            int currentCol = col + direction[1];
            boolean foundOpponent = false;

            // Progress in the direction
            while (currentRow >= 0 && currentRow < BOARD_SIZE &&
                    currentCol >= 0 && currentCol < BOARD_SIZE) {
                Disc currentDiscInDirection = board[currentRow][currentCol];
                Position currentPosition = new Position(currentRow, currentCol);

                // if the position is empty then null
                if (currentDiscInDirection == null) {
                    flips = 0;
                    break;
                }

                if ((currentDiscInDirection instanceof UnflippableDisc) && (!currentDiscInDirection.getOwner().equals(currentDisc.getOwner()))){
                    flips--;
                    foundOpponent = true;
                    //needs to skip to the next square
                }

                if ((currentDiscInDirection instanceof BombDisc)  && (!currentDiscInDirection.getOwner().equals(currentDisc.getOwner()))) {
                    flips += );
                    foundOpponent = true;
                }

                // regular disk count
                //if the cuurent disc is of the opponent
                if (!currentDiscInDirection.getOwner().equals(currentDisc.getOwner())) {
                    foundOpponent = true;
                    flips++;
                }
                //If the current disc is the current player's disk:
                if (currentDiscInDirection.getOwner().equals(currentDisc.getOwner()) ){
                    // If we found a disk of the current player, and found a previous opponent, end the test in that direction
                    if (foundOpponent) {
                        break;
                    }
                    if(!foundOpponent){
                        flips = 0; // if there is no opponent in the line, nothing to flip.
                        break;
                    }
                }

                 // checking the next direction
                currentRow += direction[0];
                 currentCol += direction[1];
            }

            // adding total flips
            totalFlips += flips;
        }

        return totalFlips;
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
                // No progress in direction
                return new ArrayList<>();
            }



            if (!currentDisc.getOwner().equals(disc.getOwner())) {
                // found opponent's disk
                foundOpponent = true;
                if(!(currentDisc instanceof UnflippableDisc)){ //can't add unflippable Discs to Flippable list
                    flippableDiscs.add(new Position(currentRow, currentCol));}
            } else {
                // We found the current player's disk
                return foundOpponent ? flippableDiscs : new ArrayList<>();
            }

            // Progress in direction
            currentRow += dRow;
            currentCol += dCol;
        }

        // If we did not find a disc for the current player, the sequence is invalid
        return new ArrayList<>();
    }

    //returns true or false if the current disk has at least one validDirection
    private boolean hasValidDirection(Position position, Disc disc) {
        for (int[] direction : directions) {
            int dRow = direction[0];
            int dCol = direction[1];

            // Check for disks that can be flipped in this direction
            if (!getFlippableDiscsInDirection(position, dRow, dCol, disc).isEmpty()) {
                return true; // If at least one direction is valid
            }
        }
        return false; // No valid directions.
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

        if ((isPlayer1Turn && !player1HasMoves) || (!isPlayer1Turn && !player2HasMoves)) {
            //game is over
            moveHistory.clear();
            printWinner();
            return true;
        }

        return false;
    }

    //method used to print the winner of the game. (and also count the discs for each player)
    public void printWinner(){

            int countPlayer1Disks = 0;
            int countPlayer2Disks = 0;

            String winnerNumber;
            String loserNumber;

            int winnerCountDisks = 0;
            int loserCountDisks = 0;



            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    Disc currentDisc = board[row][col];

                    if (currentDisc == null) {
                        continue;}

                    if (currentDisc.getOwner().equals(player1)){ countPlayer1Disks++ ;}
                    if (currentDisc.getOwner().equals(player2)){ countPlayer2Disks++ ;}
                }
            }




            if(countPlayer1Disks == countPlayer2Disks ){System.out.println("Tie!");} //If there's a tie, No one gets a win.

            else if(countPlayer1Disks > countPlayer2Disks ){ //If player1 wins.

                player1.wins++;

                winnerNumber = "1";
                loserNumber = "2";
                winnerCountDisks = countPlayer1Disks;
                loserCountDisks = countPlayer2Disks;
                System.out.println("Player " + winnerNumber + " wins with " + winnerCountDisks+ " discs! Player " + loserNumber + " had " + loserCountDisks + " discs. ");
            }
            else { //If player2 wins.

                player2.wins++;

                winnerNumber = "2";
                loserNumber = "1";
                winnerCountDisks = countPlayer2Disks;
                loserCountDisks = countPlayer1Disks;
                System.out.println("Player " + winnerNumber + " wins with " + winnerCountDisks+ " discs! Player " + loserNumber + " had " + loserCountDisks + " discs. ");
            }


    }



    @Override
    public void reset() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = null;
            }
        }

        board[3][3] = new SimpleDisc(player1);
        board[3][4] = new SimpleDisc(player2);
        board[4][3] = new SimpleDisc(player2);
        board[4][4] = new SimpleDisc(player1);

        moveHistory.clear();

        isPlayer1Turn = true;
    }




    @Override
    public void undoLastMove() {
        //if at least one player is not human, UndoLastMove cannot be used.
        if(!(player1.isHuman()) || !(player2.isHuman())){
            System.out.println("UndoLastMove cannot be used since there's at least one player who is not human");
            return;
        }
        System.out.println("Undoing last move:");

        if(moveHistory.empty()){ //If first move.
            System.out.println("\tNo previous move available to undo.");
            System.out.println();
            return;
        }

        Move lastMove = moveHistory.pop(); //get the last move and all its contents.
        Position lastPosition = lastMove.getPosition();
        Disc lastDisc = lastMove.getDisc();
        List<Position> lastFlippedDiscs = lastMove.getFlippedDisc();

        //removing the last disc located.
        System.out.println("\tUndo: removing " + lastDisc.getType() + " from " + lastPosition.toString());
        board[lastPosition.row()][lastPosition.col()] = null;

        //flipping back all the flipped disks.
        for (Position flipPosition : lastFlippedDiscs) {

            Player originalOwner = (lastDisc.getOwner().equals(player1)) ? player2 : player1;
            Disc originalDisc = new SimpleDisc(originalOwner);

            board[flipPosition.row()][flipPosition.col()] = originalDisc;

            System.out.printf("\tUndo: flipping back "+originalDisc.getType()+" in "+  flipPosition.toString());
        }

        //making sure we add up the number of bombs and unflippables we undid
        if (lastDisc instanceof BombDisc) {
            lastDisc.getOwner().number_of_bombs++;}

        if (lastDisc instanceof UnflippableDisc) {
            lastDisc.getOwner().number_of_unflippedable++;
        }


        // Turn back to the previous player
        isPlayer1Turn = !isPlayer1Turn;

        System.out.println();
    }

    public class BombDiscResult {
        private int flippedCount;
        private List<Position> flippedBombDiscs;

        public BombDiscResult(int flippedCount, List<Position> flippedBombDiscs) {
            this.flippedCount = flippedCount;
            this.flippedBombDiscs = flippedBombDiscs;
        }

        public int getFlippedCount() {
            return flippedCount;
        }

        public List<Position> getFlippedBombDiscs() {
            return flippedBombDiscs;
        }
    }



    //Creating a new class for bombs.
    private BombDiscResult handleBombDisc(Position position) {
        List<Position> flippedBombDiscs = new ArrayList<>();
        int flippedAroundBomb = 0;

        // All 8 directions around the bomb
        int[][] bombDirections = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // Horizontal and vertical
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Diagonals
        };

        // Determine the current player
        Player currentPlayer = isPlayer1Turn ? player1 : player2;

        for (int[] dir : bombDirections) {
            int adjacentRow = position.row() + dir[0];
            int adjacentCol = position.col() + dir[1];
            Position adjacentPosition = new Position(adjacentRow, adjacentCol);

            // Ensure the position is within bounds before accessing the board
            if (adjacentRow >= 0 && adjacentRow < BOARD_SIZE &&
                    adjacentCol >= 0 && adjacentCol < BOARD_SIZE) {

                Disc currentDisc = board[adjacentRow][adjacentCol];

                // Check if the disc is valid for flipping and belongs to the opponent
                if (currentDisc != null &&
                        !(currentDisc instanceof UnflippableDisc) &&
                        currentDisc.getOwner() != currentPlayer) {

                    // Flip the owner of the opponent's disc
                    board[adjacentRow][adjacentCol].setOwner(currentPlayer);
                    flippedBombDiscs.add(adjacentPosition);

                    // Increment the count of flipped discs
                    flippedAroundBomb++;
                }
            }
        }

        return flippedAroundBomb;


        List<Position> getFlippedBombDiscs(){
            return flippedBombDiscs;

        }




    }


    private int countFlippedBombDiscs(Position position) {
        int flippedAroundBomb = 0;

        // Determine the current player
        Player currentPlayer = isPlayer1Turn ? player1 : player2;

        for (int[] dir : directions) {
            int adjacentRow = position.row() + dir[0];
            int adjacentCol = position.col() + dir[1];

            // Ensure the position is within bounds before accessing the board
            if (adjacentRow >= 0 && adjacentRow < BOARD_SIZE &&
                    adjacentCol >= 0 && adjacentCol < BOARD_SIZE) {

                Disc currentDisc = board[adjacentRow][adjacentCol];

                // Check if the disc is valid for flipping and belongs to the opponent
                if (currentDisc != null &&
                        !(currentDisc instanceof UnflippableDisc) &&
                        currentDisc.getOwner() != currentPlayer) {

                    flippedAroundBomb++; // Count the flipped disc
                }
            }
        }

        return flippedAroundBomb;
    }


}
