package battleship.test;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import battleship.BattleshipMain;
import battleship.model.Board;
import battleship.model.Board.Cell;
import battleship.model.Ship;
import battleship.util.Util;
import javafx.geometry.Point2D;

public class BattleShipMainTest {
	private Board board;
	private Board enemyBoard;
	private Ship ship;
	private int enemyTurns;
	private BattleshipMain battleship;
	private Util util;
	private Cell shotCell;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		board = new Board();
		battleship = new BattleshipMain();
		util = new Util();
		enemyBoard = new Board();

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSalvaFunctionality() {
		board.placeShip(new Ship(3, false), 2, 5);
		board.placeShip(new Ship(3, false), 7, 6);
		board.placeShip(new Ship(2, true), 0, 4);
		board.placeShip(new Ship(4, true), 5, 5);
		board.placeShip(new Ship(5, false), 7, 9);
		enemyTurns = board.ships;

		Cell cell1 = board.getCell(0, 4);
		Cell cell2 = board.getCell(0, 5);
		cell1.shoot();
		cell2.shoot();

		enemyTurns = board.ships;
		assertEquals(4, enemyTurns);
	}

	@Test
	public void testAIFeature() {
		board.placeShip(new Ship(2, true), 0, 4);
		Cell cell1 = board.getCell(0, 4);
		cell1.shoot();
		Deque<Cell> shotCellStack = new ArrayDeque<Cell>();
		shotCellStack.add(cell1);
		battleship.shootNeighbours(cell1, board, shotCellStack);
		List<Point2D> li = util.getNeighbors(0, 4);
		for (int i = 0; i < li.size(); i++) {
			Cell cell = board.getCell((int) li.get(i).getX(), (int) li.get(i).getY());
			assertTrue(cell.wasShot);
		}
	}

	@Test
	public void testCalculationOfTheWinner() {
		boolean enemyBoardWinner = false;
		board.placeShip(new Ship(3, false), 2, 5);
		board.placeShip(new Ship(3, false), 7, 6);
		board.placeShip(new Ship(2, true), 0, 4);
		board.placeShip(new Ship(4, true), 5, 5);
		board.placeShip(new Ship(5, false), 7, 9);

		enemyBoard.placeShip(new Ship(3, false), 2, 5);
		enemyBoard.placeShip(new Ship(3, false), 7, 6);
		enemyBoard.placeShip(new Ship(2, true), 0, 4);
		enemyBoard.placeShip(new Ship(4, true), 5, 5);
		enemyBoard.placeShip(new Ship(5, false), 7, 9);

		while (board.ships != 0) {
			board.ships--;
		}

		if (board.ships == 0) {
			enemyBoardWinner = true;
		}

		assertTrue(enemyBoardWinner);
	}

