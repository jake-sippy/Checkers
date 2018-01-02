import java.util.*;

/**
 * The frontend for checkers
 */
public class App {
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
    
    // helpful array of known implementations of Player
    private static final String[] knownPlayers = {
        "human",
        "random",
        "minimax",
    };
    
    /**
     * Runs a game of checkers
     * @param args the flags passed by the user, must be of
     *              length 2, where args[0] is the type of
     *              player who will be playing black, and
     *              args[1] will be the player playing
     *              red
     */
    public static void main(String[] args) {
        // Print usage if args does not look correct
        if (args.length != 2) {
            System.out.println("Usage: java App <black player> " +
                    "<red player>");
            System.out.println("Player types:"); 
            for (String player : knownPlayers)
                System.out.println("\t" + player);
            return;
        }
         
        Scanner input = new Scanner(System.in);
        Board board = new Board();
        String p1 = args[0].trim();
        String p2 = args[1].trim();
        
        Player black;
        Player red;
        
        black = createPlayer(p1, board);
        red = createPlayer(p2, board);

        if (black == null) {
            System.out.println("Unknown player type: " + p1);
            return;
        }

        if (red == null) {
            System.out.println("Unknown player type: " + p2);
            return;
        }
        
        // correct players given, run game
        run(board, black, red);
    }

    /**
     * Helper method which maps legal user flags to the correct
     * implementation of player
     * @param type the flag passed by the user
     * @param board the reference to the board
     */
    private static Player createPlayer(String type, Board board) {
        if (type.equals("human"))
            return new Human(board);
        else if (type.equals("random"))
            return new RandomBot(board);
        else if (type.equals("minimax")) {
            return new MinimaxBot(board);
        } else
            return null; // unknown type
    }

    /**
     * Helper method that runs the game loop
     * @param board the board to be played on
     * @param black the player playing black
     * @param red the player playing red
     */
    private static void run(Board board, Player black, Player red) {

        // Game loop
        while (!board.gameOver()) {
            printBoard(board);
            printTurn(board);
            
            Set<Move> moves = board.getLegalMoves();

            if (board.getTurn() == Color.BLACK) {
                Move blackMove = black.getMove();
                if (moves.contains(blackMove))
                    board.move(blackMove);
            } else { // Red's move
                Move redMove = red.getMove();
                if (moves.contains(redMove))
                    board.move(redMove);
            }
            
            try {
                // wait 0.1 seconds, makes faster bots
                // moves visable
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        
        // Print how the game ended
        printBoard(board);
        printTurn(board);
        System.out.println("GAME OVER");
    }

    /**
     * Prints which players turn it is
     * @param b the board being played on
     */
    private static void printTurn(Board b) {
        System.out.println();
        System.out.print("Turn : ");
        if (b.getTurn() == Color.RED) {
            System.out.println(ANSI_RED + "RED" + ANSI_RESET);
        } else {
            System.out.println(ANSI_WHITE + "BLACK" + ANSI_RESET);
        }
    }
    
    /**
     * Prints the board being played on
     * @param board the board being played on
     */
    public static void printBoard(Board b) {
        // space before board
        System.out.println();
        for (int i = 0; i < b.getNumOfPlaces(); i++) {
            int rowType = i % 8;

            // indent the board
            if (rowType == 0 || rowType == 4)
                System.out.print(i + "\t");

            if (rowType < 4)
                System.out.print(ANSI_BLUE + "[[]] " + ANSI_RESET);

            drawPiece(b, i + 1);

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
        // System.out.println("Moves: " + b.getLegalMoves());
    }
    
    /**
     * Prints the piece at the given place on the board
     * @param board the board being played on
     * @param place the place whose piece will be drawn
     */
    private static void drawPiece(Board board, int place) {
        if (board.hasPieceAt(place)) {

            boolean isKing = board.isKing(place);

            if (board.getColor(place) == Color.RED) {
                System.out.print("[" + ANSI_RED);       // red output for red piece

                if (isKing)
                    System.out.print("KK");             // KK if king
                else
                    System.out.print("RR");             // RR if not king

                System.out.print(ANSI_RESET + "] ");
            } else {
                System.out.print("[" + ANSI_WHITE);     // white output for black piece

                if (isKing)
                    System.out.print("KK");             // KK if king
                else
                    System.out.print("BB");             // BB if not king

                System.out.print(ANSI_RESET + "] ");
            }
        } else {
            System.out.print("[  ] ");                  // empty if no piece
        }
    }

}
