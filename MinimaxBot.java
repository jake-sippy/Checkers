import java.util.*;

public class MinimaxBot implements Player {
    private Board board;
    private int maxDepth;

    public MinimaxBot(Board board, int maxDepth) {
        this.board = board;
        this.maxDepth = maxDepth;
    }

    public Move getMove() {
        GameTree t = new GameTree(this.board, this.maxDepth);
        return t.getMove();
    }

    class GameTree {
        private int maxDepth;
        private Color turn;
        private int value;
        private Move move;
        private List<GameTree> children;
        private Random r;

        public GameTree(Board board, int maxDepth) {
            this(board, maxDepth, null);
        }

        private GameTree(Board board, int maxDepth, Move move) {
            this.maxDepth = maxDepth;
            this.turn = board.getTurn();
            this.value = board.getScore();
            this.move = move;
            this.children = new ArrayList<>();
            this.r = new Random();

            if (maxDepth > 0) {
                for (Move m : board.getLegalMoves()) {
                    Board copy = new Board(board);
                    copy.move(m);
                    children.add(new GameTree(copy, maxDepth - 1, m));
                }
            }
        }

        public Move getMove() {
            fillValues(this);
            List<Move> best = new ArrayList<>();
            for (GameTree child : children) {
                if (child.value == this.value)
                    best.add(child.move);
            }

            return best.get(r.nextInt(best.size()));
        }

        private void fillValues(GameTree t) {
            // base case
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

        @Override
        public String toString() {
            return toString(1);
        }

        private String toString(int depth) {
            String result = "\n" + depth + ": " + board.getScore();
            for (GameTree child : children) {
                result += child.toString(depth + 1);
            }
            return result;
        }


    }
}
