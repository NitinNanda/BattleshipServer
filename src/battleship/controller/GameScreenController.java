package battleship.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import battleship.BattleshipMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The Class GameScreenController.
 */
/**
 * @author Vsu Chuchra, Nitin Nanda, Simarpreet Kaur Jabbal, Vrind Gupta, Ayush
 *         Arya
 *
 */
public class GameScreenController implements Initializable {

	/** The main app. */
	private BattleshipMain mainApp;

	/**
	 * The gamePlay variable which sets which type of gameplay it is.
	 */
	private String gamePlay;
	
	/**
	 * The gamePlay variable which sets which type of gameplay it is.
	 */
	private String enemy;

	/**
	 * Sets the main app.
	 *
	 * @param mainController the new main app
	 */
	public void setMainApp(BattleshipMain mainController) {
		this.mainApp = mainController;
	}

	/**
	 * Chooses between Salva play or Normal play
	 */
	@FXML
	private ChoiceBox<String> gamePlayChoices;
	@FXML
	private ChoiceBox<String> enemyOptions;

	/**
	 * Label to display the type of play (Salva or Normal)
	 */

	@FXML
	private Label choiceBoxLabel;
	@FXML
	private Label enemyOptionLabel;

	/**
	 * Handle start.
	 *
	 * @throws Exception the exception
	 */
	@FXML
	private void handleStart() throws Exception {
		/**
		 * Load the fxml file and provide relative path of the file.
		 */
		if (gamePlayChoices.getValue() == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Important Message");
			alert.setHeaderText("Not Allowed");
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setContentText("Select Game Play");
			alert.showAndWait();
			return;
		}
		if (enemyOptions.getValue() == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Important Message");
			alert.setHeaderText("Not Allowed");
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setContentText("Select Enemy");
			alert.showAndWait();
			return;
		}
		mainApp.getGameScreenController().setGamePlay(gamePlayChoices.getValue());
		mainApp.setLoadGame(false);
		mainApp.resetVariables();
		mainApp.getGameScreenController().setEnemy(enemyOptions.getValue());
		mainApp.setEnemyValue(enemyOptions.getValue());
		FXMLLoader loader = new FXMLLoader();
		loader.setController(mainApp.getGameLayoutController());
		mainApp.getGameLayoutController().setMainApp(mainApp);
		loader.setLocation(BattleshipMain.class.getResource("/battleship/view/GameLayout.fxml"));
		SplitPane gameLayout = (SplitPane) loader.load();

		mainApp.createEnemyContent(((AnchorPane) ((SplitPane) gameLayout.getItems().get(1)).getItems().get(0)));
		mainApp.createPlayerContent(((AnchorPane) ((SplitPane) gameLayout.getItems().get(1)).getItems().get(2)));
		Scene scene = new Scene(gameLayout);

		mainApp.getPrimaryStage().setTitle("Battleship");
		/**
		 * Sets the scene containing the game layout.
		 */
		mainApp.getPrimaryStage().setScene(scene);
	}

	private void handleLoad() throws Exception {
		/**
		 * Load the fxml file and provide relative path of the file.
		 */
		gamePlayChoices.setValue(mainApp.getLocalGamplay());

		System.out.println("HANDLE LOAD 1");
		FXMLLoader loader = new FXMLLoader();
		loader.setController(mainApp.getGameLayoutController());
		mainApp.getGameLayoutController().setMainApp(mainApp);
		mainApp.getGameScreenController().setGamePlay(gamePlayChoices.getValue());
		System.out.println("HANDLE LOAD 2");
		mainApp.setSerializationObject();
		loader.setLocation(BattleshipMain.class.getResource("/battleship/view/GameLayout.fxml"));
		SplitPane gameLayout = (SplitPane) loader.load();
		System.out.println("HANDLE LOAD 3");
		mainApp.setLoadGame(true);
		mainApp.resetVariables();
		mainApp.createEnemyContent(((AnchorPane) ((SplitPane) gameLayout.getItems().get(1)).getItems().get(0)));
		mainApp.createPlayerContent(((AnchorPane) ((SplitPane) gameLayout.getItems().get(1)).getItems().get(2)));
		Scene scene = new Scene(gameLayout);
		System.out.println("HANDLE LOAD 4");

		mainApp.getPrimaryStage().setTitle("Battleship");
		/**
		 * Sets the scene containing the game layout.
		 */
		mainApp.getPrimaryStage().setScene(scene);

	}

	/**
	 * Handle scores.
	 *
	 * Displays a message that this section is currently under development.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@FXML
	private void handleScores() throws Exception {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText("Coming Soon");
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setContentText("Wait for build 3");
		alert.showAndWait();
	}

	/**
	 * Handle rules.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@FXML
	private void handleRules() throws Exception {
		try {
			/**
			 * Load the fxml file and provide relative path of the file.
			 */
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(GameScreenController.class.getResource("/battleship/view/Rules.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			/**
			 * Create the dialog Stage.
			 */
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Rules");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(mainApp.getPrimaryStage());
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			/**
			 * Show the dialog and wait until the user closes it
			 */
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handle exit.
	 *
	 * @throws Exception the exception
	 */
	@FXML
	private void handleExit() throws Exception {
		System.exit(0);
	}

	/**
	 * Overridden method to initialize the play
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		choiceBoxLabel.setText("Select Game Play");

		gamePlayChoices.getItems().add("SALVA");
		gamePlayChoices.getItems().add("NORMAL");
		
		enemyOptionLabel.setText("Choose Enemy");

		enemyOptions.getItems().add("COMPUTER");
		enemyOptions.getItems().add("PLAYER");

	}

	/**
	 * Getter for the selected gameplay string
	 */
	public String getGamePlay() {
		return gamePlay;
	}

	/**
	 * Setter for the selected gameplay string
	 */
	public void setGamePlay(String gamePlay) {
		this.gamePlay = gamePlay;
	}

	public String getEnemy() {
		return enemy;
	}

	public void setEnemy(String enemy) {
		this.enemy = enemy;
	}
@FXML
	public void load() throws Exception {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText("Load Game");
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setContentText("It will open your most recent saved game instance");
		alert.showAndWait();

		System.out.println("abover handle load");
		handleLoad();
		System.out.println("below handle load");
	}
}
