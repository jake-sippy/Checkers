/**
 * Contains a move over a checkers board, God,
 * this really shouldn't be a public class...
 */
public class Move {
    // starting position
    private int start;
    // ending position
    private int end;
    // the position of the piece jumped (optional)
    // this really should be handled in the Board class,
    // but is much simpler to do here
    private int jumped;

    /**
     * Creates a new Move from start to end
     * @param start the starting position of the move
     * @param end the ending position of the move
     * @throws IllegalArgumentException if start or end
     *          are not legal positions on the board
     */
    public Move(int start, int end) {
        this(start, end, -1);
    }

    /**
     * Creates a new Move from start to end
     * jumping the piece at position jumped
     * @param start the starting position
     * @param end the ending position
     * @param jumped the position of the jumped piece
     *              less than 1 if none
     * @throws IllegalArgumentException if start or end
     *          are not legal positions on the board, and
     *          if jumped is greater than the number of places
     *          on the board
     */
    public Move(int start, int end, int jumped) {
        int max = (Board.WIDTH * Board.WIDTH) / 2;
        this.start = start;
        this.end = end;
        this.jumped = jumped;
        if (start < 1 || end < 1  || start > max || end > max || jumped > max)
            throw new IllegalArgumentException(this.toString() + " is off the board, max = " + max);
    }
    
    /**
     * Returns the starting position of the move
     * @return the starting position of the move
     */
    public int getStart() {
        return this.start;
    }

    /**
     * Returns the ending position of the move
     * @return the ending position of the move
     */
    public int getEnd() {
        return this.end;
    }

    /**
     * Returns the position of jumped piece of the move,
     *          less than 0 if there is none
     * @return the position of jumped piece of the move,
     *          less than 0 if there is none
     */
    public int getJumped() {
        return this.jumped;
    }
    
    /**
     * Returns whether or not the move is a jump
     * @return whether or not the move is a jump
     */
    public boolean isJump() {
        return this.jumped > 0;
    }

    /**
     * Returns a string representation of the move
     * @return a string representation of the move
     */
    @Override
    public String toString() {
        String result = "(";
        if (isJump())
            result += "jump: ";
        else
            result += "step: ";
        result += start + ", " + end + ")";
        return result;
    }

    /**
     * Returns whether or not the move equals the given object
     * equality is defined as equal starts and ends, jumped position
     * is ignored because this is a garbage class
     * @param o the object to be compared for equality
     * @return true if the object is equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Move))
            return false;
        Move m = (Move) o;
        return (this.start == m.start && this.end == m.end);
    }

    /**
     * Returns a hashCode for this move
     * @return a hashCode for this move
     */
    @Override
    public int hashCode() {
        return (int) Math.pow(this.start, 31) * this.end;
    }
}
