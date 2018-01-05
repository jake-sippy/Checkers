import java.util.*;

/**
 * Implementation of the Player interface that
 * uses a decision tree and a truncated minimax
 * algorithm to find a move
 */
public class MinimaxBot implements Player {
    // Limit on the depth of the decision tree (gets slow above 7)
    private static final int MAX_DEPTH = 7;
    
    // The board being played on
    private Board board;
    // Trashy field to keep track of the value of node
    private Map<Integer, List<Move>> map;
    // Used for deciding between equally valued moves
    private Random r;

    /**
     * Create a new instance of MinimaxBot with a
     * reference to the current board being played
     * on
     * @param board the board being played on
     */
    public MinimaxBot(Board board) {
        this.board = board;
        this.map = new HashMap<>();
        this.r = new Random();
    }

    /**
     * Returns a legal move on the board
     * passed in the constructor
     * @return a legal move
     */
    public Move getMove() {
        map.clear();

        int v = minimax(board, MAX_DEPTH);
       
        return map.get(v).get(r.nextInt(map.get(v).size()));
    }

    /**
     * Helper method for getMove() that builds and
     * searches a game tree of fixed depth, returning
     * a utility value of the best possible path
     * for the current board. Simultaneously, the
     * function fills this.map with utility values
     * to the current legal moves that result in those
     * values.
     * @param board the current state of the board
     * @param depth the maximum depth of the game tree
     * @return a utility value corresponding to the
     *          best possible path
     */
    private int minimax(Board board, int depth) {
        if (depth == 1 || board.gameOver())
            return board.getScore();
        
        // Black aims to maximize the score of the board
        boolean max = (board.getTurn() == Color.BLACK);

        // best is meant to be overriden (fenceposting)
        int best;
        if (max)
            best = Integer.MIN_VALUE;
        else
            best = Integer.MAX_VALUE;

        // recursively find best value among children
        for (Move m : board.getLegalMoves()) {
            int v = minimax(new Board(board, m), depth - 1);

            if (depth == MAX_DEPTH) {
                if (!map.containsKey(v))
                    map.put(v, new ArrayList<Move>());
                map.get(v).add(m);
            }

            if (max)
                best = Math.max(best, v);
            else
                best = Math.min(best, v);
        }

        return best;
    }
}

