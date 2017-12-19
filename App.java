import java.util.*;

public class App {
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
            int startX = promptX(input);
            int startY = promptY(input);
        
            printMoves(b, startX, startY);

            int endX = promptX(input);
            int endY = promptY(input);
            if (b.isLegalMove(startX, startY, endX, endY))
                b.move(startX, startY, endX, endY);
        }
    }

    public static void printBoard(Board b) {
        for (int y = Board.BOARD_SIZE - 1; y >= 0; y--) {
            System.out.print(y + "    ");
            for (int x = 0; x < Board.BOARD_SIZE; x++) {
                if (b.hasPiece(x, y)) {
                    System.out.print("[");
                    if (b.getColor(x, y) == Color.BLACK) {
                        System.out.print(ANSI_WHITE + "BB");
                    } else {
                        System.out.print(ANSI_RED + "RR");
                    }
                    System.out.print(ANSI_RESET);
                    System.out.print("] ");
                } else {
                    System.out.print("[  ] ");
                }
            }
            System.out.println();
            System.out.println();
        }

        System.out.print("      ");
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            System.out.print(i + "    ");
        }
        System.out.println();
    }

    public static void printMoves(Board b, int startX, int startY) {
        if (!b.hasPiece(startX, startY)) {
            System.out.println("There is no piece at (" +
                    startX + ", " + startY + ").");
            return;
        }
        
        System.out.println();
        for (int y = Board.BOARD_SIZE - 1; y >= 0; y--) {
            System.out.print(y + "    ");
            for (int x = 0; x < Board.BOARD_SIZE; x++) {

                if (b.hasPiece(x, y)) {
                    System.out.print("[");
                    if (b.getColor(x, y) == Color.BLACK) {
                        System.out.print(ANSI_WHITE + "BB");
                    } else {
                        System.out.print(ANSI_RED + "RR");
                    }
                    System.out.print(ANSI_RESET);
                    System.out.print("] ");
                } else if (b.isLegalMove(startX, startY, x, y))
                    System.out.print("[" + ANSI_CYAN + "&&" +
                            ANSI_RESET + "] ");
                else
                    System.out.print("[  ] ");
            }
            System.out.println();
            System.out.println();
        }

        System.out.print("      ");
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            System.out.print(i + "    ");
        }
        System.out.println();

    }

    public static int promptX(Scanner input) {
        System.out.print("Enter x-coordinate (0 - " + (Board.BOARD_SIZE - 1) + "): ");
        int result = input.nextInt();
        if (result >= 0 && result <= Board.BOARD_SIZE)
            return result;
        else
            return promptX(input);
    }


    public static int promptY(Scanner input) {
        System.out.print("Enter y-coordinate (0 - " + (Board.BOARD_SIZE - 1) + "): ");
        int result = input.nextInt();
        if (result >= 0 && result < Board.BOARD_SIZE)
            return result;
        else
            return promptY(input);
    }
}
