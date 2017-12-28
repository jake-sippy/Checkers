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
        ScoreTree(board);
        for (int i = 0; i < DEPTH; i++) {
            
        }
    }

    public int getScore() {
        int score = 0;
        for (int i = 1; i <= board.getNumOfPlaces; i++) {
            if (board.hasPieceAt(i)) {
                int mult = 1;
                if  (board.isKing(i))
                    mult = 2;
                
                if (board.getColor(i) == this.color) {
                    score += mult;
                } else {
                    score += mult;
                }
            }
        }
    }

    class ScoreTree {
        int depth;
        int score;
        List<ScoreTree> children;

        ScoreTree(Board b, int depth) {
            this.depth = depth;
        }

        
    }
}
