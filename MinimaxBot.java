import java.util.*;

/**
 * An implementation of Player which uses the
 * minimax algorithm (limited to a maximum search
 * depth) to choose moves
 */
public class MinimaxBot implements Player {
    // The board being played on
    private Board board;

    /**
     * Creates a new MinimaxBot with a reference
     * to the board being played on
     * @param board the board being played on
     */
    public MinimaxBot(Board board) {
        this.board = board;
    }

    /**
     * Returns a legal move
     * @return a legal move
     */
    public Move getMove() {
        GameTree t = new GameTree(this.board);
        return t.getMove();
    }

    /**
     * Inner class that holds a structure
     * of the game and how it will change
     * as moves are made
     */
    class GameTree {
        private static final int MAX_DEPTH = 7;
        private Color turn;
        private int value;
        private Move move;
        private List<GameTree> children;
        private Random r;

        /**
         * Creates a new GameTree with reference to the
         * given board
         */
        public GameTree(Board board) {
            this(board, null, 1);
        }

        /**
         * Creates a new GameTree with reference to the
         * current board, the previous move, and this
         * subtree's current depth in the overall tree
         */
        private GameTree(Board board, Move move, int depth) {
            this.turn = board.getTurn();
            this.value = board.getScore();
            this.move = move;
            this.children = new ArrayList<>();
            this.r = new Random();

            // keep making more subtrees until we reach MAX_DEPTH
            if (depth < MAX_DEPTH) {
                for (Move m : board.getLegalMoves()) {
                    Board copy = new Board(board);
                    copy.move(m);
                    children.add(new GameTree(copy, m, depth + 1));
                }
            }
        }

        /**
         * Returns a random move that minimizes the
         * maximum possible score the opposing player
         * could get in MAX_DEPTH number of steps
         * @return a move decided by minimax limited to
         *          MAX_DEPTH steps
         */
        public Move getMove() {
            fillValues(this);
            List<Move> best = new ArrayList<>();
            for (GameTree child : children) {
                if (child.value == this.value)
                    best.add(child.move);
            }

            return best.get(r.nextInt(best.size()));
        }

        /**
         * Helper method for getMove which evaluates
         * the score of the board at each step in the
         * tree
         * @param t the root of the GameTree to fill
         *          values in
         */
        private void fillValues(GameTree t) {
            // get all lowest children (base case)
            if (t.children.isEmpty()) {
                return;
            }

            for (GameTree child : t.children) {
                fillValues(child);
            }

            boolean firstValue = true;
            // choose a random max or min
            for (GameTree child : t.children) {

                if (firstValue) {
                    t.value = child.value;
                    firstValue = false;
                }

                if (t.turn == Color.BLACK) {            // maximize
                    if (child.value > t.value)
                        t.value = child.value;
                } else {                                // minimize
                    if (child.value < t.value)
                        t.value = child.value;
                }
            }
        }
    }
}
