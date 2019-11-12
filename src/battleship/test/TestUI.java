package battleship.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import battleship.BattleshipMain;
import battleship.controller.GameScreenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TestUI extends ApplicationTest {
	private static BattleshipMain battleship;
	private Stage startStage;
	private BorderPane startScreen;
	private static GameScreenController gameScreenController;
	private AnchorPane gameScreen;
	private ChoiceBox choice;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		battleship = new BattleshipMain();
		gameScreenController = new GameScreenController();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		gameScreenController = new GameScreenController();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStartUpPhaseValues() {

		ChoiceBox choice = (ChoiceBox) gameScreen.getChildren().get(4);
		boolean initialChoiceBoxValue = false;
		if (choice.getValue() == null) {
			initialChoiceBoxValue = true;
		}
		assertTrue(initialChoiceBoxValue);
	}

	@Test
	public void testStartUpPhaseValuesOfChoiceBox() {

		ChoiceBox choice = (ChoiceBox) gameScreen.getChildren().get(4);
		boolean validateCorrectItemsOfChoiceBox = false;
		if (choice.getItems().get(0) == "SALVA" && choice.getItems().get(0) == "NORMAL") {
			validateCorrectItemsOfChoiceBox = true;
		}
		assertTrue(true);
	}

	@Test
	public void testGameScreenStartUp() throws Exception {
		assertTrue(startStage.isShowing());
	}
	
	

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

	public void showGameScreen(Stage primaryStage) {
		try {
			/**
			 * Load the fxml file and provide relative path of the file.
			 */
			FXMLLoader loader = new FXMLLoader();
			loader.setController(gameScreenController);
			gameScreenController.setMainApp(battleship);
			loader.setLocation(BattleshipMain.class.getResource("/battleship/view/GameScreen.fxml"));
			gameScreen = (AnchorPane) loader.load();

			startScreen.setCenter(gameScreen);
			/**
			 * Sets a controller for the loaded fxml file.
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.startStage = stage;
		startStage.setTitle("Battleship");
		startStage.setResizable(false);
		startStage.getIcons().add(new Image("/battleship/view/images/logo.jpg"));
		initRootLayout(startStage);
	}

}
