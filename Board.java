import java.util.*;

public final class Board {
    /*
     * |--------------- WIDTH ---------------|
     *
     * [  ] [01] [  ] [02] [  ] [03] [  ] [04]
     *
     * [05] [  ] [06] [  ] [07] [  ] [08] [  ]
     *
     * [  ] [09] [  ] [10] [  ] [11] [  ] [12]
     *
     * [13] [  ] [14] [  ] [15] [  ] [16] [  ]
     *
     * [  ] [17] [  ] [18] [  ] [19] [  ] [20]
     *
     * [21] [  ] [22] [  ] [23] [  ] [24] [  ]
     *
     * [  ] [25] [  ] [26] [  ] [27] [  ] [28]
     *
     * [29] [  ] [30] [  ] [31] [  ] [32] [  ]
     */

    // The width of the board
    public static final int WIDTH = 8;

    // Half the width of the board / number of possible pieces per row
    private static final int H_WIDTH = WIDTH / 2;
    // Run expensive tests
    private static final boolean DEBUG = true;

    // Container for pieces
    private Piece[] board;
    // Current legal jumps, empty if none
    private Set<Move> jumps;
    // Current legal steps, empty if none
    private Set<Move> steps;
    // Which color's turn it is
    private Color turn;

    // Create a new FastBoard
    public Board() {
        this.turn = Color.BLACK;

        int numOfBlackSquares = (WIDTH * WIDTH) / 2;
        board = new Piece[numOfBlackSquares];

        // add black pieces
        for (int i = 0; i < ((H_WIDTH - 1) * (WIDTH / 2)); i++) {
            board[i] = new Piece(Color.BLACK);
        }

        // add red pieces
        for (int i = (H_WIDTH + 1) * H_WIDTH; i < board.length; i++) {
            board[i] = new Piece(Color.RED);
        }

        updateLegalMoves();
        // checkRep included in updateLegalMoves
    }

    public Board(Board b) {
        this.board = new Piece[b.board.length];
        for (int i = 0; i < b.board.length; i++) {
            this.board[i] = b.board[i];
        }

        this.turn = b.turn;

        updateLegalMoves();
    }

    // Check the representation invariant holds
    private void checkRep() {
        assert board != null;
        assert jumps != null;
        assert steps != null;
        assert turn != null;

        if (DEBUG) {
            // no non-king reds in blacks final row
            for (int i = 0; i < H_WIDTH; i++) {
                if (hasPieceAt(i + 1) && board[i].color == Color.RED)
                    assert board[i].isKing;
            }

            // no non-king blacks in reds final row
            for (int i = getNumOfPlaces() - H_WIDTH; i < getNumOfPlaces(); i++) {
                if (hasPieceAt(i + 1) && board[i].color == Color.BLACK)
                    assert board[i].isKing;
            }

            // moves seem correct
            for (Move move : getLegalMoves()) {
                assert board[move.getStart()] != null;
                assert board[move.getStart()].color == this.turn;
                assert board[move.getEnd()] == null;
                assert Math.abs(move.getStart() - move.getEnd()) <= WIDTH + 1;
            }
        }
    }

    //////////////////////// RULES ///////////////////////////////

    public Set<Move> getLegalMoves() {
        if (this.jumps.isEmpty()) {
            return this.steps;
        } else {
            return this.jumps;
        }
    }

    // updates jumps and steps with the current
    // legal moves
    private void updateLegalMoves() {
        this.jumps = getLegalJumps();
        if (this.jumps.isEmpty()) {
            this.steps = getLegalSteps();
        }
        checkRep();
    }

    //////////////////////// JUMPS //////////////////////////////

