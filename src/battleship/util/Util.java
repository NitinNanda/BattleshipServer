package battleship.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import battleship.model.Board;
import battleship.model.Board.Cell;
import javafx.geometry.Point2D;
import javafx.scene.layout.HBox;

/**
 * A utility class that checks the various conditions 
 * while selecting a cell and returns appropriate cells 
 */
public class Util {
	
	/**
	 * Returns neighbours of the cell provided by 
	 * the passed x and y coordinates
	 */
	public List<Point2D> getNeighbors(int x, int y) {
		Point2D[] points = new Point2D[] { new Point2D(x - 1, y), new Point2D(x + 1, y), new Point2D(x, y - 1),
				new Point2D(x, y + 1) };
		ArrayList<Point2D> tempList = new ArrayList<>();
		for (Point2D p : points) {
			if (isValidPoint(p)) {
				tempList.add(p);
			}
		}
		return tempList;
	}

	/**
	 * Helper function which verifies if the coordinates 
	 * exists within the chosen grid 
	 */
	public boolean isValidPoint(Point2D point) {
		return isValidPoint(point.getX(), point.getY());
	}

	/**
	 * Verifies if the coordinates exists within the chosen grid 
	 */
	public boolean isValidPoint(double x, double y) {
		return x >= 0 && x < 10 && y >= 0 && y < 10;
	}

	/**
	 * Returns a cell existing at the passed x and y coordinates
	 * on the board  
	 */
	public Cell getCell(int x, int y, Board board) {
		return (Cell) ((HBox) board.getRows().getChildren().get(y)).getChildren().get(x);
	}
}