	@Test
	public void testShotCellvisitedAIFunctionality() {
		board.placeShip(new Ship(4, true), 5, 5);

		Deque<Cell> shotCellStack = new ArrayDeque<Cell>();
		boolean playerBoardVisited[][] = new boolean[10][10];
		boolean shipFoundOnCell[][] = new boolean[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				playerBoardVisited[i][j] = false;
			}
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				shipFoundOnCell[i][j] = false;
			}
		}
		Cell cell1 = board.getCell(5, 5);
		cell1.shoot();
		shotCellStack.add(cell1);
		playerBoardVisited[(int) cell1.getX()][(int) cell1.getY()] = true;
		shipFoundOnCell[(int) cell1.getX()][(int) cell1.getY()] = true;
		assertFalse(shotCellvisited(shotCellStack, playerBoardVisited, shipFoundOnCell));
	}

	@Test
	public void testAIFunctionalityForInvalidCells() {
		board.placeShip(new Ship(4, true), 5, 5);

		Deque<Cell> shotCellStack = new ArrayDeque<Cell>();
		boolean playerBoardVisited[][] = new boolean[10][10];
		boolean shipFoundOnCell[][] = new boolean[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				playerBoardVisited[i][j] = false;
			}
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				shipFoundOnCell[i][j] = false;
			}
		}
		Cell cell1 = board.getCell(5, 5);
		Cell cell2 = board.getCell(5, 4);
		Cell cell3 = board.getCell(5, 6);

		cell1.shoot();
		cell2.shoot();
		cell3.shoot();
		shotCellStack.add(cell1);
		shotCellStack.add(cell2);
		shotCellStack.add(cell3);
		playerBoardVisited[(int) cell1.getX()][(int) cell1.getY()] = true;
		playerBoardVisited[(int) cell2.getX()][(int) cell2.getY()] = true;
		playerBoardVisited[(int) cell3.getX()][(int) cell3.getY()] = true;
		shipFoundOnCell[(int) cell1.getX()][(int) cell1.getY()] = true;
		shipFoundOnCell[(int) cell2.getX()][(int) cell2.getY()] = false;
		shipFoundOnCell[(int) cell3.getX()][(int) cell3.getY()] = true;
		assertFalse(shotCellvisited(shotCellStack, playerBoardVisited, shipFoundOnCell));
	}

	@Test
	public void testUpadtedShotCell() {
		board.placeShip(new Ship(4, true), 6, 5);

		Deque<Cell> shotCellStack = new ArrayDeque<Cell>();
		boolean playerBoardVisited[][] = new boolean[10][10];
		boolean shipFoundOnCell[][] = new boolean[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				playerBoardVisited[i][j] = false;
			}
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				shipFoundOnCell[i][j] = false;
			}
		}
		Cell cell1 = board.getCell(6, 5);
		Cell cell2 = board.getCell(6, 4);
		Cell cell3 = board.getCell(6, 6);

		cell1.shoot();
		cell2.shoot();
		cell3.shoot();
		shotCellStack.add(cell1);
		shotCellStack.add(cell2);
		shotCellStack.add(cell3);
		playerBoardVisited[(int) cell1.getX()][(int) cell1.getY()] = true;
		playerBoardVisited[(int) cell2.getX()][(int) cell2.getY()] = true;
		playerBoardVisited[(int) cell3.getX()][(int) cell3.getY()] = true;
		shipFoundOnCell[(int) cell1.getX()][(int) cell1.getY()] = true;
		shipFoundOnCell[(int) cell2.getX()][(int) cell2.getY()] = false;
		shipFoundOnCell[(int) cell3.getX()][(int) cell3.getY()] = true;
		shotCellvisited(shotCellStack, playerBoardVisited, shipFoundOnCell);
		assertEquals(shotCell, cell3);
	}
	
	
	
	@Test
	public void testShipsToBeDropped() {
		int[] shipsToBeDropped=battleship.ships;
		assertEquals(shipsToBeDropped.length,5);
	}
	
	
	
	
	

	boolean shotCellvisited(Deque<Cell> shotCellStack, boolean playerBoardVisited[][], boolean shipFoundOnCell[][]) {
		boolean visited = true;
		Iterator<Cell> stackDescendingItr = shotCellStack.descendingIterator();

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
					System.out.println("nonVisitedNeighbours X " + p.getX());
					System.out.println("nonVisitedNeighbours Y " + p.getY());
				} else {
					visitedNeighbours.add(p);
					System.out.println("visitedNeighbours X " + p.getX());
					System.out.println("visitedNeighbours Y " + p.getY());
				}
			}
			for (Point2D point : visitedNeighbours) {
				System.out.println("shipFoundOnCell X " + point.getX());
				System.out.println("shipFoundOnCell Y " + point.getY());
				System.out.println(shipFoundOnCell[(int) point.getX()][(int) point.getY()]);
				if (shipFoundOnCell[(int) point.getX()][(int) point.getY()]) {
					Point2D left = new Point2D(((int) shotCell2.getX() - 1), (int) shotCell2.getY());
					Point2D right = new Point2D(((int) shotCell2.getX() + 1), (int) shotCell2.getY());
					Point2D top = new Point2D(((int) shotCell2.getX()), (int) shotCell2.getY() - 1);
					Point2D bottom = new Point2D(((int) shotCell2.getX()), (int) shotCell2.getY() + 1);
					System.out.println("left " + left);
					System.out.println("right " + right);
					System.out.println("top " + top);
					System.out.println("bottom " + bottom);
					if ((util.isValidPoint(left) && left == point) || (util.isValidPoint(right) && right == point)) {
						if (util.isValidPoint(top)) {
							invalidNeighbours.add(top);
							playerBoardVisited[(int) top.getX()][(int) top.getY()] = true;
						}
						if (util.isValidPoint(bottom)) {
							invalidNeighbours.add(bottom);
							playerBoardVisited[(int) bottom.getX()][(int) bottom.getY()] = true;
						}
					}
					if ((util.isValidPoint(top) && top == point) || (util.isValidPoint(bottom) && bottom == point)) {
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
}
