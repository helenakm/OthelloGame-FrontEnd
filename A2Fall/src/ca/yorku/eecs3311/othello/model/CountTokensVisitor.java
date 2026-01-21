package ca.yorku.eecs3311.othello.model;


/**
 * A visitor class that counts the number of tokens for a specified player
 * on an Othello board.
 * 
 * This class implements the  BoardVisitor interface and iterates over
 * the board to count the tokens belonging to the specified player.
 */
public class CountTokensVisitor implements BoardVisitor {
    private char player;
    private int count;
    
    
    /**
     * Constructs a new CountTokensVisitor for a specified player.
     * 
     * @param player the character representing the player (e.g., 'X' or 'O').
     */
    public CountTokensVisitor(char player) {
        this.player = player;
        this.count = 0;
    }

    /**
     * Visits an Othello board and counts the number of tokens for the specified player.
     * 
     * @param board the OthelloBoard to be visited.
     */
    @Override
    public void visit(OthelloBoard board) {
        this.count = 0;
        for (int row = 0; row < board.getDimension(); row++) {
            for (int col = 0; col < board.getDimension(); col++) {
                if (board.get(row, col) == player) {
                    this.count++;
                }
            }
        }
    }
    
    /**
     * Returns the total count of tokens for the specified player.
     * 
     * @return the count of tokens.
     */
    public int getCount() {
        return count;
    }
}
