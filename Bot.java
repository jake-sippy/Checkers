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
        GameTree t = new GameTree(this.board);
        //System.out.println(t);
        //List<Move> moves = new ArrayList<>(board.getLegalMoves());
        //Random r = new Random();
        //return moves.get(r.nextInt(moves.size()));
        return t.getMove();
    }

    class GameTree {
        private static final int MAX_DEPTH = 5;
        
        int value;
        Board board;
        Move move;
        List<GameTree> children;
        Random r;

        public GameTree(Board board) {
            this(board, null, 1);
        }

        private GameTree(Board board, Move move, int depth) {
            this.value = 0;
            this.board = board;
            this.move = move;
            this.children = new ArrayList<>();
            this.r = new Random();
            
            if (depth < MAX_DEPTH) {
                for (Move m : board.getLegalMoves()) {
                    Board copy = new Board(board);
                    copy.move(m);
                    children.add(new GameTree(copy, m, depth + 1));
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
            // get all lowest children (base case)
            if (t.children.isEmpty()) {
                t.value = t.board.getScore();
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

                if (t.board.getTurn() == Color.BLACK) { // maximize
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
