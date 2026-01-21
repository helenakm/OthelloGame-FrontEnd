package ca.yorku.eecs3311.othello.command;

public interface Command {
	
	//Executes the command
    void execute();
    //undo the command
    void undo();
}

