import java.util.*;

public class Bot {
    private static final int DEPTH = 3;

    private Board board;
    private Color color;

    public Bot(Board board, Color color) {
        this.board = board;
        this.color = color;
    }

    public Move getMove() {
        List<Move> moves = new ArrayList<>(board.getLegalMoves());
        Random r = new Random();
        return moves.get(r.nextInt(moves.size()));
    }
}
