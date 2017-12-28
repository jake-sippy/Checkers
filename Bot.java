public class Bot {
    private Board board;
    private Color color;

    public Bot(Board board, Color color) {
        this.board = board;
        this.color = color;
    }

    public Move getMove() {
        for (Move m : board.getLegalMoves())
            return m;
        return null;
    }
}
