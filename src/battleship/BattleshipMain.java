package battleship;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import battleship.controller.GameLayoutController;
import battleship.controller.GameScreenController;
import battleship.model.Board;
import battleship.model.Board.Cell;
import battleship.model.Ship;
import battleship.network.Client;
import battleship.network.NetworkConnection;
import battleship.util.Util;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

// TODO: Auto-generated Javadoc
/**
 * The Class BattleshipMain.
 *
 * @author Vsu Chuchra, Nitin Nanda, Simarpreet Kaur Jabbal, Vrind Gupta, Ayush
 *         Arya
 */
public class BattleshipMain extends Application {

	Serialization seri = new Serialization();

	Serialization loadSeriObj;
	/** The start screen. */
	private BorderPane startScreen;
	private int enemyShips = 5;
	/**
	 * GameLayoutController object
	 */
	public GameLayoutController gameLayoutController = new GameLayoutController();

	/**
	 * GameScreenController object
	 */
	public GameScreenController gameScreenController = new GameScreenController();

	/**
	 * Util Class Object
	 */
	Util util = new Util();

	/**
	 * Denotes if ship drop was a success or a failure
	 */
	boolean success = false;

	/**
	 * An array of number and size of ships
	 */
	public int[] ships = { 5, 4, 3, 3, 2 };

	/**
	 * This variable stores the total player time in a game session.
	 */
	public double totalPlayerTime = 0;

	/**
	 * This variable stores the total game time in a game session including the time
	 * taken by both the players, human/computer.
	 */
	public double globalTimeVar = 0;

	/**
	 * This variable stores the time a player uses to take his/her turn.
	 */
	public double playerTimeVar = 0;

	/**
	 * The TimerTask class helps in creating a separate task thread which will be
	 * called automatically as described by some time interval in the schedule
	 * method.
	 */
	public TimerTask gameTimerTask;

	/**
	 * The TimerTask class helps in creating a separate task thread which will be
	 * called automatically as described by some time interval in the schedule
	 * method.
	 */
	public TimerTask playerTimerTask;

	private boolean loadGame = false;

	private String localGamplay;
	private String enemyValue;
	/**
	 * Formatting the Timer variables to single digit precision
	 */
	DecimalFormat df = new DecimalFormat("#.#");

	/** The ship index. */
	private int shipIndex = 0;

	private int finalScore;
	/**
	 * Set true if the player can shoot the enemy Set to false when the grids are
	 * not created. Also prevents the player to shoot its own ships
	 */
	private boolean running = true;
	private boolean isMyTurn = false;
	private boolean playerReady;
	private boolean enemyReady;
	private SimpleBooleanProperty isEnemyTurn = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty isPlaying = new SimpleBooleanProperty(false);
	/** The enemy board. */
	private Board enemyBoard;
	private boolean isServerPlay = false;
	/**
	 * A string which specifies the selected gameplay
	 */
	private String GAME_PLAY_NORMAL = "NORMAL";

	/**
	 * A string which specifies the selected gameplay
	 */
	private String GAME_PLAY_SALVA = "SALVA";

	/**
	 * A string which specifies the selected gameplay
	 */
	private String ENEMY_COMPUTER = "COMPUTER";

	/**
	 * A string which specifies the selected gameplay
	 */
	private String ENEMY_PLAYER = "PLAYER";

	/**
	 * Stores the number of turns left for the enemy based on the number of ships
	 * alive
	 */
	private int playerTurns;

	/**
	 * Stores the number of turns left for the enemy based on the number of ships
	 * alive
	 */
	private int enemyTurns;

	/** The player board. */
	private Board playerBoard;

	/**
	 * Stores the cell that has been shot
	 */
	private Cell shotCell = null;

	/**
	 * Stores if a ship has been found on the player board using a 2-D array
	 */
	private boolean playerBoardVisited[][] = new boolean[10][10];
	private List<ArrayList<Integer>> shipList = new ArrayList<>();

	private boolean enemyBoardVisited[][] = new boolean[10][10];
	private List<ArrayList<Integer>> enemyShipList = new ArrayList<>();

	/**
	 * Stores if a ship has been found on the computer board using a 2-D array
	 */
	private boolean shipFoundOnCell[][] = new boolean[10][10];

	/**
	 * Stores the cells which have been shot by the user or the computer in Salva
	 * mode
	 */
	Deque<Cell> shotCellStack = new ArrayDeque<Cell>();

	/** The primary stage. */
	private Stage primaryStage;

	/**
	 * Integer which stores the total number of ships alive currently.
	 */
	private int totalShips = ships.length;

	/** Boolean which sets true if it is enemy's turn. */
	private boolean enemyTurn = false;

	/** The random. */
	private Random random = new Random();
	private NetworkConnection connection = createClient();

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * 
	 * @throws Exception
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		connection.startConnection();
		isPlaying.addListener((Observable o) -> {
			if (isPlaying.getValue()) {
				resetPlayerGlobalTime();
				startGameTimer();
				startPlayerTimer();
				gameLayoutController.setPlayerIndicator(Color.RED);
				gameLayoutController.setEnemyIndicator(Color.GREEN);
				gameLayoutController.setGameEvent("Enemy Turn");
			}
		});

