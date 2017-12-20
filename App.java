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
        Scanner input = new Scanner(System.in);
        while (true) {
            printBoard(b);

            Point start = promptStart(input);
            if (b.hasPiece(start)) {
                printMoves(b, start);
                Point end = promptEnd(input);
            } else {
                System.out.println(start + " is does not correspond to a piece")
            }
        }
    }

    public static void printBoard(Board b) {
        printMoves(b, null);
    }

    public static void printMoves(Board b, Point p) {
        Set<Move> moves;
        moves = b.getLegalMoves();

        System.out.println();
        for (int y = Board.WIDTH - 1; y >= 0; y--) {
            System.out.print(y + "    ");
            for (int x = 0; x < Board.WIDTH; x++) {
                Point curr = new Point(x, y);
                if (b.hasPiece(curr))
                    drawPiece(b.getColor(curr));
                else
                    drawMove(moves, p);
                System.out.print(' ');
            }
            System.out.println();
            System.out.println();
        }

        System.out.print("       ");
        for (int i = 0; i < Board.WIDTH; i++) {
            System.out.print(i + "    ");
        }
        System.out.println();
        System.out.println();
    }

    private static void drawPiece(Color c) {
        if (c == Color.RED) {
            System.out.print(ANSI_RED + "[RR]" + ANSI_RESET);
        } else {
            System.out.print(ANSI_WHITE + "[BB]" + ANSI_RESET);
        }
    }

    private static void drawMove(Set<Move> moves, Point p) {
        if (p != null) {
            for (Move move : moves) {
                if (move.getStart().equals(p)) {
                    System.out.print(ANSI_GREEN + "[:)]" + ANSI_RESET);
                    return;
                }
            }
        }
        System.out.print("[  ]");
    }

    public static int promptX(Scanner input) {
        System.out.print("Enter x-coordinate (0 - " + (Board.WIDTH - 1) + "): ");
        int result = input.nextInt();
        if (result >= 0 && result <= Board.WIDTH)
            return result;
        else
            return promptX(input);
    }


    public static int promptY(Scanner input) {
        System.out.print("Enter y-coordinate (0 - " + (Board.WIDTH - 1) + "): ");
        int result = input.nextInt();
        if (result >= 0 && result < Board.WIDTH)
            return result;
        else
            return promptY(input);
    }

    public static Point promptStart(Scanner input) {
        System.out.println("Choose a piece");
        return promptPoint(input);
    }

    public static Point promptEnd(Scanner input) {
        System.out.println("Choose a move");
        return promptPoint(input);
    }

    private static Point promptPoint(Scanner input) {
        System.out.print("\tEnter a point: ");

        int x = input.nextInt();
        int y = input.nextInt();
        Point result = new Point(x, y);
        System.out.print(result);
        return result;
    }
}
