import java.util.*;

/**
 * Implementation of Player that simply returns a
 * random legal move
 */
public class RandomBot implements Player {
    // the board being played on
    private Board board;
    // Random object for chosing random moves
    private Random r;

    /**
     * Creates a new RandomBot with a reference
     * to the board being played on
     * @param board the board being played on
     */
    public RandomBot(Board board) {
        this.board = board;
        this.r = new Random();
    }

    /**
     * Returns a random legal move
     * @return a random legal move
     */
    public Move getMove() {
        List<Move> moves = new ArrayList<>(board.getLegalMoves());
        return moves.get(r.nextInt(moves.size()));
    }
}
