package ca.yorku.eecs3311.othello.command;

import java.io.Serializable;
import java.util.Stack;

/**
 * A class to manage the history of moves for undo and redo functionality.
 */
public class GameHistory implements Serializable{
    private final Stack<Command> undoStack;
    private final Stack<Command> redoStack;

    /**
     * Constructs a new GameHistory.
     */
    public GameHistory() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    /**
     * Executes a command and adds it to the undo stack.
     *
     * @param command the command to execute.
     */
    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Clear the redo stack when a new command is executed
    }

    /**
     * Undoes the last command, if possible.
     */
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    /**
     * Redoes the last undone command, if possible.
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }
}
