package battleship.model;

import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// TODO: Auto-generated Javadoc
/**
 * The Class Board.
 */
/**
 * @author Vsu Chuchra, Nitin Nanda, Simarpreet Kaur Jabbal, Vrind Gupta, Ayush
 *         Arya
 *
 */
public class Board extends Parent {

	/** The rows. */
	private VBox rows = new VBox();

	/** The enemy. */
	private boolean enemy = false;

	/** The ships. */
	public int ships = 5;

	/**
	 * Instantiates a new board.
	 *
	 * @param enemy   the enemy
	 * @param handler the handler
	 */
	public Board(boolean enemy, EventHandler<Event> handler) {
		this.enemy = enemy;
		for (int y = 0; y < 10; y++) {
			HBox row = new HBox();
			for (int x = 0; x < 10; x++) {
				Cell c = new Cell(x, y, this);
				if (!enemy) {
					c.setOnDragDropped(handler);
					c.setOnDragOver((DragEvent event) -> {
						if (event.getDragboard().hasImage()) {
							event.acceptTransferModes(TransferMode.ANY);
						}
						event.consume();
					});

					c.setOnDragEntered((DragEvent event) -> {
						if (event.getDragboard().hasImage() && c.ship == null) {
							c.setFill(Color.GREEN);
						}
						event.consume();
					});

					c.setOnDragExited((DragEvent event) -> {
						if (c.getFill().equals(Color.GREEN) && !event.isDropCompleted()) {
							c.setFill(Color.BLACK);
							c.setStroke(Color.WHITE);
						}
						event.consume();
					});
				} else {
					c.setOnMouseClicked(handler);
				}
				row.getChildren().add(c);
			}

			rows.getChildren().add(row);
		}

		getChildren().add(rows);
	}

	/**
	 * Board Class constructor that creates the grid
	 */
	public Board() {
		for (int y = 0; y < 10; y++) {
			HBox row = new HBox();
			for (int x = 0; x < 10; x++) {
				Cell c = new Cell(x, y, this);
				row.getChildren().add(c);
			}
			rows.getChildren().add(row);
		}

	}

	/**
	 * Place ship.
	 *
	 * @param ship the ship
	 * @param x    the x
	 * @param y    the y
	 * @return true, if successful
	 */
	public boolean placeShip(Ship ship, int x, int y) {
		/**
		 * Checks boundary conditions if a ship can be placed at the chosen coordinates
		 * and places the ship by appropriately colouring the cells if it is a player
		 * ship. Does not color the cells if it is an enemy ship.
		 * 
		 */
		if (canPlaceShip(ship, x, y)) {
			int length = ship.type;

			if (ship.vertical) {
				for (int i = y; i < y + length; i++) {
					Cell cell = getCell(x, i);
					cell.ship = ship;
					/**
					 * If it is not an enemy ship color it as Grey
					 */
					cell.setFill(Color.DARKGREY);
					cell.setStroke(Color.GREEN);
				}
			} else {
				for (int i = x; i < x + length; i++) {
					Cell cell = getCell(i, y);
					cell.ship = ship;
					/**
					 * If it is not an enemy ship color it as Grey
					 */

					cell.setFill(Color.DARKGREY);
					cell.setStroke(Color.GREEN);
				}
			}

			return true;
		}

		return false;
	}

	/**
	 * Gets the cell.
	 *
	 * Returns the Cell object when grid coordinates are passed.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the cell
	 */
	public Cell getCell(int x, int y) {
		return (Cell) ((HBox) rows.getChildren().get(y)).getChildren().get(x);
	}

