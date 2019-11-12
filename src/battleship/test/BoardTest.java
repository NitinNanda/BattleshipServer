package battleship.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import battleship.model.Board;
import battleship.model.Board.Cell;
import battleship.model.Ship;

public class BoardTest {
	private Board board;
	private Ship ship;
	int x;
	int y;



	@Before
	public void setUp() throws Exception {
		ArrayList<ArrayList<Integer>> listOfCoordinates = new ArrayList<ArrayList<Integer>>();
		x = 5;
		y = 5;
		board = new Board();
		ship = new Ship(2, false);
	}

	
	@Test 
	public void testCanPlaceShipHorizonatally() {
		ship = new Ship(3, false);
		assertFalse(board.canPlaceShip(ship, 9, 0));
	}

	@Test 
	public void testCanPlaceShipVertically() {
		ship = new Ship(3, true);
		assertFalse(board.canPlaceShip(ship, 0, 9));
	}

	@Test 
	public void testCannotPlaceShipHorizontallyAdjacentToNeighbours() {

		ship = new Ship(3, false);
		board.placeShip(ship, x, y);
		System.out.println(x);
		System.out.println(y);
		Cell[] cellCoOrdinates = board.getNeighbors(x, y);

		System.out.println("The ships are " + board.ships);

		for (Cell neighbour : cellCoOrdinates) {
			assertFalse(board.placeShip(ship, neighbour.x, neighbour.y));
		}
	}

	 @Test 
	public void testCannotPlaceShipVerticallytallyAdjacentToNeighbours() {
		ship = new Ship(2, true);
		board.placeShip(ship, x, y);

		Cell[] cellCoOrdinates = board.getNeighbors(x, y);

		System.out.println("The ships are " + board.ships);

		for (Cell neighbour : cellCoOrdinates) {
			assertFalse(board.placeShip(ship, neighbour.x, neighbour.y));
		}
	}

	@Test 
	public void testCellColourChangeOnShoot() {
		ship = new Ship(2, true);
		board.placeShip(ship, x, y);
		Cell cell = board.getCell(x, y);
		cell.shoot();
		assertEquals(javafx.scene.paint.Color.RED, cell.getFill());
	}

	@Test 
	public void testShipLengthChangedOnShoot() {
		ship = new Ship(4, true);
		board.placeShip(ship, x, y);
		Cell cell = board.getCell(x, y);
		cell.shoot();
		assertEquals(ship.getHealth(), 3);
	}

	@Test 
	public void testCellColourChangeOnMiss() {
		ship = new Ship(2, true);
		board.placeShip(ship, x, y);
		Cell cell = board.getCell(8, 8);
		cell.shoot();
		assertEquals(javafx.scene.paint.Color.WHITE, cell.getFill());
	}
	
	@Test 
	public void testShipLengthRemainedSameOnMiss() {
		ship = new Ship(2, true);
		board.placeShip(ship, x, y);
		Cell cell = board.getCell(0,5);
		cell.shoot();
		assertEquals(ship.health,2);
	}
	@Test
	public void checkIfTheCoOrdinateIsValidOrNot() {
		assertFalse(board.isValidPoint(11.0, 11.0));
	}
	
	@Test 
	public void checkTheLengthOfShipAfterShoot() {
		ship = new Ship(2, true);
		board.placeShip(ship, x, y);
		Cell cell1 = board.getCell(5,5);
		cell1.shoot();
		Cell cell2 = board.getCell(5,6);
		cell2.shoot();
		assertEquals(ship.getHealth(), 0);
		
	}
	@Test 
	public void checkGetCellMethod() {
		boolean isInstanceOfCell=false;
		if(board.getCell(x, y) instanceof Cell) {
			isInstanceOfCell=true;
		}
		assertTrue(isInstanceOfCell);
	}
}
