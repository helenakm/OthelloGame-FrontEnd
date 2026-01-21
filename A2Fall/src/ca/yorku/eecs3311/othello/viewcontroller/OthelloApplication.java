package ca.yorku.eecs3311.othello.viewcontroller;

import java.io.IOException;

import ca.yorku.eecs3311.othello.model.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OthelloApplication extends Application{
    private Othello othello;
    private Button[][] boardButtons; // Board of buttons with images
    private Label statusLabel;
    private OthelloController controller; 
    private Label statusBar;
	
   

    @Override
    public void start(Stage stage) {
        // Initialize Othello game model
        othello = new Othello();
        
        // Set up the root layout
        BorderPane root = new BorderPane();
     // Create a container for both the status bar and status token
        VBox statusContainer = new VBox(); // VBox stacks children vertically
        statusContainer.getChildren().addAll(createStatusBar(), createStatusToken());

        // Add the container to the top of the BorderPane
        root.setTop(statusContainer);
        root.setCenter(createGameBoard()); // Game board in the center
        // Add ComboBox for player selection
        ComboBox<String> player1ComboBox = new ComboBox<>();
        ComboBox<String> player2ComboBox = new ComboBox<>();
        player1ComboBox.getItems().addAll("Human", "Greedy", "Random");
        player2ComboBox.getItems().addAll("Human", "Greedy", "Random");

        // Default selection
        player1ComboBox.getSelectionModel().select("Human");
        player2ComboBox.getSelectionModel().select("Human");

        // Add ComboBox for player choice to the bottom of the screen
        root.setBottom(createControlButtons(player1ComboBox, player2ComboBox));

        // Set up the scene and stage
        Scene scene = new Scene(root);
        stage.setTitle("Othello");
        stage.setScene(scene);
        stage.show();
    }


    // Create a status bar to display the current player's turn
    private Label createStatusBar() {
        statusLabel = new Label("Player 1's Turn");
        return statusLabel;
    }
    
    // Create a status bar to display the current player's turn
    private Label createStatusToken() {
        statusBar = new Label("Player's token count");
        return statusBar;
    }

    // Create the game board (grid of buttons)
    private GridPane createGameBoard() {
        GridPane grid = new GridPane();
        boardButtons = new Button[8][8]; // Assuming an 8x8 Othello board

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Button button = new Button();
                button.setPrefSize(60, 60);
                
                int finalRow = row;
                int finalCol = col;
                button.setOnAction(event -> handleCellClick(finalRow, finalCol));


//                // Optionally set a default graphic (like a blank tile)
//                Image blankImage = new Image("file:src/images/empty.png");
//                ImageView blankView = new ImageView(blankImage);
//                blankView.setFitWidth(50);
//                blankView.setFitHeight(50);
//                button.setGraphic(blankView);

                boardButtons[row][col] = button;
                grid.add(button, col, row);
            }
        }

        return grid;
    }

    // Handle click events on board buttons
    private void handleCellClick(int row, int col) {
        if (othello.move(row, col)) {
            updateBoard(); // Refresh the GUI board
            updateStatusLabel(); // Update the status label
            updateStatus();
        }
    }

    // Update the GUI board based on the game state
    private void updateBoard() {
    	 Image xImage = new Image("file:src/images/black-circle.png");
    	 Image oImage = new Image("file:src/images/shape.png");
   

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                char token = othello.getToken(row, col); // Use getToken from Othello
                Button button = boardButtons[row][col];

                if (token == 'X') {
                    ImageView xView = new ImageView(xImage); // Add X image
                    xView.setFitWidth(40); // Set width of the image
                    xView.setFitHeight(40); // Set height of the image
                    button.setGraphic(xView);
                } else if (token == 'O') {
                    ImageView oView = new ImageView(oImage); // Add O image
                    oView.setFitWidth(40); // Set width of the image
                    oView.setFitHeight(40); // Set height of the image
                    button.setGraphic(oView); // Add the ImageView to the button
                } else {
                    button.setGraphic(null); // Clear the button if it's empty
                }
            }
        }
    }


    // Update the status label to show whose turn it is
    private void updateStatusLabel() {
        if (othello.isGameOver()) {
            char winner = othello.getWinner();
            if (winner == OthelloBoard.EMPTY) {
                statusLabel.setText("Game Over! It's a tie!");
            } else {
                statusLabel.setText("Game Over! Winner: Player " + winner);
            }
        } else {
            statusLabel.setText("Player " + othello.getWhosTurn() + "'s turn");
        }
    }
    
    private void updateStatus() {
        int xCount = othello.countTokens('X');
        int oCount = othello.countTokens('O');
        statusBar.setText("Player X: " + xCount + " | Player O: " + oCount);
    }

    // Create control buttons (restart, undo, save, etc.)
    private GridPane createControlButtons(ComboBox<String> player1ComboBox, ComboBox<String> player2ComboBox) {
        GridPane controlPane = new GridPane();
        Button restartButton = new Button("Restart");
        Button undoButton = new Button("Undo");
        Button redoButton = new Button("Redo");
        Button saveButton = new Button("Save");
        Button loadButton = new Button("Load");
        Button startButton = new Button("Start Game");

        // Handle the start button click to set player types
        startButton.setOnAction(event -> {
            String player1Type = player1ComboBox.getSelectionModel().getSelectedItem();
            String player2Type = player2ComboBox.getSelectionModel().getSelectedItem();

            Player player1 = createPlayer(player1Type, OthelloBoard.P1);
            Player player2 = createPlayer(player2Type, OthelloBoard.P2);

         // Dynamically create the controller based on the player types
            if (player1 instanceof PlayerHuman && player2 instanceof PlayerHuman) {
                controller = new OthelloControllerHumanVSHuman();
            } 
            else if (player1 instanceof PlayerHuman && player2 instanceof PlayerRandom) {
                controller = new OthelloControllerHumanVSRandom();
            } 
            else if (player1 instanceof PlayerHuman && player2 instanceof PlayerGreedy) {
                controller = new OthelloControllerHumanVSGreedy();
            } 
            else if (player1 instanceof PlayerRandom && player2 instanceof PlayerRandom) {
                controller = new OthelloControllerRandomVSRandom();
            } 
            else {
                controller = new OthelloControllerRandomVSGreedy();
            }
            
         // Start the game and update the board and status
            startGame(player1, player2);
            updateBoard();
            updateStatusLabel();
            updateStatus();
        });


        // Restart button logic
        restartButton.setOnAction(event -> {
            othello.reset();
            updateBoard();
            updateStatusLabel();
            updateStatus();
        });

        // Undo button logic
        undoButton.setOnAction(event -> {
            othello.undo();
            updateBoard();
            updateStatusLabel();
            updateStatus();
        });
        
        // redo button logic
        redoButton.setOnAction(event -> {
            othello.redo();
            updateBoard();
            updateStatusLabel();
            updateStatus();
        });
        //save button logic
        saveButton.setOnAction(event -> {
            try {
                othello.saveGame("othello_save.dat");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //load button logic
        loadButton.setOnAction(event -> {
            try {
                othello = Othello.loadGame("othello_save.dat");
                controller = new OthelloControllerHumanVSHuman(); // Reattach controller
                updateBoard();
                updateStatusLabel();
                updateStatus();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        controlPane.add(restartButton, 0, 0);
        controlPane.add(undoButton, 1, 0);
        controlPane.add(redoButton, 2, 0);
        controlPane.add(saveButton, 3, 0);
        controlPane.add(loadButton, 4, 0);
        controlPane.add(startButton, 0, 1);
        controlPane.add(player1ComboBox, 1, 1);
        controlPane.add(player2ComboBox, 2, 1);

        return controlPane;
    }
    
    private Player createPlayer(String playerType, char player) {
        switch (playerType) {
            case "Human":
                return new PlayerHuman(othello, player);
            case "Greedy":
                return new PlayerGreedy(othello, player);
            case "Random":
                return new PlayerRandom(othello, player);
            default:
                throw new IllegalArgumentException("Invalid player type");
        }
    }
    
    // Start the game with the selected players
    private void startGame(Player player1, Player player2) {
    	othello.reset(); // Reset the game model
        updateBoard(); // Update the GUI with the initial state
        updateStatusLabel(); // Update the status label to show who's turn it is
        updateStatus();
        
        // Pass the players to the controller for game logic
        controller.setPlayers(player1, player2); 
    }


    public static void main(String[] args) {
        launch(args); // Start the JavaFX application
    }
}