	/**
	 * Gets the neighbors. Returns an array of neighbouring cells which satisfy the
	 * boundary conditions defined by method isValidPoint(Point2D).
	 * 
	 * @param x the x
	 * @param y the y
	 * @return the neighbors
	 */
	public Cell[] getNeighbors(int x, int y) {
		Point2D[] points = new Point2D[] { new Point2D(x - 1, y), new Point2D(x + 1, y), new Point2D(x, y - 1),
				new Point2D(x, y + 1) };

		List<Cell> neighbors = new ArrayList<Cell>();

		for (Point2D p : points) {
			if (isValidPoint(p)) {
				neighbors.add(getCell((int) p.getX(), (int) p.getY()));
			}
		}

		return neighbors.toArray(new Cell[0]);
	}

	/**
	 * Can place ship.
	 * 
	 * Checks various conditions if a ship can be placed at the passed x and y
	 * coordinates.
	 * 
	 * @param ship the ship
	 * @param x    the x
	 * @param y    the y
	 * @return true, if successful
	 */
	public boolean canPlaceShip(Ship ship, int x, int y) {
		int length = ship.type;

		if (ship.vertical) {
			for (int i = y; i < y + length; i++) {
				if (!isValidPoint(x, i))
					return false;

				Cell cell = getCell(x, i);
				/**
				 * Returns false if the cell already contains a ship.
				 */
				if (cell.ship != null)
					return false;

				/**
				 * Returns false if the neighbouring cells already contains a ship
				 */
				for (Cell neighbor : getNeighbors(x, i)) {
					if (!isValidPoint(x, i))
						return false;

					if (neighbor.ship != null)
						return false;
				}

			}
		} else {
			for (int i = x; i < x + length; i++) {
				if (!isValidPoint(i, y))
					return false;

				Cell cell = getCell(i, y);
				/**
				 * Returns false if the cell already contains a ship.
				 */
				if (cell.ship != null)
					return false;

				for (Cell neighbor : getNeighbors(i, y)) {
					if (!isValidPoint(i, y))
						return false;
					/**
					 * Returns false if the neighbouring cells already contains a ship.
					 */
					if (neighbor.ship != null)
						return false;
				}

			}
		}

		return true;
	}

	/**
	 * Checks if is valid point.
	 * 
	 * @param point the point
	 * @return true, if is valid point
	 */
	public boolean isValidPoint(Point2D point) {
		return isValidPoint(point.getX(), point.getY());
	}

	/**
	 * Checks if is valid point.
	 * 
	 * Returns false if the point is out of the grid boundary.
	 * 
	 * @param x the x
	 * @param y the y
	 * @return true, if is valid point
	 */
	public boolean isValidPoint(double x, double y) {
		return x >= 0 && x < 10 && y >= 0 && y < 10;
	}

	/**
	 * The Class Cell that define and declares the x and y coordinates along with
	 * the borad that "this" cell belongs to.
	 */
	public class Cell extends Rectangle {

		/** The y. */
		public int x, y;

		/** The ship. */
		public Ship ship = null;

		/** The was shot. */
		public boolean wasShot = false;

		/** The board. */
		private Board board;

		/**
		 * Instantiates a new cell.
		 *
		 * @param x     the x
		 * @param y     the y
		 * @param board the board
		 */
		public Cell(int x, int y, Board board) {
			super(30, 30);
			this.x = x;
			this.y = y;
			this.board = board;
			setFill(Color.BLACK);
			setStroke(Color.WHITE);
		}

		/**
		 * Shoot. Sets the cell as "shot" and a player or enemy cannot shoot it again.
		 * 
		 * Sets appropriate color to the cell if it was a hit or a miss.
		 * 
		 * @return true, if successful
		 */
		public boolean shoot() {
			wasShot = true;
			setFill(Color.WHITE);

			if (ship != null) {
				ship.hit();
				setFill(Color.RED);
				if (!ship.isAlive()) {
					board.ships--;
				}
				return true;
			}

			return false;
		}
	}

	/**
	 * Getter for the number of rows as we can choose 10*10 or 20*20 grid
	 */
	public VBox getRows() {
		return rows;
	}

	/**
	 * Setter for the number of rows as we can choose 10*10 or 20*20 grid
	 */
	public void setRows(VBox rows) {
		this.rows = rows;
	}

}