package ca.yorku.eecs3311.othello.model;
import ca.yorku.eecs3311.othello.command.*;
import ca.yorku.eecs3311.util.*;
import java.util.Random;
import java.util.Stack;
import java.io.*;

/**
 * Capture an Othello game. This includes an OthelloBoard as well as knowledge
 * of how many moves have been made, whosTurn is next (OthelloBoard.P1 or
 * OthelloBoard.P2). It knows how to make a move using the board and can tell
 * you statistics about the game, such as how many tokens P1 has and how many
 * tokens P2 has. It knows who the winner of the game is, and when the game is
 * over.
 * 
 * See the following for a short, simple introduction.
 * https://www.youtube.com/watch?v=Ol3Id7xYsY4
 * 
 * @author Helena
 *
 */
public class Othello implements Serializable{
	public static final int DIMENSION=8; // This is an 8x8 game

	private OthelloBoard board=new OthelloBoard(Othello.DIMENSION);
	private GameHistory history = new GameHistory();
	private char whosTurn = OthelloBoard.P1;
	private int numMoves = 0;
	private Stack<Othello> undoStack = new Stack<>();
	private Stack<Othello> redoStack = new Stack<>();

	/**
	 * return P1,P2 or EMPTY depending on who moves next.
	 * 
	 * @return P1, P2 or EMPTY
	 */
	public char getWhosTurn() {
		return this.whosTurn;
	}
	
	/**
	 * 
	 * @param row 
	 * @param col
	 * @return the token at position row, col.
	 */
	public char getToken(int row, int col) {
		return this.board.get(row, col);
	}

	/**
	 * Attempt to make a move for P1 or P2 (depending on whos turn it is) at
	 * position row, col. A side effect of this method is modification of whos turn
	 * and the move count.
	 * 
	 * @param row
	 * @param col
	 * @return whether the move was successfully made.
	 */
	public boolean move(int row, int col) {
		if(this.board.move(row, col, this.whosTurn)) {
			saveStateForUndo();
			this.whosTurn = OthelloBoard.otherPlayer(this.whosTurn);
			char allowedMove = board.hasMove();
			if(allowedMove!=OthelloBoard.BOTH)this.whosTurn=allowedMove;
			this.numMoves++;
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 
	 * @param player P1 or P2
	 * @return the number of tokens for player on the board
	 */
	public int getCount(char player) {
		return board.getCount(player);
	}


	/**
	 * Returns the winner of the game.
	 * 
	 * @return P1, P2 or EMPTY for no winner, or the game is not finished.
	 */
	public char getWinner() {
		if(!this.isGameOver())return OthelloBoard.EMPTY;
		if(this.getCount(OthelloBoard.P1)> this.getCount(OthelloBoard.P2))return OthelloBoard.P1;
		if(this.getCount(OthelloBoard.P1)< this.getCount(OthelloBoard.P2))return OthelloBoard.P2;
		return OthelloBoard.EMPTY;
	}


	/**
	 * 
	 * @return whether the game is over (no player can move next)
	 */
	public boolean isGameOver() {
		return this.whosTurn==OthelloBoard.EMPTY;
	}
	
	/**
	 * Resets the game to its initial state.
	 */
	public void reset() {
	    this.board = new OthelloBoard(Othello.DIMENSION);
	    this.whosTurn = OthelloBoard.P1;
	    this.numMoves = 0;
	    undoStack.clear();
	    redoStack.clear();
	}
	
	/**
	 * Attempts to undo the last move.
	 * @return true if undo was successful, false otherwise.
	 */
	public boolean undo() {
	    if (!undoStack.isEmpty()) {
	        redoStack.push(this.copy());
	        Othello previousState = undoStack.pop();
	        this.board = previousState.board.copy();
	        this.whosTurn = previousState.whosTurn;
	        this.numMoves = previousState.numMoves;
	        this.whosTurn=OthelloBoard.otherPlayer(this.whosTurn);
	        return true;
	    }
	    return false;
	}

	/**
	 * Attempts to redo the last undone move.
	 * @return true if redo was successful, false otherwise.
	 */
	public boolean redo() {
	    if (!redoStack.isEmpty()) {
	        undoStack.push(this.copy());
	        Othello nextState = redoStack.pop();
	        this.board = nextState.board.copy();
	        this.whosTurn = nextState.whosTurn;
	        this.numMoves = nextState.numMoves;
	        return true;
	    }
	    return false;
	}

	/**
	 * Pushes the current game state onto the undo stack.
	 * This should be called after every successful move.
	 */
	private void saveStateForUndo() {
	    undoStack.push(this.copy());
	    redoStack.clear(); // Clear the redo stack as redo is no longer valid after a new move
	}
	
	/**
	 * Saves the current game state to a file.
	 * @param filename the name of the file to save the game state.
	 */
	public void saveGame(String filename) throws IOException {
	    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
	        out.writeObject(this);
	    }
	}


	public static Othello loadGame(String filename) throws IOException, ClassNotFoundException {
	    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
	        return (Othello) in.readObject();
	    }
	}


	/**
	 * 
	 * @return a copy of this. The copy can be manipulated without impacting this.
	 */
	public Othello copy() {
		Othello o= new Othello();
		o.board=this.board.copy();
		o.numMoves = this.numMoves;
		o.whosTurn = this.whosTurn;
		return o;
	}
	
	public int countTokens(char token) {
        CountTokensVisitor visitor = new CountTokensVisitor(token);
        board.accept(visitor);
        return visitor.getCount();
    }
	
	


	/**
	 * 
	 * @return a string representation of the board.
	 */
	public String getBoardString() {
		return board.toString()+"\n";
	}


	/**
	 * run this to test the current class. We play a completely random game. DO NOT
	 * MODIFY THIS!! See the assignment page for sample outputs from this.
	 * 
	 * @param args
	 */
	public static void main(String [] args) {
		Random rand = new Random();


		Othello o = new Othello();
		System.out.println(o.getBoardString());
		while(!o.isGameOver()) {
			int row = rand.nextInt(8);
			int col = rand.nextInt(8);

			if(o.move(row, col)) {
				System.out.println("makes move ("+row+","+col+")");
				System.out.println(o.getBoardString()+ o.getWhosTurn()+" moves next");
			}
		}

	}
}


