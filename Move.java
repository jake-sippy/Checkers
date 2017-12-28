public class Move {
    private int start;
    private int end;
    private int jumped;


    public Move(int start, int end) {
        this(start, end, -1);
    }

    public Move(int start, int end, int jumped) {
        int max = (Board.WIDTH * Board.WIDTH) / 2;
        this.start = start;
        this.end = end;
        this.jumped = jumped;
        if (start < 1 || end < 1  || start > max || end > max)
            throw new IllegalArgumentException(this.toString() + " is off the board, max = " + max);
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    public int getJumped() {
        return this.jumped;
    }

    public boolean isJump() {
        return this.jumped > 0;
    }

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Move))
            return false;
        Move m = (Move) o;
        return (this.start == m.start && this.end == m.end);
    }

    @Override
    public int hashCode() {
        return (int) Math.pow(this.start, 31) * this.end;
    }
}
