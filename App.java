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

    public static void main(String[] args) {
        Board b = new Board();
        Bot bot = new Bot(b, Color.RED);
        Scanner input = new Scanner(System.in);
        while (!b.gameOver()) {
            printBoard(b);
            Set<Move> moves = b.getLegalMoves();
            if (b.getTurn() == Color.BLACK) {
                boolean goodStart = false;
                int start = promptStart(input);
                
                for (Move m : moves) {
                    if (m.getStart() == start) {
                        goodStart = true;
                        break;
                    }
                }
                
                if (goodStart) {
                    printMoves(b, start);
                    int end = promptEnd(input);

                    for (Move m : moves) {
                        if (m.getStart() == start && m.getEnd() == end)
                            b.move(m);
                    }
                }

            } else {
                Move botMove = bot.getMove();
                if (moves.contains(botMove))
                    b.move(botMove);
            }
        }

        System.out.println("Game Over");
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
        printTurn(b);
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
        System.out.println("Moves: " + b.getLegalMoves());
    }

    private static void printMoves(Board b, int place) {
        printTurn(b);
        Set<Move> moves = b.getLegalMoves();

        // space before board
        System.out.println();
        for (int i = 0; i < b.getNumOfPlaces(); i++) {
            int rowType = i % 8;

            // indent the board
            if (rowType == 0 || rowType == 4)
                System.out.print(i + "\t");

            if (rowType < 4)
                System.out.print(ANSI_BLUE + "[[]] " + ANSI_RESET);

            if (isMove(moves, place, i + 1)) {
                System.out.print("[" + ANSI_GREEN + ":)" +
                        ANSI_RESET + "] ");
            } else {
                drawPiece(b, i + 1);
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

        System.out.println("Moves: " + moves);
    }

    private static boolean isMove(Set<Move> moves, int start, int end) {
        for (Move move : moves) {
            if (move.getStart() == start && move.getEnd() == end) {
                return true;
            }
        }

        return false;
    }

    private static void drawPiece(Board b, int place) {
        if (b.hasPieceAt(place)) {
            if (b.getColor(place) == Color.RED) {
                if (b.isKing(place)) {
                    System.out.print("[" + ANSI_RED + "KK" +
                            ANSI_RESET + "] ");
                } else {
                    System.out.print("[" + ANSI_RED + "RR" +
                            ANSI_RESET + "] ");
                }
            } else {
                if (b.isKing(place)) {
                    System.out.print("[" + ANSI_WHITE + "KK" +
                            ANSI_RESET + "] ");
                } else {
                    System.out.print("[" + ANSI_WHITE + "BB" +
                            ANSI_RESET + "] ");
                }
            }
        } else {
            System.out.print("[  ] ");
        }
    }

    private static int promptStart(Scanner input) {
        int place = -1;
        while (place < 1 || place > 32) {
            System.out.print("Enter start (1 - 32): ");
            while (!input.hasNextInt()) {
                if (input.hasNext()) {
                    input.next();
                    System.out.print("Enter start (1 - 32): ");
                }
            }
            place = input.nextInt();
        }
        return place;
    }

    private static int promptEnd(Scanner input) {
        int place = -1;
        while (place < 1 || place > 32) {
            System.out.print("Enter end (1 - 32): ");
            while (!input.hasNextInt()) {
                if (input.hasNext()) {
                    input.next();
                    System.out.print("Enter end (1 - 32): ");
                }
            }
            place = input.nextInt();
        }
        return place;
    }
}
