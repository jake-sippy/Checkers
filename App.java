import java.util.*;

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

    public static final String[] knownPlayers = {
        "human",
        "random",
        "minimax",
    };

    public static void main(String[] args) {
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

        run(board, black, red);
    }

    private static Player createPlayer(String type, Board board) {
        if (type.equals("human"))
            return new Human(board);
        else if (type.equals("random"))
            return new RandomBot(board);
        else if (type.equals("minimax")) {
            return new MinimaxBot(board, 7);
        } else
            return null; // unknown type
    }

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
                Thread.sleep(1000 * 0);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        
        // Print how the game ended
        printBoard(board);
        printTurn(board);
        System.out.println("GAME OVER");
    }

    private static void printTurn(Board b) {
        System.out.println();
        System.out.print("Turn : ");
        if (b.getTurn() == Color.RED) {
            System.out.println(ANSI_RED + "RED" + ANSI_RESET);
        } else {
            System.out.println(ANSI_WHITE + "BLACK" + ANSI_RESET);
        }
    }

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

    private static void drawPiece(Board board, int place) {
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
