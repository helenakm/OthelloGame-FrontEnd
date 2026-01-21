package ca.yorku.eecs3311.othello.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A visitor class that determines the valid moves for a specified player
 * on an Othello board.
 * 
 * This class implements the BoardVisitor interface and identifies
 * all valid moves available to the specified player.
 */
public class ValidMovesVisitor implements BoardVisitor {
	
	 //The character representing the player whose valid moves will be calculated.
    private char player;
    //A list of valid moves for the specified player, where each move is represented
    //as an array of two integers: {row, column}.
    private List<int[]> validMoves;

   
    /**
     * Constructs a new {@code ValidMovesVisitor} for a specified player.
     * 
     * @param player the character representing the player (e.g., 'X' or 'O').
     */
    public ValidMovesVisitor(char player) {
        this.player = player;
        this.validMoves = new ArrayList<>();
    }

    /**
     * Visits an Othello board and determines all valid moves for the specified player.
     * A valid move is identified by checking if a move in any direction
     * satisfies the game's rules.
     * 
     * @param board the OthelloBoard to be visited.
     */
    @Override
    public void visit(OthelloBoard board) {
        validMoves.clear();
        for (int row = 0; row < board.getDimension(); row++) {
            for (int col = 0; col < board.getDimension(); col++) {
                for (int drow = -1; drow <= 1; drow++) {
                    for (int dcol = -1; dcol <= 1; dcol++) {
                        if (board.hasMove(row, col, drow, dcol) == player) {
                            validMoves.add(new int[] { row, col });
                            break;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Returns the list of valid moves for the specified player.
     * Each move is represented as an array of two integers: {row, column}.
     * 
     * @return the list of valid moves.
     */
    public List<int[]> getValidMoves() {
        return validMoves;
    }
}