		isEnemyTurn.addListener((Observable o) -> {
			if (isEnemyTurn.getValue()) {
				if (enemyReady) {
					gameLayoutController.setGameEvent("Enemy Turn");

				} else {
					gameLayoutController.setGameEvent("Waiting for enemy");
				}
				gameLayoutController.setEnemyIndicator(Color.GREEN);
				gameLayoutController.setPlayerIndicator(Color.RED);

			} else {
				if (playerReady) {
					gameLayoutController.setGameEvent("Your Turn");

				} else {
					gameLayoutController.setGameEvent("Place your ships");
				}
				gameLayoutController.setEnemyIndicator(Color.RED);
				gameLayoutController.setPlayerIndicator(Color.GREEN);
			}

		});

		this.primaryStage = primaryStage;
		primaryStage.setTitle("Battleship");
		primaryStage.setResizable(false);
		this.primaryStage.getIcons().add(new Image("/battleship/view/images/logo.jpg"));
		initRootLayout(primaryStage);
		primaryStage.setOnCloseRequest(event -> {
			System.exit(0);
			gameTimerTask.cancel();
			playerTimerTask.cancel();
		});
	}

	/**
	 * Inits the root layout.
	 * 
	 * @param primaryStage the primary stage
	 * @throws InterruptedException the interrupted exception
	 */
	public void initRootLayout(Stage primaryStage) throws InterruptedException {
		try {

			/**
			 * Load root layout from fxml file.
			 */
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(BattleshipMain.class.getResource("/battleship/view/StartScreen.fxml"));
			startScreen = (BorderPane) loader.load();

			/**
			 * Show the scene containing the root layout.
			 */
			Scene startScene = new Scene(startScreen);
			primaryStage.setScene(startScene);
			showGameScreen(primaryStage);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Show game screen. Initial View which offers the player various options such
	 * as to play a game, view rules, scores and exit the game
	 * 
	 * @param primaryStage the primary stage
	 */
	public void showGameScreen(Stage primaryStage) {
		try {

			/**
			 * Load the fxml file and provide relative path of the file.
			 */
			FXMLLoader loader = new FXMLLoader();
			loader.setController(gameScreenController);
			gameScreenController.setMainApp(this);
			loader.setLocation(BattleshipMain.class.getResource("/battleship/view/GameScreen.fxml"));
			AnchorPane gameScreen = (AnchorPane) loader.load();

			startScreen.setCenter(gameScreen);
			/**
			 * Sets a controller for the loaded fxml file.
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the computer content.
	 * 
	 * Creates the battleship grid for the computer
	 * 
	 * @return the parent
	 */
	public Parent createEnemyContent(AnchorPane root) {
		enemyShipList = new ArrayList<>();
		if (localGamplay.equals(GAME_PLAY_SALVA)) {
			playerTurns = 5;
		} else if (localGamplay.equals(GAME_PLAY_NORMAL)) {
			playerTurns = 1;
		}

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				playerBoardVisited[i][j] = false;
			}
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				enemyBoardVisited[i][j] = false;
			}
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				shipFoundOnCell[i][j] = false;
			}
		}
		enemyBoard = new Board(true, event -> {
			System.out.println("isMyTurn && isPlaying.getValue() server " + (isMyTurn && isPlaying.getValue()));
			if (!isMyTurn || (isMyTurn && !isPlaying.getValue())) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Important Message");
				alert.setHeaderText("Not Allowed");
				alert.initOwner(primaryStage);
				alert.setContentText("Wait for game to start!");
				alert.showAndWait();
				return;
			}
			Cell cell = (Cell) event.getSource();
			enemyBoardVisited[cell.x][cell.y] = true;
			/**
			 * gives an alert if the cell was already shot by the player
			 */
			if (cell.wasShot) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Important Message");
				alert.setHeaderText("Not Allowed");
				alert.initOwner(primaryStage);
				alert.setContentText("Cell already shot !!");
				alert.showAndWait();
				return;
			}
			if (isServerPlay) {

				try (StringWriter messageWriter = new StringWriter()) {
					messageWriter.append("shoot" + "_" + cell.x + "_" + cell.y);
					System.out.println("messageWriter " + messageWriter.toString());
					System.out.println("connection " + connection);
					connection.send(messageWriter.toString());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Failed to send\n");
				}

			} else {

				if (!cell.shoot()) {
					playerTurns--;
				}
				if (playerTurns == 0) {
					enemyTurn = true;
				}
			}

			/**
			 * Displays a messaage if the player wins by destroying all ships of customer
			 */
			if (enemyBoard.ships == 0) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Important Message");
				alert.setHeaderText("Congratulations");
				alert.initOwner(primaryStage);
				alert.setContentText("YOU WIN. You took " + df.format(totalPlayerTime) + " seconds to play");
				gameTimerTask.cancel();
				playerTimerTask.cancel();
				alert.showAndWait();
				restartGameScreen();
			}

			if (enemyTurn) {
				totalPlayerTime = totalPlayerTime + playerTimeVar;
				resetPlayerTimer();
				enemyMove();
			}
		});

		if (loadGame) {
			enemyBoardVisited = loadSeriObj.getEnemyBoardVisited();
			int counterFOrShips3 = 0;
			enemyShipList = loadSeriObj.getEnemyShipList();
			for (ArrayList<Integer> shipList : enemyShipList) {
				System.out.println("Place enemy shuiips " + loadGame);
				Ship ship = new Ship(shipList.get(3), shipList.get(2) == 0 ? true : false);
				if (ship.type == 5) {
					gameLayoutController.setShip5ImageView(null);
					gameLayoutController.setShip5Dropped(true);
				} else if (ship.type == 4) {
					gameLayoutController.setShip4ImageView(null);
					gameLayoutController.setShip4Dropped(true);
				} else if (ship.type == 3) {
					counterFOrShips3++;
					if (counterFOrShips3 == 1) {
						gameLayoutController.setShip3ImageView1(null);
						gameLayoutController.setShip3Dropped1(true);
					} else {
						gameLayoutController.setShip3ImageView2(null);
						gameLayoutController.setShip3Dropped2(true);
					}
				} else if (ship.type == 2) {
					gameLayoutController.setShip2ImageView(null);
					gameLayoutController.setShip2Dropped(true);
				}
				boolean test = enemyBoard.placeShip(ship, shipList.get(0), shipList.get(1));
				System.out.println("Test " + test);
			}
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					if (enemyBoardVisited[i][j]) {
						enemyBoard.getCell(i, j).shoot();
					}
				}
			}
		} else {
			shotCellStack = new ArrayDeque<Cell>();
		}
		HBox enemyBox = new HBox(enemyBoard);
		enemyBox.setAlignment(Pos.CENTER);

		root.getChildren().add(enemyBox);
		root.getChildren().get(0).setTranslateX(-50);
		root.getChildren().get(0).setTranslateY(-60);

		root.getChildren().add(new Label("Enemy"));
		root.getChildren().get(1).setTranslateX(199);
		return root;
	}

	/**
	 * Creates the player content.
	 *
	 * @return the parent
	 */
	public Parent createPlayerContent(AnchorPane root) {
	
		if (enemyValue.equals(ENEMY_PLAYER)) {
			isServerPlay = true;
		}
		gameLayoutController.setGameEvent("Wait for Enemy");
		/**
		 * Creates the pla yer battleship grid. *
		 */
		playerBoard = new Board(false, event -> {
			if (!isMyTurn) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Important Message");
				alert.setHeaderText("Not Allowed");
				alert.initOwner(primaryStage);
				alert.setContentText(isPlaying.getValue() ? "You cannot shoot your own ships !!" : "wait for enemy");
				alert.showAndWait();
				return;
			}

			Dragboard db = ((DragEvent) event).getDragboard();
			Cell cell = (Cell) event.getSource();

			String[] shipData = db.getString().split("_");
			Ship ship = new Ship(Integer.parseInt(shipData[0]),
					String.valueOf(MouseButton.PRIMARY).equalsIgnoreCase(shipData[1]));
			/**
			 * places the ships horizontally or vertically as per the users input
			 */
			if (isServerPlay) {
				if (playerBoard.placeShip(ship, cell.x, cell.y)) {

					try (StringWriter messageWriter = new StringWriter()) {
						messageWriter.append("playerShip" + "_" + cell.x + "_" + cell.y + "_" + shipData[0] + "_"
								+ String.valueOf(MouseButton.PRIMARY).equalsIgnoreCase(shipData[1]));
						System.out.println("messageWriter " + messageWriter.toString());
						System.out.println("connection " + connection);
						connection.send(messageWriter.toString());
						System.out.println("Player ship placed on enemy board");
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Failed to send\n");
					}
					success = true;
					((DragEvent) event).setDropCompleted(success);
					event.consume();
					if (--totalShips == 0) {
						playerReady = true;
						isEnemyTurn.set(true);
						running = true;
						isPlaying.set(true);
						isMyTurn = false;
					}
				}
			} else {
				if (playerBoard.placeShip(ship, cell.x, cell.y)) {
					ArrayList<Integer> shipValues = new ArrayList<>();
					shipValues.add(cell.x);
					shipValues.add(cell.y);
					shipValues.add(ship.vertical ? 0 : 1);
					shipValues.add(ship.type);
					shipList.add(shipValues);
					success = true;
					((DragEvent) event).setDropCompleted(success);
					event.consume();
					if (--totalShips == 0) {
						startGame();
					}
				}
			}

		});
		playerBoard.setOnDragOver(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				handleDragOver(event);
			}
		});
		playerBoard.setOnDragDropped(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				handleDragDropped(event);
			}
		});
		playerBoard.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (running) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Important Message");
					alert.setHeaderText("Not Allowed");
					alert.initOwner(primaryStage);
					alert.setContentText(
							isPlaying.getValue() ? "You cannot shoot your own ships !!" : "wait for enemy");
					alert.showAndWait();
					return;
				}
			}
		});
		if (loadGame) {
			playerBoardVisited = loadSeriObj.getPlayerBoardVisited();
			for (ArrayList<Integer> shipList : loadSeriObj.getShipList()) {
				Ship ship = new Ship(shipList.get(3), shipList.get(2) == 0 ? true : false);
				playerBoard.placeShip(ship, shipList.get(0), shipList.get(1));
			}
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					if (playerBoardVisited[i][j]) {
						playerBoard.getCell(i, j).shoot();
					}
				}
			}

			Deque<ArrayList<Integer>> shotCellList = seri.getShotCellList();
			Iterator<ArrayList<Integer>> stackDescendingItr = shotCellList.descendingIterator();
			while (stackDescendingItr.hasNext()) {
				ArrayList<Integer> shotCellValues = stackDescendingItr.next();
				Cell tempCell = playerBoard.getCell(shotCellValues.get(0), shotCellValues.get(1));
				shotCellStack = new ArrayDeque<Cell>();
				shotCellStack.add(tempCell);
			}

			startGameTimer();
			startPlayerTimer();
		}
		HBox playerBox = new HBox(playerBoard);
		playerBox.setAlignment(Pos.CENTER);

		playerBox.setOnDragOver(new EventHandler<DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				handleDragOver(event);
			}
		});
		root.getChildren().add(playerBox);
		root.getChildren().get(1).setTranslateX(-50);
		root.getChildren().get(1).setTranslateY(-60);

		root.getChildren().add(new Label("Player"));
		root.getChildren().get(2).setTranslateX(199);

		return root;
	}

	/**
	 * Enemy move.
	 */
	private void enemyMove() {

		/**
		 * Calls itself infinitely unless the enemy finds a valid cell to shoot which
		 * satisfies the boundary condition
		 */
		if (localGamplay.equals(GAME_PLAY_SALVA)) {
			enemyTurns = enemyBoard.ships;
		} else if (localGamplay.equals(GAME_PLAY_NORMAL)) {
			enemyTurns = 1;
		}

		while (enemyTurn) {

			if (shotCell == null || (shotCell != null && shotCellvisited())) {
				Cell cell = getRandonCell();

				if (!shootCells(cell, playerBoard)) {

					enemyTurn = false;
					if (localGamplay.equals(GAME_PLAY_SALVA)) {
						playerTurns = playerBoard.ships;
					} else if (localGamplay.equals(GAME_PLAY_NORMAL)) {
						playerTurns = 1;
					}

				}
			} else {
				Cell cell = shotCell;
				if (!shootNeighbours(cell, playerBoard, shotCellStack)) {
					if (enemyTurns == 0) {
						enemyTurn = false;
					} else {
						shotCell = null;
					}
					if (localGamplay.equals(GAME_PLAY_SALVA)) {
						playerTurns = playerBoard.ships;
					} else if (localGamplay.equals(GAME_PLAY_NORMAL)) {
						playerTurns = 1;
					}

				} else {
					shotCell = null;
				}
			}

			/**
			 * Re-iterates the loop and choses another random cell if the current cell was
			 * already shot.
			 */

			/**
			 * Displays a message if all player ships have been shot
			 */
			if (playerBoard.ships == 0) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Message");
				alert.setHeaderText("Bad Luck");
				alert.setContentText("YOU Lose. You Score is " + df.format(calculateScore()));
				alert.initOwner(primaryStage);
				alert.showAndWait();
				gameTimerTask.cancel();
				playerTimerTask.cancel();
				restartGameScreen();
			}
		}
		startPlayerTimer();
	}

	/**
	 * This method implements the AI functionality for the computer turn.
	 * 
	 * It checks if one cell has already been shot for a ship but the ship is not
	 * completely destroyed then search for the neighbouring cells.
	 */
	public boolean shotCellvisited() {
		boolean visited = true;
		Deque<Cell> tempStack = new ArrayDeque<Cell>(shotCellStack);
		Iterator<Cell> stackDescendingItr = tempStack.descendingIterator();
		while (stackDescendingItr.hasNext()) {
			Cell shotCell2 = stackDescendingItr.next();
			visited = true;
			shotCell = shotCell2;
			List<Point2D> visitedNeighbours = new ArrayList<Point2D>();
			List<Point2D> nonVisitedNeighbours = new ArrayList<Point2D>();
			List<Point2D> invalidNeighbours = new ArrayList<Point2D>();
			for (Point2D p : util.getNeighbors(shotCell2.x, shotCell2.y)) {
				if (!playerBoardVisited[(int) p.getX()][(int) p.getY()]) {
					visited = false;
					nonVisitedNeighbours.add(p);
				} else {
					visitedNeighbours.add(p);
				}
			}
			for (Point2D point : visitedNeighbours) {
				if (shipFoundOnCell[(int) point.getX()][(int) point.getY()]) {
					Point2D left = new Point2D(((int) shotCell2.x - 1), (int) shotCell2.y);
					Point2D right = new Point2D(((int) shotCell2.x + 1), (int) shotCell2.y);
					Point2D top = new Point2D(((int) shotCell2.x), (int) shotCell2.y - 1);
					Point2D bottom = new Point2D(((int) shotCell2.x), (int) shotCell2.y + 1);

					if ((util.isValidPoint(left) && left.equals(point))
							|| (util.isValidPoint(right) && right.equals(point))) {
						if (util.isValidPoint(top)) {
							invalidNeighbours.add(top);
							playerBoardVisited[(int) top.getX()][(int) top.getY()] = true;
						}
						if (util.isValidPoint(bottom)) {
							invalidNeighbours.add(bottom);
							playerBoardVisited[(int) bottom.getX()][(int) bottom.getY()] = true;
						}
					}
					if ((util.isValidPoint(top) && top.equals(point))
							|| (util.isValidPoint(bottom) && bottom.equals(point))) {
						if (util.isValidPoint(left)) {
							invalidNeighbours.add(left);
							playerBoardVisited[(int) left.getX()][(int) left.getY()] = true;
						}
						if (util.isValidPoint(right)) {
							invalidNeighbours.add(right);
							playerBoardVisited[(int) right.getX()][(int) right.getY()] = true;
						}

					}
				}
			}
			nonVisitedNeighbours.removeAll(invalidNeighbours);
			if (nonVisitedNeighbours.size() > 0) {
				break;
			} else {
				visited = true;
				shotCellStack.pop();
				continue;

			}
		}
		return visited;

	}

	/**
	 * Picks up a random cell on the player grid.
	 */
	private Cell getRandonCell() {
		int x = random.nextInt(10);
		int y = random.nextInt(10);

		if (!playerBoardVisited[x][y]) {
			playerBoardVisited[x][y] = true;
			return playerBoard.getCell(x, y);
		} else {
			return getRandonCell();
		}

	}

	/**
	 * Start game.
	 * 
	 * Randomly places the enemy ships within the enemy grid.
	 */
	private void startGame() {
		int[] ships = { 5, 4, 3, 3, 2 };

		System.out.println("reaching here loadGame " + loadGame);

		if (!loadGame || (loadGame && enemyShipList.isEmpty())) {
			for (int i = 0; i < ships.length;) {
				int x = random.nextInt(10);
				int y = random.nextInt(10);
				Ship ship = new Ship(ships[i], Math.random() < 0.5);
				if (enemyBoard.placeShip(ship, x, y)) {
					ArrayList<Integer> shipValues = new ArrayList<>();
					shipValues.add(x);
					shipValues.add(y);
					shipValues.add(ship.vertical ? 0 : 1);
					shipValues.add(ship.type);
					enemyShipList.add(shipValues);
					i++;
				}
			}
		}
		running = true;
		resetPlayerGlobalTime();
		startGameTimer();
		startPlayerTimer();
	}

	/**
	 * This is the global game timer method. It will be reset when a new game
	 * session is created either by pressing the retry button or the quit button.
	 */
	public void startGameTimer() {
		gameLayoutController = getGameLayoutController();

		Timer timer = new Timer();
		gameTimerTask = new GameTimerHelper();
		timer.schedule(gameTimerTask, 0, 100);
	}

	/**
	 * This is the local player timer method. It will be reset when the player has
	 * taked its turn and it is a miss and the next turn is of the other
	 * player/computer.
	 */
	public void startPlayerTimer() {
		gameLayoutController = getGameLayoutController();

		Timer timer = new Timer();
		playerTimerTask = new PlayerTimerHelper();
		timer.schedule(playerTimerTask, 0, 100);
	}

	/**
	 * Resets the player timer.
	 */
	private void resetPlayerTimer() {
		playerTimerTask.cancel();
		playerTimeVar = 0;
	}

	/**
	 * Resets the player global timer variable which stores the total time taken by
	 * the player in the entire game session.
	 */
	public void resetPlayerGlobalTime() {
		totalPlayerTime = 0;
	}

	private void setTimerVariables() {
		totalPlayerTime = seri.getTotalPlayerTime();
		globalTimeVar = seri.getGlobalTimeVar();
		playerTimeVar = seri.getPlayerTimeVar();
	}

	/**
	 * This class extends the TimerTask class which helps in automatic creation of
	 * separate timer thread and also supports scheduled calling of the run method
	 * for updating the timer variables used.
	 */
	class GameTimerHelper extends TimerTask {
		@Override
		public void run() {
			Platform.runLater(() -> {
				if (loadGame) {
					gameLayoutController.setTimerLabel(String.valueOf(df.format(globalTimeVar)));
					globalTimeVar += 0.1;
				} else {
					gameLayoutController.setTimerLabel(String.valueOf(df.format(globalTimeVar)));
					globalTimeVar += 0.1;
				}
			});
		}

	}

	/**
	 * This class extends the TimerTask class which helps in automatic creation of
	 * separate timer thread and also supports scheduled calling of the run method
	 * for updating the timer variables used.
	 */
	class PlayerTimerHelper extends TimerTask {
		@Override
		public void run() {
			Platform.runLater(() -> {
				gameLayoutController.setTimer2Label(String.valueOf(df.format(playerTimeVar)));
				playerTimeVar += 0.1;
			});
		}

	}

	/**
	 * Return to game.
	 * 
	 * Takes back to the main screen if the player wishes to abort the current game
	 * session
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void returnToGame() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(BattleshipMain.class.getResource("/battleship/view/GameScreen.fxml"));
		AnchorPane gameLayout = (AnchorPane) loader.load();
		Scene scene = new Scene(gameLayout);

		this.getPrimaryStage().setScene(scene);
		this.getPrimaryStage().setResizable(false);
		this.getPrimaryStage().show();
	}

	/**
	 * Restart game screen.
	 * 
	 * Resets to the empty scene so that other scenes can be implemented to get
	 * desired output.
	 */
	public void restartGameScreen() {
		try {
			resetGlobalVars();
			resetPlayerGlobalTime();
			initRootLayout(primaryStage);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public boolean getLoadGame() {
		return loadGame;
	}

	public void setLoadGame(boolean loadGame) {
		this.loadGame = loadGame;
	}

	/**
	 * Reset global vars. So that a new game can be started
	 */
	public void resetGlobalVars() {
		if (gameTimerTask != null) {
			gameTimerTask.cancel();
		}
		if (playerTimerTask != null) {
			playerTimerTask.cancel();
		}
		loadGame = false;
		totalPlayerTime = 0;
		globalTimeVar = 0;
		playerTimeVar = 0;
		enemyTurn = false;
		running = false;
		shipIndex = 0;
		totalShips = 5;
		localGamplay = gameScreenController.getGamePlay();
		enemyValue = gameScreenController.getEnemy();
		finalScore = 0;
	}

	public String getLocalGamplay() {
		return localGamplay;
	}

	public void setLocalGamplay(String localGamplay) {
		this.localGamplay = localGamplay;
	}

	/**
	 * Gets the primary stage.
	 *
	 * @return the primary stage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Sets the primary stage.
	 *
	 * @param primaryStage the new primary stage
	 */
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	/**
	 * Checks if a ship was shot in the previous run.
	 * 
	 * If the cell was shot then the computer will look for the neighbouring cells
	 * to shoot.
	 */
	public boolean shootCells(Cell cell, Board board) {
		int x = cell.x;
		int y = cell.y;
		if (cell.shoot()) {
			shotCell = cell;
			shotCellStack.add(cell);
			playerBoardVisited[x][y] = true;
			shipFoundOnCell[x][y] = true;
			shootNeighbours(cell, board, shotCellStack);

		} else {
			enemyTurns--;
			if (enemyTurns == 0) {
				return false;
			}
		}
		if (enemyTurns > 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * This method checks if the neighbouring cells of the previously shot cell(of
	 * the player ship) belong to the ship and shoots the cell
	 */
	public boolean shootNeighbours(Cell cell, Board board, Deque<Cell> shotCellStack) {
		int x = cell.x;
		int y = cell.y;
		for (Point2D p : util.getNeighbors(cell.x, cell.y)) {
			Cell cell1 = board.getCell((int) p.getX(), (int) p.getY());
			int shotCellx = cell1.x;
			int shotCelly = cell1.y;

			if (!playerBoardVisited[shotCellx][shotCelly]) {
				playerBoardVisited[shotCellx][shotCelly] = true;
				if (cell1.shoot()) {
					shipFoundOnCell[cell1.x][cell1.y] = true;
					shotCell = cell1;
					shotCellStack.add(cell1);
					if ((x - 1) == shotCellx) {
						boolean runLoop = false;
						do {
							if (shotCellx - 1 >= 0) {
								Cell cell2 = board.getCell(--shotCellx, shotCelly);
								if (!playerBoardVisited[shotCellx][shotCelly]) {
									playerBoardVisited[shotCellx][shotCelly] = true;
									if (cell2.shoot()) {
										shotCellStack.add(cell2);
										shipFoundOnCell[cell2.x][cell2.y] = true;
										shotCell = cell2;
										runLoop = true;
									} else {
										enemyTurns--;
										runLoop = false;
										if (enemyTurns == 0) {
											return false;
										}
									}
								} else {
									runLoop = false;
								}
							} else {
								continue;
							}
						} while (runLoop);
					} else if ((x + 1) == cell1.x) {
						boolean runLoop = false;
						do {
							if (shotCellx + 1 < 10) {
								Cell cell2 = board.getCell(++shotCellx, shotCelly);
								if (!playerBoardVisited[shotCellx][shotCelly]) {
									playerBoardVisited[shotCellx][shotCelly] = true;
									if (cell2.shoot()) {
										shotCellStack.add(cell2);
										shipFoundOnCell[cell2.x][cell2.y] = true;
										shotCell = cell2;
										runLoop = true;
									} else {
										enemyTurns--;
										runLoop = false;
										if (enemyTurns == 0) {
											return false;
										}
									}
								} else {
									runLoop = false;
								}
							} else {
								continue;
							}
						} while (runLoop);

					}

					else if ((y - 1) == shotCelly) {
						boolean runLoop = false;
						do {
							if (shotCelly - 1 >= 0) {
								Cell cell2 = board.getCell(shotCellx, --shotCelly);
								if (!playerBoardVisited[shotCellx][shotCelly]) {
									playerBoardVisited[shotCellx][shotCelly] = true;
									if (cell2.shoot()) {
										shotCellStack.add(cell2);
										shipFoundOnCell[cell2.x][cell2.y] = true;
										shotCell = cell2;
										runLoop = true;
									} else {
										enemyTurns--;
										runLoop = false;
										if (enemyTurns == 0) {
											return false;
										}
									}
								} else {
									runLoop = false;
								}
							} else {
								continue;
							}
						} while (runLoop);
					}

					else if ((y + 1) == cell1.y) {
						boolean runLoop = false;
						do {
							if (shotCelly + 1 < 10) {
								Cell cell2 = board.getCell(shotCellx, ++shotCelly);
								if (!playerBoardVisited[shotCellx][shotCelly]) {
									playerBoardVisited[shotCellx][shotCelly] = true;
									if (cell2.shoot()) {
										shotCellStack.add(cell2);
										shipFoundOnCell[cell2.x][cell2.y] = true;
										shotCell = cell2;
										runLoop = true;
									} else {
										enemyTurns--;
										runLoop = false;
										if (enemyTurns == 0) {
											return false;
										}
									}
								} else {
									runLoop = false;
								}
							} else {
								continue;
							}
						} while (runLoop);
					}
				} else {
					enemyTurns--;
					if (enemyTurns == 0) {
						break;
					}
				}
			}
		}
		if (enemyTurns > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Getter for GameLayoutController singleton object
	 */
	public GameLayoutController getGameLayoutController() {
		return gameLayoutController;
	}

	/**
	 * Getter for GameScreenController singleton object
	 */
	public GameScreenController getGameScreenController() {
		return gameScreenController;
	}

	/**
	 * Called by the event handler when a ship image is dragged
	 */
	private void handleDragOver(DragEvent event) {
		event.acceptTransferModes(TransferMode.ANY);
		event.consume();

	}

	/**
	 * Called by the event handler when a ship image is dropped
	 */
	private void handleDragDropped(DragEvent event) {
		event.acceptTransferModes(TransferMode.ANY);
		event.consume();

	}

	public void createConnection() {
		this.connection = createClient();
	}

	private Client createClient() {
		return new Client("localhost", 55555, data -> {
			Platform.runLater(() -> {
				try {
					String[] clientMessages = data.toString().split("_");
					System.out.println("clientMessages " + data.toString());
					System.out.println("clientMessages " + clientMessages[0]);
					switch (clientMessages[0]) {
					case "playerShip":
						Ship ship = new Ship(Integer.parseInt(clientMessages[3]),
								clientMessages[4].equalsIgnoreCase("true"));
						placeEnemyShips(Integer.parseInt(clientMessages[1]), Integer.parseInt(clientMessages[2]), ship);
						break;
					case "shoot":
						boolean success = enemyMove(Integer.parseInt(clientMessages[1]),
								Integer.parseInt(clientMessages[2]));

						try (StringWriter messageWriter = new StringWriter()) {

							messageWriter.append("enemyReply" + "_" + success);

							System.out.println("SHOOT " + messageWriter.toString());
							connection.send(messageWriter.toString());
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("Failed to send\n");
						}
						if (success) {
							isMyTurn = false;
							enemyTurn = true;
							isEnemyTurn.set(enemyTurn);
						} else {
							isMyTurn = true;
							enemyTurn = false;
							isEnemyTurn.set(enemyTurn);
						}
						break;
					case "enemyReply":
						System.out.println("Case enemyReply : " + clientMessages[1]);
						System.out.println("clientMessages[1].equalsIgnoreCase(\"true\") : "
								+ clientMessages[1].equalsIgnoreCase("true"));
						if (clientMessages[1].equalsIgnoreCase("true")) {
							isMyTurn = true;
							enemyTurn = false;
							isEnemyTurn.set(enemyTurn);
						} else {
							System.out.println("Failed to send\n");
							isMyTurn = false;
							enemyTurn = true;
							isEnemyTurn.set(enemyTurn);
						}
						break;
					default:
						break;
					}
				} catch (Exception e) {
					gameLayoutController.setServerEvent("Waiting for server");
				}
			});

		});
	}

	private void placeEnemyShips(int x, int y, Ship ship) {
		if (enemyBoard.placeShip(ship, x, y)) {
			enemyShips--;
		}

		if (enemyShips == 0) {
			enemyReady = true;
			isMyTurn = true;
			isEnemyTurn.setValue(false);
			resetPlayerGlobalTime();
			startGameTimer();
			startPlayerTimer();
		}

	}

	private boolean enemyMove(int x, int y) {

		Cell cell = playerBoard.getCell(x, y);
		/**
		 * Re-iterates the loop and choses another random cell if the current cell was
		 * already shot.
		 */
		if (gameScreenController.getGamePlay().equalsIgnoreCase(GAME_PLAY_SALVA)) {
			if (!cell.shoot()) {
				enemyTurns--;
				if (enemyTurns == 0) {
					enemyTurn = false;
				}
			}
		} else {
			return cell.shoot();
		}

		/**
		 * Displays a message if all player ships have been shot
		 */
		if (playerBoard.ships == 0) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Message");
			alert.setHeaderText("Bad Luck");
			alert.setContentText("YOU Lose");
			alert.initOwner(primaryStage);
			alert.showAndWait();
			restartGameScreen();
		}
		return enemyTurn;
	}

	private int calculateScore() {
		finalScore = (int) (totalPlayerTime - (globalTimeVar - totalPlayerTime)) / 10;
		return finalScore;

	}

	public void save() {
		seri.setSuccess(success);
		seri.setShips(ships);
		seri.setTotalPlayerTime(totalPlayerTime);
		seri.setGlobalTimeVar(globalTimeVar);
		seri.setPlayerTimeVar(playerTimeVar);
		seri.setFinalScore(finalScore);
		seri.setRunning(running);
		seri.setPlayerTurns(playerTurns);
		seri.setEnemyTurns(enemyTurns);
		seri.setPlayerBoardVisited(playerBoardVisited);
		seri.setShipFoundOnCell(shipFoundOnCell);
		seri.setShipList(shipList);
		seri.setTotalShips(totalShips);
		seri.setEnemyTurn(enemyTurn);
		seri.setEnemyBoardVisited(enemyBoardVisited);
		seri.setEnemyShipList(enemyShipList);
		seri.setLocalGamplay(localGamplay);
		seri.setEnemy(enemyValue);
		seri.setShip5Placed(gameLayoutController.isShip5Dropped());
		seri.setShip4Placed(gameLayoutController.isShip4Dropped());
		seri.setShip3Placed1(gameLayoutController.isShip3Dropped1());
		seri.setShip3Placed2(gameLayoutController.isShip3Dropped2());
		seri.setShip2Placed(gameLayoutController.isShip2Dropped());
		seri.setEnemyShips(enemyShips);

		seri.setEnemy(enemyValue);
		seri.setMyTurn(isMyTurn);
		seri.setPlayerReady(playerReady);
		seri.setEnemyReady(enemyTurn);
		seri.setIsEnemyTurn(isEnemyTurn);
		seri.setIsPlaying(isPlaying);
		seri.setServerPlay(isServerPlay);

		ArrayList<Integer> shotCellTemp = new ArrayList<>();
		shotCellTemp.add(shotCell.x);
		shotCellTemp.add(shotCell.y);
		seri.setShotCellCoordinates(shotCellTemp);

		Deque<ArrayList<Integer>> shotCellList = new ArrayDeque<ArrayList<Integer>>();
		Iterator<Cell> stackDescendingItr = shotCellStack.descendingIterator();
		while (stackDescendingItr.hasNext()) {
			ArrayList<Integer> shotCellValues = new ArrayList<>();
			shotCellValues.add(stackDescendingItr.next().x);
			shotCellValues.add(stackDescendingItr.next().y);
			shotCellList.add(shotCellValues);
		}
		seri.setShotCellList(shotCellList);

		// saving the object to external file
		FileOutputStream fileOutputStream = null;

		try {

			/*
			 * // String fileName = new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new //
			 * Date()); File file = new File(
			 * BattleshipMain.class.getResource("/battleship/save/").getPath() + new
			 * Date().getTime() + ".txt");
			 * 
			 * FileWriter fw = new FileWriter(file); fw.write("Welcome to javaTpoint.");
			 * fw.close(); System.out.println("File has been written");
			 */

			fileOutputStream = new FileOutputStream(
					BattleshipMain.class.getResource("/battleship/save/").getPath() + new Date().getTime() + ".txt",
					false);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(seri);
			objectOutputStream.flush();
			objectOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (gameTimerTask != null)
			gameTimerTask.cancel();
		if (playerTimerTask != null)
			playerTimerTask.cancel();
		try {
			initRootLayout(primaryStage);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public Serialization load() throws ClassNotFoundException, IOException {
		Path folder = Paths.get(BattleshipMain.class.getResource("/battleship/save/").getPath().substring(1));
		Optional<Path> lastFilePath = Files.list(folder).filter(f -> !Files.isDirectory(f))
				.max(Comparator.comparingLong(f -> Long.parseLong(f.toFile().getName().replace(".txt", ""))));
		System.out.println("lastFilePath " + lastFilePath.get());

		byte[] fileBytes = Files.readAllBytes(lastFilePath.get());
		ByteArrayInputStream in = new ByteArrayInputStream(fileBytes);
		ObjectInputStream is = new ObjectInputStream(in);
		Serialization loadSeriObj = (Serialization) is.readObject();
		System.out.println("loadSeriObj " + loadSeriObj.getTotalShips());

		setTimerVariables();
		return loadSeriObj;
	}

	private void initializeClassObjects() {
		System.out.println("gameScreenController: " + gameScreenController);

	}

	public void setSerializationObject() throws ClassNotFoundException, IOException {
		loadSeriObj = load();
		resetVariables();
		System.out.println("Callling here");
		System.out.println(loadSeriObj.getGlobalTimeVar());
	}

	public void resetVariables() {
		if (loadGame) {
			System.out.println("loadSeriObj.getLocalGamplay(); " + loadSeriObj.getLocalGamplay());
			totalPlayerTime = loadSeriObj.getTotalPlayerTime();
			globalTimeVar = loadSeriObj.getGlobalTimeVar();
			playerTimeVar = loadSeriObj.getPlayerTimeVar();
			finalScore = loadSeriObj.getFinalScore();
			running = loadSeriObj.isRunning();
			playerTurns = loadSeriObj.getPlayerTurns();
			enemyTurns = loadSeriObj.getEnemyTurns();
			playerBoardVisited = loadSeriObj.getPlayerBoardVisited();
			shipFoundOnCell = loadSeriObj.getShipFoundOnCell();
			totalShips = loadSeriObj.getTotalShips();
			enemyTurn = loadSeriObj.isEnemyTurn();
			enemyBoardVisited = loadSeriObj.getEnemyBoardVisited();
			enemyShipList = loadSeriObj.getEnemyShipList();
			localGamplay = loadSeriObj.getLocalGamplay();
			enemyValue = loadSeriObj.getEnemy();

			enemyShips = seri.getEnemyShips();

			enemyValue = seri.getEnemy();
			isMyTurn = seri.isMyTurn();
			playerReady = seri.isPlayerReady();
			enemyReady = seri.isEnemyReady();
			isEnemyTurn = seri.getIsEnemyTurn();
			isPlaying = seri.getIsPlaying();
			isServerPlay = seri.isServerPlay();

			ArrayList<Integer> shotCellTemp = seri.getShotCellCoordinates();
			shotCell.x = shotCellTemp.get(0);
			shotCell.y = shotCellTemp.get(1);
		} else {
			resetGlobalVars();
		}
	}

	public String getEnemyValue() {
		return enemyValue;
	}

	public void setEnemyValue(String enemyValue) {
		this.enemyValue = enemyValue;
	}

}