    // Helper for updateLegalMoves that
    // only checks for jumps
    private Set<Move> getLegalJumps() {
        Set<Move> result = new HashSet<>();

        // loop over all pieces
        int max = getNumOfPlaces();
        for (int i = 1; i <= max; i++) {
            Piece p = getPiece(i);

            // only look at pieces whose turn it is
            if (p != null && p.color == this.turn) {
                int type = (i - 1) % WIDTH;
                // black pieces (or kings) that are not on Red's last two rows
                if ( (p.color == Color.BLACK || p.isKing) && i <= max - WIDTH) {

                    // no left edge pieces
                    if (type != 0 && type != H_WIDTH) {
                        // +7
                        if (!hasPieceAt(i + WIDTH - 1)) {

                            // check that theres a piece to jump
                            if (type < H_WIDTH) {   // right shifted row
                                if (hasPieceAt(i + H_WIDTH) && getColor(i + H_WIDTH) != this.turn)
                                    result.add(new Move(i, i + WIDTH - 1, i + H_WIDTH));
                            } else {                // left shifted row
                                if (hasPieceAt(i + H_WIDTH - 1) && getColor(i + H_WIDTH - 1) != this.turn)
                                    result.add(new Move(i, i + WIDTH - 1, i + H_WIDTH - 1));
                            }
                        }
                    }


                    // no right edge pieces
                    if (type != H_WIDTH - 1 && type != WIDTH - 1) {
                        // +9
                        if (!hasPieceAt(i + WIDTH + 1)) {

                            // check that there's a piece to jump
                            if (type < H_WIDTH) {
                                if (hasPieceAt(i + H_WIDTH  + 1) && getColor(i + H_WIDTH + 1) != this.turn)
                                    result.add(new Move(i, i + WIDTH + 1, i + H_WIDTH + 1));
                            } else {
                                if (hasPieceAt(i + H_WIDTH) && getColor(i + H_WIDTH) != this.turn)
                                    result.add(new Move(i, i + WIDTH + 1, i + H_WIDTH));
                            }
                        }
                    }
                }

                // red pieces (or kings) that are not on Black's last two rows
                if ( (p.color == Color.RED || p.isKing) && i > WIDTH) {

                    // no right edge pieces
                    if (type != H_WIDTH - 1 && type != WIDTH - 1) {
                        // -7
                        if (!hasPieceAt(i - WIDTH + 1)) {

                            if (type < H_WIDTH) {   // right shifted row
                                if (hasPieceAt(i - H_WIDTH + 1) && getColor(i - H_WIDTH + 1) != this.turn)
                                    result.add(new Move(i, i - WIDTH + 1, i - H_WIDTH + 1));
                            } else {                // left shifted row
                                if (hasPieceAt(i - H_WIDTH) && getColor(i - H_WIDTH) != this.turn)
                                    result.add(new Move(i, i - WIDTH + 1, i - H_WIDTH));
                            }
                        }
                    }


                    // no left edge pieces
                    if (type != 0 && type != H_WIDTH) {
                        // -9
                        if (!hasPieceAt(i - WIDTH - 1)) {
                            if (type < H_WIDTH) {   // right shifted row
                                if (hasPieceAt(i - H_WIDTH) && getColor(i - H_WIDTH) != this.turn)
                                    result.add(new Move(i, i - WIDTH - 1, i -  H_WIDTH));
                            } else {                // left shifted row
                                if (hasPieceAt(i - H_WIDTH - 1) && getColor(i - H_WIDTH - 1) != this.turn)
                                    result.add(new Move(i, i - WIDTH - 1, i - H_WIDTH - 1));
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    /////////////////////////// STEPS //////////////////////////////////

    // Helper for updateLegalMoves that
    // only checks for steps
    private Set<Move> getLegalSteps() {
        Set<Move> result = new HashSet<>();

        int max = getNumOfPlaces();

        for (int i = 1; i <= max; i++) {
            Piece p = getPiece(i);
            if (p != null && p.color == this.turn) {
                int type = (i - 1) % WIDTH;

                // black pieces (or kings) that are not on Red's last row
                if ( (p.color == Color.BLACK || p.isKing) && i <= max - H_WIDTH) {

                    // Always can move +H_WIDTH
                    if (!hasPieceAt(i + H_WIDTH))
                        result.add(new Move(i, i + H_WIDTH));

                    if (type < H_WIDTH - 1) {       // right shifted rows (excluding edge)
                        if (!hasPieceAt(i + H_WIDTH + 1))
                            result.add(new Move(i, i + H_WIDTH + 1));
                    } else if (type > H_WIDTH) {    // left shifted rows (excluding edge)
                        if (!hasPieceAt(i + H_WIDTH - 1))
                            result.add(new Move(i, i + H_WIDTH - 1));
                    }
                }

                // red pieces (or kings) that are not on Black's last row
                if ( (p.color == Color.RED || p.isKing) && i > H_WIDTH) {

                    // Always can move -H_WIDTH
                    if (!hasPieceAt(i - H_WIDTH))
                        result.add(new Move(i, i - H_WIDTH));

                    if (type < H_WIDTH - 1) {       // right shifted rows (excluding edge)
                        if (!hasPieceAt(i - H_WIDTH + 1))
                            result.add(new Move(i, i - H_WIDTH + 1));
                    } else if (type > H_WIDTH) {    // left shifted rows (excluding edge)
                        if (!hasPieceAt(i - H_WIDTH - 1))
                            result.add(new Move(i, i - H_WIDTH - 1));
                    }
                }
            }
        }
        return result;
    }

    /////////////////// END OF RULES ////////////////////////////////

    // Non static reference to WIDTH
    public int getWidth() {
        return WIDTH;
    }

    // return the color turn
    public Color getTurn() {
        return this.turn;
    }

    // Total number of places on the board (good for loops)
    public int getNumOfPlaces() {
        return (WIDTH * WIDTH) / 2;
    }

    // Return the Piece at the given place
    private Piece getPiece(int place) {
        if (place < 1 || place > getNumOfPlaces())
            throw new IllegalArgumentException("Given place not on board");
        return board[place - 1];
    }

    // Throw an exception if the place given
    // is not on the board
    private void checkPlace(int place) {
        if (place < 1 || place > getNumOfPlaces())
            throw new IllegalArgumentException("Given place is not on the board");
    }

    public boolean hasPieceAt(int place) {
        checkPlace(place);
        return board[place - 1] != null;
    }

    public boolean isKing(int place) {
        checkPlace(place);
        return board[place - 1].isKing;
    }

    public Color getColor(int place) {
        checkPlace(place);
        if (!hasPieceAt(place))
            throw new NoSuchElementException("there is no piece at: " + place);
        return board[place - 1].color;
    }

    public boolean gameOver() {
        return getLegalMoves().isEmpty();
    }

    public void move(Move m) {
        checkRep();

        if (!getLegalMoves().contains(m))
            throw new IllegalArgumentException(m + " is not a legal move");

        boolean multijump = false;

        // get the move we created, instead of the user passed
        for (Move move : getLegalMoves()) {
            if (move.equals(m)) {
                m = move;
                break;
            }
        }

        // move the piece
        int start = m.getStart() - 1;
        int end = m.getEnd() - 1;
        board[end] = board[start];
        board[start] = null;

        // remove jumped piece if its a jump
        if (m.isJump()) {
            board[m.getJumped() - 1] = null;

            Set<Move> newJumps = new HashSet<>();

            // check if piece can jump again
            for (Move move : getLegalJumps()) {
                if (move.getStart() == m.getEnd()) {
                    multijump = true;
                    newJumps.add(move);
                }
            }

            if (multijump)
                this.jumps = newJumps;
        }

        // handle making kings
        if ((board[end].color == Color.BLACK && end >= getNumOfPlaces() - H_WIDTH) ||
                (board[end].color == Color.RED && end < H_WIDTH)) {
            board[end].isKing = true;
        }

        // handle multiple jumps
        if (!multijump) {
            swapTurn();
            updateLegalMoves();
        }

    }

    // change whose turn it is
    public void swapTurn() {
        if (this.turn == Color.RED)
            this.turn = Color.BLACK;
        else
            this.turn = Color.RED;
    }
    
    // return a numerical estimate of which player is winning,
    // positive is Black, negative is Red, magnitude indicates
    // how strong their lead is
    public int getScore() {
        int score = 0;
        for (int i = 0; i < getNumOfPlaces(); i++) {
            if (board[i] != null) {
                if (board[i].color == Color.BLACK)
                    score++;
                else
                    score--;
            }
        }
        return score;
    }


    /////////////// Piece Class ///////////////////
    class Piece {
        Color color;
        boolean isKing;

        public Piece(Color color) {
            this.color = color;
            this.isKing = false;
        }
    }
}
