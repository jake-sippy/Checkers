import java.util.*;

public class Board {
    // static int for board width
    public static final int WIDTH = 8;

    // array of checkers pieces (backend for board)
    private Piece[][] board;
    // the color of whose turn it is
    private Color turn;
    // current legal captures moves
    private Set<Move> jumps;
    // current legal non-capture moves
    private Set<Move> steps;

    // constructs a new board
    public Board() {
        this.board = new Piece[WIDTH][WIDTH];
        this.turn = Color.BLACK;

        // Red pieces
        for (int y = 0; y < (WIDTH - 1) / 2; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (x % 2 == y % 2) {
                    board[x][y] = new Piece(Color.RED, false);
                }
            }
        }

        // Black pieces
        for (int y = WIDTH - 1; y > WIDTH / 2; y--) {
            for (int x = 0; x < WIDTH; x++) {
                if (x % 2 == y % 2) {
                    board[x][y] = new Piece(Color.BLACK, false);
                }
            }
        }

        updateLegalMoves();
    }

    // Check if the point is on the board
    public void checkPoint(Point p) {
        if (p.x() >= WIDTH || p.x() < 0) {
            throw new IllegalArgumentException("x-coordinate is out of the range of the board." +
                    "\texpected: (0 - " + (WIDTH - 1) + ")" +
                    "\tgiven: " + p.x());
        }

        if (p.y() >= WIDTH || p.y() < 0) {
            throw new IllegalArgumentException("y-coordinate is out of the range of the board." +
                    "\texpected: (0 - " + (WIDTH - 1) + ")" +
                    "\tgiven: " + p.y());
        }
    }

    // return true if the board has a piece at the given coord
    public boolean hasPiece(Point p) {
        checkPoint(p);
        return getPiece(p) != null;
    }

    // returns the piece at the given point, null if there
    // is none
    private Piece getPiece(Point p) {
        checkPoint(p);
        return board[p.x()][p.y()];
    }

    // return true if the board has a king at the given coord
    public boolean isKing(Point p) {
        checkPoint(p);
        if (hasPiece(p))
            return getPiece(p).isKing;
        return false;
    }

    // return the color of the piece at the given coord
    public Color getColor(Point p) {
        checkPoint(p);
        if (!hasPiece(p))
            throw new NoSuchElementException("No piece at: " + p);
        return getPiece(p).color;
    }

    public Set<Move> getLegalMoves() {
        return this.moves;
    }

    private void updateLegalMoves() {
        this.jumps = getLegalJumps();
        this.steps = getLegalSteps();
    }
    
    private Set<Move> getLegalJumps() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < WIDTH; x++) {
                if (hasPiece(x, y) && getColor(x, y) == turn) {
                    Piece curr = getPiece(x, y);
                    if (curr.color == Color.RED || curr.isKing) {
                         
                    }

                    if (curr.color == Color.BLACK || curr.isKing) {

                    }
                }
            }
        }
    }

    private Set<Move> getLegalSteps() {
        if (this.jumps.isEmpty()) {
            
        } else {
            return jumps.clear();
        }
    }

    public void move(Point start, Point end) {
        checkPoint(start);
        checkPoint(end);
        updateLegalMoves();
    }
    
    public Color getTurn() {
        return this.turn;
    }

    public void swapTurn() {
        if (turn == Color.BLACK) {
            turn = Color.RED;
        } else {
            turn = Color.BLACK;
        }
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
