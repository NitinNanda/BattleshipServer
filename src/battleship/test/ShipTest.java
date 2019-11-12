package battleship.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import battleship.model.Ship;
import battleship.BattleshipMain;
import battleship.model.Board;
import battleship.model.Board.Cell;

public class ShipTest {
	private Ship ship;
	private Board board;
	private BattleshipMain battleship;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		board=new Board();
		battleship=new BattleshipMain();
	
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testShipLengthAfterOneCellShoot() {
		ship = new Ship(4, true);
		board.placeShip(ship, 5,4);
		Cell cell = board.getCell(5,4);
		cell.shoot();
		assertEquals(ship.getHealth(), 3);
	}
	
	@Test
	public void testGetShipHealthAfterShoot() {
		ship = new Ship(2, true);
		board.placeShip(ship, 5,4);
		Cell cell1 = board.getCell(5,4);
		cell1.shoot();
		Cell cell2 = board.getCell(5,5);
		cell2.shoot();
		assertEquals(ship.getHealth(), 0);
	}
	
	@Test
	public void testIsAlive() {
		ship = new Ship(2, true);
		board.placeShip(ship, 5,4);
		Cell cell1 = board.getCell(5,4);
		cell1.shoot();
		Cell cell2 = board.getCell(5,5);
		cell2.shoot();
		assertFalse(ship.isAlive());
	}
	
	@Test
	public void testNumberOfShips() {
		int[] shipsToBeDropped=battleship.ships;
		assertEquals(shipsToBeDropped.length,5);
	}

}
