public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point))
            return false;
        Point p = (Point) o;
        return this.x == p.x && this.y == p.y;
    }

    @Override 
    public int hashCode() {
        return (int) Math.pow(this.x, 31) * this.y;
    }
}
