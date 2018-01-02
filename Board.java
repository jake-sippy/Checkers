import java.util.*;

/**
 * The backend model for checkers games
 * @author Jake Sippy
 */
public final class Board {
    /*
     * Numbering of the checker board
     *
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

    /**
     * The width of the board
     */
    public static final int WIDTH = 8;

    // Half the width of the board / number of legal spaces per row
    private static final int H_WIDTH = WIDTH / 2;
    // Run expensive tests?
    private static final boolean DEBUG = true;

    // Container for pieces
    private Piece[] board;
    // Current legal jumps, empty if none
    private Set<Move> jumps;
    // Current legal steps, empty if none
    private Set<Move> steps;
    // Which color's turn it is
    private Color turn;

    /**
     * Creates a new board
     */
    public Board() {
        // black always goes first
        this.turn = Color.BLACK;

        int numOfPlaces = (WIDTH * WIDTH) / 2;
        board = new Piece[numOfPlaces];

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


    /**
     * Copy constructor which returns a deep copy of
     * the passed Board
     * @param b the board to be copied
     */
    public Board(Board b) {
        // just copys array, Pieces are immutable
        this.board = new Piece[b.board.length];
        for (int i = 0; i < b.board.length; i++) {
            this.board[i] = b.board[i];
        }

        this.turn = b.turn;

        updateLegalMoves();
    }

    /**
     * Check the representation invariant holds
     * through the use of assert statements
     */
    private void checkRep() {
        assert board != null;
        assert jumps != null;
        assert steps != null;
        assert turn != null;

        // non-constant tests
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

    /**
     * Returns a set of all current legal moves
     * @return a set of all current legal moves
     */
    public Set<Move> getLegalMoves() {
        if (this.jumps.isEmpty()) {
            return this.steps;
        } else {
            return this.jumps;
        }
    }

    /**
     * Helper method that updates the current
     * legal moves
     */
    private void updateLegalMoves() {
        this.jumps = getLegalJumps();
        if (this.jumps.isEmpty()) {
            this.steps = getLegalSteps();
        }
        checkRep();
    }

    //////////////////////// DISGUSTING ENCODING OF RULES //////////////////////////////

    /**
     * Helper method that returns the current legal
     * jumps (moves that kill)
     * @return a set of all legal jumps
     */
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

    /**
     * Helper method that returns the current
     * legal steps (moves that do not kill)
     * @return a set of all legal steps
     */
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

    ////////////////////////// END OF RULES ////////////////////////////////

    /**
     * Gets the width of the board, also accessible through
     * Board.WIDTH
     * @return the width of the board as an int
     */
    public int getWidth() {
        return WIDTH;
    }

    /**
     * Returns which color's turn it currently is
     * @return which color's turn it currently is
     */
    public Color getTurn() {
        return this.turn;
    }

    /**
     * Returns the total number of legal places
     * (good for looping over the board)
     * @return the number of legal places on the board
     */
    public int getNumOfPlaces() {
        return (WIDTH * WIDTH) / 2;
    }

    /**
     * Helper method that returns the piece at the given place
     * which helps when we have the place, rather than index
     * @param place the place on the board whose Piece will be
     *              returned
     * @return Piece at the given place on the board
     * @throws IllegalArgumentException if the place is not on
     *              the board (less than 1 or greater than
     *              getNumOfPlaces())
     */
    private Piece getPiece(int place) {
        checkPlace(place);
        return board[place - 1];
    }

    /**
     * Helper method which checks if the given place is on
     * the board
     * @param place the place on the board to be checked
     * @throws IllegalArgumentException if the place is not on
     *              the board (less than 1 or greater than
     *              getNumOfPlaces())
     */
    private void checkPlace(int place) {
        if (place < 1 || place > getNumOfPlaces())
            throw new IllegalArgumentException("Given place is not on the board");
    }

    /**
     * Returns whether or not there is a piece at the given place
     * on the board
     * @param place the place on the board to check for a piece
     * @return true if there is a piece at the given place on the
     *              board, false otherwise
     * @throws IllegalArgumentException if the place is not on
     *              the board (less than 1 or greater than
     *              getNumOfPlaces())
     */
    public boolean hasPieceAt(int place) {
        checkPlace(place);
        return board[place - 1] != null;
    }

    /**
     * Returns whether or not the piece at the given place is a king
     * @param place the place on the board whose piece will be
     *              checked if it is a king
     * @return true if the piece at the given place is a king,
     *              false otherwise
     * @throws IllegalArgumentException if the place is not on
     *              the board (less than 1 or greater than
     *              getNumOfPlaces())
     * @throws NoSuchElementException if there is no piece at the
     *              given place
     */
    public boolean isKing(int place) {
        checkPlace(place);
        if (!hasPieceAt(place))
            throw new NoSuchElementException("there is no piece at: " + place);
        return board[place - 1].isKing;
    }

    /**
     * Returns the color of the piece at the given place
     * @param place the place of the piece whose color
     *              will be returned
     * @return the color of the piece at the given place
     * @throws IllegalArgumentException if the place is not on
     *              the board (less than 1 or greater than
     *              getNumOfPlaces())
     * @throws NoSuchElementException if there is no piece at the
     *              given place
     */
    public Color getColor(int place) {
        checkPlace(place);
        if (!hasPieceAt(place))
            throw new NoSuchElementException("there is no piece at: " + place);
        return board[place - 1].color;
    }

    /**
     * Returns whether the game is over (there are no
     * moves left)
     * @return true if the game is over, false otherwise
     */
    public boolean gameOver() {
        return getLegalMoves().isEmpty();
    }

    /**
     * Makes the move m, and changes the turn unless the move
     * was a jump and the piece can make another jump
     * @param m the move to be made
     * @throws IllegalArgumentException if m is not a legal
     *              move
     */
    public void move(Move m) {
        checkRep();

        if (!getLegalMoves().contains(m))
            throw new IllegalArgumentException(m + " is not a legal move");

        // can the piece jump again?
        boolean multijump = false;

        // get the move we created, instead of the user passed
        // so that we know if a piece was jumped
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
        if (this.turn == Color.BLACK && end >= getNumOfPlaces() - H_WIDTH) {
            board[end] = board[end].makeKing();
        }

        if (this.turn == Color.RED && end < H_WIDTH) {
            board[end] = board[end].makeKing();
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

    /**
     * Return a numerical estimate of which player is winning,
     * positive is Black, negative is Red, magnitude indicates
     * how strong their lead is
     * @return numerical estimate of the state of the game
     */
    public int getScore() {

        // highest value is winning
        if (gameOver()) {
            if (this.turn == Color.BLACK)   // black loses
                return -999999;
            else                            // red loses
                return 999999;
        }

        int score = 0;
        for (int i = 0; i < getNumOfPlaces(); i++) {
            if (board[i] != null) {
                if (board[i].color == Color.BLACK) {
                    if (board[i].isKing)
                        score += 4;
                    else
                        score += 1;
                } else {
                    if (board[i].isKing)
                        score -= 4;
                    else
                        score -= 1;
                }
            }
        }
        return score;
    }

    /**
     * Private immutable Piece class used in Board to
     * store information about color and king
     * status
     */
    class Piece {
        // Color of the piece
        Color color;
        // Is the piece a king
        boolean isKing;

        /**
         * Creates a new piece of the given color
         * @param color the color of the new Piece
         */
        public Piece(Color color) {
            this.color = color;
            this.isKing = false;
        }

        /**
         * Creates a new Piece of the same color
         * as this, but is a king
         */
        public Piece makeKing() {
            Piece p = new Piece(this.color);
            p.isKing = true;
            return p;
        }
    }
}
