package battleship;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import javafx.beans.property.SimpleBooleanProperty;

public class Serialization implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int enemyShips = 5;

	public boolean gameTimerTaskRunning;
	public boolean playerTimerTaskRunning;

	private String enemyValue;
	private boolean isMyTurn = false;
	private boolean playerReady;
	private boolean enemyReady;
	private SimpleBooleanProperty isEnemyTurn = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty isPlaying = new SimpleBooleanProperty(false);
	private boolean isServerPlay = false;

	private ArrayList<Integer> shotCellCoordinates = null;
	Deque< ArrayList<Integer>> shotCellList = new ArrayDeque< ArrayList<Integer>>();
	
	
	
	
	
	
	
	
	
	
	boolean success = false;

	public int[] ships = { 5, 4, 3, 3, 2 };
	private List<ArrayList<Integer>> shipList = new ArrayList<>();
	public double totalPlayerTime = 0;
	private boolean enemyBoardVisited[][] = new boolean[10][10];
	private List<ArrayList<Integer>> enemyShipList = new ArrayList<>();
	private boolean ship5Placed;
	private boolean ship4Placed;
	private boolean ship3Placed1;
	private boolean ship3Placed2;
	private boolean ship2Placed;
	private String localGamplay;
	private String Enemy;
	public double globalTimeVar = 0;

	public double playerTimeVar = 0;

	private int finalScore;

	private boolean running = false;

	private int playerTurns;

	private int enemyTurns;

	private boolean playerBoardVisited[][] = new boolean[10][10];

	private boolean shipFoundOnCell[][] = new boolean[10][10];

	// Deque<Cell> shotCellStack = new ArrayDeque<Cell>();

	private int totalShips = ships.length;

	private boolean enemyTurn = false;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int[] getShips() {
		return ships;
	}

	public void setShips(int[] ships) {
		this.ships = ships;
	}

	public double getTotalPlayerTime() {
		return totalPlayerTime;
	}

	public void setTotalPlayerTime(double totalPlayerTime) {
		this.totalPlayerTime = totalPlayerTime;
	}

	public double getGlobalTimeVar() {
		return globalTimeVar;
	}

	public void setGlobalTimeVar(double globalTimeVar) {
		this.globalTimeVar = globalTimeVar;
	}

	public double getPlayerTimeVar() {
		return playerTimeVar;
	}

	public void setPlayerTimeVar(double playerTimeVar) {
		this.playerTimeVar = playerTimeVar;
	}

	public int getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(int finalScore) {
		this.finalScore = finalScore;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public int getPlayerTurns() {
		return playerTurns;
	}

	public void setPlayerTurns(int playerTurns) {
		this.playerTurns = playerTurns;
	}

	public int getEnemyTurns() {
		return enemyTurns;
	}

	public void setEnemyTurns(int enemyTurns) {
		this.enemyTurns = enemyTurns;
	}

	public boolean[][] getPlayerBoardVisited() {
		return playerBoardVisited;
	}

	public void setPlayerBoardVisited(boolean[][] playerBoardVisited) {
		this.playerBoardVisited = playerBoardVisited;
	}

	public boolean[][] getShipFoundOnCell() {
		return shipFoundOnCell;
	}

	public void setShipFoundOnCell(boolean[][] shipFoundOnCell) {
		this.shipFoundOnCell = shipFoundOnCell;
	}

	public int getTotalShips() {
		return totalShips;
	}

	public void setTotalShips(int totalShips) {
		this.totalShips = totalShips;
	}

	public boolean isEnemyTurn() {
		return enemyTurn;
	}

	public void setEnemyTurn(boolean enemyTurn) {
		this.enemyTurn = enemyTurn;
	}

	public List<ArrayList<Integer>> getShipList() {
		return shipList;
	}

	public void setShipList(List<ArrayList<Integer>> shipList) {
		this.shipList = shipList;
	}

	public boolean[][] getEnemyBoardVisited() {
		return enemyBoardVisited;
	}

	public void setEnemyBoardVisited(boolean[][] enemyBoardVisited) {
		this.enemyBoardVisited = enemyBoardVisited;
	}

	public List<ArrayList<Integer>> getEnemyShipList() {
		return enemyShipList;
	}

	public void setEnemyShipList(List<ArrayList<Integer>> enemyShipList) {
		this.enemyShipList = enemyShipList;
	}

	public String getLocalGamplay() {
		return localGamplay;
	}

	public void setLocalGamplay(String localGamplay) {
		this.localGamplay = localGamplay;
	}

	public boolean isShip5Placed() {
		return ship5Placed;
	}

	public void setShip5Placed(boolean ship5Placed) {
		this.ship5Placed = ship5Placed;
	}

	public boolean isShip4Placed() {
		return ship4Placed;
	}

	public void setShip4Placed(boolean ship4Placed) {
		this.ship4Placed = ship4Placed;
	}

	public boolean isShip3Placed1() {
		return ship3Placed1;
	}

	public void setShip3Placed1(boolean ship3Placed1) {
		this.ship3Placed1 = ship3Placed1;
	}

	public boolean isShip3Placed2() {
		return ship3Placed2;
	}

	public void setShip3Placed2(boolean ship3Placed2) {
		this.ship3Placed2 = ship3Placed2;
	}

	public boolean isShip2Placed() {
		return ship2Placed;
	}

	public void setShip2Placed(boolean ship2Placed) {
		this.ship2Placed = ship2Placed;
	}

	public String getEnemy() {
		return Enemy;
	}

	public void setEnemy(String enemy) {
		Enemy = enemy;
	}

	public int getEnemyShips() {
		return enemyShips;
	}

	public void setEnemyShips(int enemyShips) {
		this.enemyShips = enemyShips;
	}

	public boolean isGameTimerTaskRunning() {
		return gameTimerTaskRunning;
	}

	public void setGameTimerTaskRunning(boolean gameTimerTaskRunning) {
		this.gameTimerTaskRunning = gameTimerTaskRunning;
	}

	public boolean isPlayerTimerTaskRunning() {
		return playerTimerTaskRunning;
	}

	public void setPlayerTimerTaskRunning(boolean playerTimerTaskRunning) {
		this.playerTimerTaskRunning = playerTimerTaskRunning;
	}

	public String getEnemyValue() {
		return enemyValue;
	}

	public void setEnemyValue(String enemyValue) {
		this.enemyValue = enemyValue;
	}

	public boolean isMyTurn() {
		return isMyTurn;
	}

	public void setMyTurn(boolean isMyTurn) {
		this.isMyTurn = isMyTurn;
	}

	public boolean isPlayerReady() {
		return playerReady;
	}

	public void setPlayerReady(boolean playerReady) {
		this.playerReady = playerReady;
	}

	public boolean isEnemyReady() {
		return enemyReady;
	}

	public void setEnemyReady(boolean enemyReady) {
		this.enemyReady = enemyReady;
	}

	public SimpleBooleanProperty getIsEnemyTurn() {
		return isEnemyTurn;
	}

	public void setIsEnemyTurn(SimpleBooleanProperty isEnemyTurn) {
		this.isEnemyTurn = isEnemyTurn;
	}

	public SimpleBooleanProperty getIsPlaying() {
		return isPlaying;
	}

	public void setIsPlaying(SimpleBooleanProperty isPlaying) {
		this.isPlaying = isPlaying;
	}

	public boolean isServerPlay() {
		return isServerPlay;
	}

	public void setServerPlay(boolean isServerPlay) {
		this.isServerPlay = isServerPlay;
	}

	public ArrayList<Integer> getShotCellCoordinates() {
		return shotCellCoordinates;
	}

	public void setShotCellCoordinates(ArrayList<Integer> shotCellCoordinates) {
		this.shotCellCoordinates = shotCellCoordinates;
	}

	public Deque<ArrayList<Integer>> getShotCellList() {
		return shotCellList;
	}

	public void setShotCellList(Deque<ArrayList<Integer>> shotCellList) {
		this.shotCellList = shotCellList;
	}

}
