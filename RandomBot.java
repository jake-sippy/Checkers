import java.util.*;

public class RandomBot implements Player {
    private Board board;
    private Random r;

    public RandomBot(Board board) {
        this.board = board;
        this.r = new Random();
    }

    public Move getMove() {
        List<Move> moves = new ArrayList<>(board.getLegalMoves());
        return moves.get(r.nextInt(moves.size()));
    }
}
