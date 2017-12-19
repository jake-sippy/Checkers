public class Move {
    private Point start;
    private Point end;

    public Move(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Move))
            return false;
        Move m = (Move) o;
        return this.start.equals(m.start) &&
            this.end.equals(m.end);
    }

    @Override
    public int hashCode() {
        return this.start.hashCode() * this.end.hashCode();
    }
}
