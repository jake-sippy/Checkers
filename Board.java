import java.util.*;

public class Board {
    // static int for board width
    public static final int BOARD_SIZE = 8;

    // array of checkers pieces (backend for board)
    private Piece[][] board;

    public Board() {
        board = new Piece[BOARD_SIZE + 1][BOARD_SIZE + 1];
        // Red pieces
        for (int y = 1; y < BOARD_SIZE / 2; y++) {
            for (int x = 1; x <= BOARD_SIZE; x++) {
                if (x % 2 == y % 2) {
                    board[x][y] = new Piece(Color.RED, false);
                }
            }
        }

        // Black pieces
        for (int y = BOARD_SIZE; y > BOARD_SIZE / 2 + 1; y--) {
            for (int x = 1; x <= BOARD_SIZE; x++) {
                if (x % 2 == y % 2) {
                    board[x][y] = new Piece(Color.BLACK, false);
                }
            }
        }
    }

    private void checkCoord(int x, int y) {
        if ((x < 1 || x > BOARD_SIZE) || (y < 1 || y > BOARD_SIZE))
            throw new IllegalArgumentException();
    }

    public boolean hasPiece(int x, int y) {
        checkCoord(x, y);
        return (board[x][y] != null);
    }


    public boolean isKing(int x, int y) {
        if (hasPiece(x, y))
            return board[x][y].isKing();
        return false;
    }

    public Color getColor(int x, int y) {
        if (!hasPiece(x, y))
            throw new NoSuchElementException();
        return board[x][y].getColor();
    }

    public Piece getPiece(int x, int y) {
        if (!hasPiece(x, y))
            throw new NoSuchElementException();
        return board[x][y];
    }

    public boolean isLegalMove(int startX, int startY,
                               int endX, int endY) {
        checkCoord(startX, startY);
        checkCoord(endX, endY);
        if (!hasPiece(startX, startY))
            return false;
        else if (hasPiece(endX, endY))
            return false;
        else if (Math.abs(startX - endX) != 1 ||
                 Math.abs(startY - endY) != 1)
            return false;
        else
            return true;
    }

    public String toString() {
        String result = "\n";
        for (int y = BOARD_SIZE; y >= 1; y--) {
            result += y + "    ";
            for (int x = 1; x <= BOARD_SIZE; x++) {
                result += "[";
                if (hasPiece(x, y))
                    result += board[x][y];
                else
                    result += "--";
                result  += "] ";
            }
            result += "\n\n";
        }

        result += "      ";
        for (int i = 1; i <= BOARD_SIZE; i++) {
            result += i + "    ";
        }
        return result;
    }
}
