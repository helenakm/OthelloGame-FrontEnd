package ca.yorku.eecs3311.othello.command;

import ca.yorku.eecs3311.othello.model.OthelloBoard;

/**
 * A command to execute and undo a player's move on the Othello board.
 */
public class MoveCommand implements Command {
    private final OthelloBoard board;
    private final int row;
    private final int col;
    private final char player;

    /**
     * Constructs a new MoveCommand.
     *
     * @param board  the Othello board.
     * @param row    the row of the move.
     * @param col    the column of the move.
     * @param player the player making the move.
     */
    public MoveCommand(OthelloBoard board, int row, int col, char player) {
        this.board = board;
        this.row = row;
        this.col = col;
        this.player = player;
    }

    @Override
    public void execute() {
        board.move(row, col, player); // Executes the move
    }

    @Override
    public void undo() {
        board.removeToken(row, col); // Removes the token to undo the move
    }
}
