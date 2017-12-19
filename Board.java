import java.util.*;

public class Board {
    // static int for board width
    public static final int BOARD_SIZE = 8;

    // array of checkers pieces (backend for board)
    private Piece[][] board;
    // the color of whose turn it is
    private Color turn;

    // constructs a new board
    public Board() {
        this.board = new Piece[BOARD_SIZE][BOARD_SIZE];
        this.turn = Color.BLACK;

        // Red pieces
        for (int y = 0; y < (BOARD_SIZE - 1) / 2; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                if (x % 2 == y % 2) {
                    board[x][y] = new Piece(Color.RED, false);
                }
            }
        }

        // Black pieces
        for (int y = BOARD_SIZE - 1; y > BOARD_SIZE / 2; y--) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                if (x % 2 == y % 2) {
                    board[x][y] = new Piece(Color.BLACK, false);
                }
            }
        }
    }

    // Checks if given coordinates are valid on the board
    private void checkCoord(int x, int y) {
        if (x < 0 || x >= BOARD_SIZE) {
            throw new IllegalArgumentException("x-coordinate must be between " +
                    "0 and " + (BOARD_SIZE - 1));
        }
        if (y < 0 || y >= BOARD_SIZE) {
            throw new IllegalArgumentException("y-coordinate must be between " +
                    "0 and " + (BOARD_SIZE - 1));
        }
    }

    // return true if the board has a piece at the given coord
    public boolean hasPiece(int x, int y) {
        return (board[x][y] != null);
    }

    // return true if the board has a king at the given coord
    public boolean isKing(int x, int y) {
        if (hasPiece(x, y))
            return board[x][y].isKing;
        return false;
    }

    // return the color of the piece at the given coord
    public Color getColor(int x, int y) {
        if (!hasPiece(x, y))
            throw new NoSuchElementException();
        return board[x][y].color;
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

    private boolean[][] getLegalAttacks(int x, int y) {
        checkCoord(x, y);
        boolean[][] result = new boolean[BOARD_SIZE][BOARD_SIZE];
        result[x][y] = true;
        return getLegalAttacks(result, x, y);
    }

    private boolean[][] getLegalAttacks(boolean[][] result, int startX, int startY) {
        boolean[][] result2 = result.clone();
        Piece p = board[startX][startY];
        
        if (p.color == Color.RED || p.isKing) {
            
        }

        if (p.color == Color.BLACK || p.isKing) {

        }

        return null;
    }

    public void move(int startX, int startY,
                     int endX, int endY) {
        if(!isLegalMove(startX, startY, endX, endY))
            throw new IllegalArgumentException("Illegal move");
        board[endX][endY] = board[startX][startY];
        board[startX][startY] = null;
    }

    class Piece {
        Color color;
        boolean isKing;

        public Piece(Color color, boolean isKing) {
            this.color = color;
            this.isKing = isKing;
        }
    }
}
