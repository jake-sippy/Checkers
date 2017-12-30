public interface Bot {
    // Return next move, must be in board.getLegalMoves()
    public Move getMove();
}
