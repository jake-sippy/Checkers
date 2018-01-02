import java.util.*;

/**
 * Implmentation of Player, which allows a human to play
 */
public class Human implements Player {
    // static ANSI codes for colorizing output
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    
    // reference to the board being played on
    private Board board;
    // source of user input
    private Scanner input;
    
    /**
     * Creates a new Human instance
     * @param board the board being played on
     */
    public Human(Board board) {
         this.board = board;
         // user input source is system.in
         this.input = new Scanner(System.in);
    }

    /**
     * Gets and returns a move from the human player
     * @return a legal move, as chosen by the player
     */
    public Move getMove() {
        Set<Move> moves = board.getLegalMoves();

        // keep trying until we get a legal move
        while (true) {

            boolean goodStart = false;
            int start = promptPlace("start");
            
            for (Move m : moves) {
                if (m.getStart() == start)
                    goodStart = true;
            }

            if (goodStart) {

                printMoves(start);

                int end = promptPlace("end");
                Move userMove = new Move(start, end);
                if (moves.contains(userMove))
                    return userMove;
            }
        }
    }
    
    /**
     * Helper method which prompts the user for a legal
     * place with the name placeName
     * @param placeName the name of the place being prompted for
     * @return a legal place on the board, as chosen by the user
     */
    private int promptPlace(String placeName) {
        int place = 0;
        while (place < 1 || place > 32) {
            System.out.print("Enter " + placeName + " (1 - 32): ");
            
            while (!input.hasNextInt()) {
                // clear non-ints
                if (input.hasNext()) {
                    input.next();
                    System.out.print("Enter " + placeName +
                            " (1 - 32): ");
                }
            }

            place = input.nextInt();
        }
        return place;
    }

    /**
     * Print the board with legal ending places
     * highlighted
     * @param place the starting place to check for moves
     */
    private void printMoves(int place) {
        Set<Move> moves = board.getLegalMoves();

        // space before board
        System.out.println();
        for (int i = 0; i < board.getNumOfPlaces(); i++) {
            int rowType = i % 8;

            // indent the board
            if (rowType == 0 || rowType == 4)
                System.out.print(i + "\t");

            if (rowType < 4)
                System.out.print(ANSI_BLUE + "[[]] " + ANSI_RESET);

            if (moves.contains(new Move(place, i + 1))) {
                System.out.print("[" + ANSI_GREEN + ":)" +
                        ANSI_RESET + "] ");
            } else {
                drawPiece(i + 1);
            }

            if (rowType >= 4)
                System.out.print(ANSI_BLUE + "[[]] " + ANSI_RESET);

            if (rowType == 3 || rowType == 7) {
                System.out.println();
                System.out.println();
            }
        }

        System.out.print(" \t    ");
        for (int i = 1; i <= Board.WIDTH / 2; i++) {
            System.out.print("+" + i + "        ");
        }
        System.out.println();
        System.out.println();
    }
   
    /**
     * Helper method for printMoves which draws the piece at the
     * given place
     * @param place the place whose piece will be drawn
     */
    private void drawPiece(int place) {
        if (board.hasPieceAt(place)) {

            boolean isKing = board.isKing(place);

            if (board.getColor(place) == Color.RED) {
                System.out.print("[" + ANSI_RED);

                if (isKing)
                    System.out.print("KK");
                else
                    System.out.print("RR");

                System.out.print(ANSI_RESET + "] ");
            } else {
                System.out.print("[" + ANSI_WHITE);

                if (isKing)
                    System.out.print("KK");
                else
                    System.out.print("BB");

                System.out.print(ANSI_RESET + "] ");
            }
        } else {
            System.out.print("[  ] ");
        }
    }
}
