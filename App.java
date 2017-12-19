import java.util.*;

public class App {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {
        Board b = new Board();
        printBoard(b);

        Scanner input = new Scanner(System.in);
        while (true) {
            int x = promptX(input);
            int y = promptY(input);

            printMoves(b, x, y);
        }
    }

    public static void printBoard(Board b) {
        for (int y = Board.BOARD_SIZE; y >= 1; y--) {
            System.out.print(y + "    ");
            for (int x = 1; x <= Board.BOARD_SIZE; x++) {
                if (b.hasPiece(x, y))
                    System.out.print("[" + b.getPiece(x, y) + "] ");
                else
                    System.out.print("[--] ");
            }
            System.out.println();
            System.out.println();
        }

        System.out.print("      ");
        for (int i = 1; i <= Board.BOARD_SIZE; i++) {
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
        for (int y = Board.BOARD_SIZE; y >= 1; y--) {
            System.out.print(y + "    ");
            for (int x = 1; x <= Board.BOARD_SIZE; x++) {
                if (b.hasPiece(x, y))
                    System.out.print("[" + b.getPiece(x, y) + "] ");
                else if (b.isLegalMove(startX, startY, x, y))
                    System.out.print("[" + ANSI_GREEN + "&&" +
                            ANSI_RESET + "] ");
                else
                    System.out.print("[--] ");
            }
            System.out.println();
            System.out.println();
        }

        System.out.print("      ");
        for (int i = 1; i <= Board.BOARD_SIZE; i++) {
            System.out.print(i + "    ");
        }
        System.out.println();

    }

    public static int promptX(Scanner input) {
        System.out.print("Enter x-coordinate (1 - " + Board.BOARD_SIZE + "): ");
        int result = input.nextInt();
        if (result >= 1 && result <= Board.BOARD_SIZE)
            return result;
        else
            return promptX(input);
    }


    public static int promptY(Scanner input) {
        System.out.print("Enter y-coordinate (1 - " + Board.BOARD_SIZE + "): ");
        int result = input.nextInt();
        if (result >= 1 && result <= Board.BOARD_SIZE)
            return result;
        else
            return promptY(input);
    }
}
