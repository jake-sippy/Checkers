import java.util.*;

/**
 * Implementation of Player that uses alpha-beta
 * pruning to make decisions. This has an advantage
 * over simple minimax implementations, as it can search much
 * deeper in the same amount of time.
 */
public class PruningBot implements Player {
    // max depth of the game-tree
    // this can be larger than simple minimax implementations
    private static final int MAX_DEPTH = 10;

    // reference to the current board
    private Board board;
    // map from utility values to legal moves
    private Map<Integer, List<Move>> map;
    // object to make random decision when multiple
    // moves have the same value
    private Random r;

    /**
     * Creates a new instance of PruningBot with a reference
     * to the current state of the board
     * @param board the current state of the board
     */
    public PruningBot(Board board) {
        this.board = board;
        this.map = new HashMap<>();
        this.r = new Random();
    }

    /**
     * Returns a legal move on the current board, as chosen
     * using the alpha-beta pruning algorithm on a game tree 
     * of depth MAX_DEPTH. 
     * @return a legal move on the current board
     */
    public Move getMove() {
        if (board.getLegalMoves().size() == 1)
            return board.getLegalMoves().iterator().next();

        map.clear();

        int v = alphabeta(board, Integer.MIN_VALUE, Integer.MAX_VALUE, MAX_DEPTH);
       
        return map.get(v).get(r.nextInt(map.get(v).size()));
    }

    /**
     * Helper method for getMove() that builds the game tree
     * and runs alpha-beta on it to return the optimum utility
     * value of current moves. Also maps utility values to the
     * current moves that result in those values.
     * @param board the current state of the board
     * @param alpha the minimum bound on utility values to continue
     *              building tree on
     * @param beta the maximum bound on utility values to continue
     *              building tree on
     * @param depth the maximum depth of the tree to build
     * @return the utility value of the optimum legal moves on
     *              board
     */
    private int alphabeta(Board board, int alpha, int beta, int depth) {
        if (depth == 1 || board.gameOver()) // terminal case
            return board.getScore();

        // black aims to maximize the score of the board
        boolean max = (board.getTurn() == Color.BLACK);
       
        // best is meant to be overriden
        int best;
        if (max)
            best = Integer.MIN_VALUE;
        else
            best = Integer.MAX_VALUE;

        // set best to be the optimum utlity value of child game states
        for (Move m : board.getLegalMoves()) {
            // recursivly find utility values of child states
            Board next = new Board(board, m);
            int v = alphabeta(next, alpha, beta, depth - 1);

            // fill map for the original passed board
            if (depth == MAX_DEPTH) {
                if (!map.containsKey(v))
                    map.put(v, new ArrayList<>());
                map.get(v).add(m);
            }

            if (max) {
                best = Math.max(best, v);
                
                if ((next.getTurn() != board.getTurn()) && v >= beta)
                    return Integer.MAX_VALUE;
                alpha = Math.max(alpha, v);

            } else {
                best = Math.min(best, v);
                
                if ((next.getTurn() != board.getTurn()) && v <= alpha)
                    return Integer.MIN_VALUE;
                beta = Math.min(beta, v);
            }
        }

        return best;
   }
}

