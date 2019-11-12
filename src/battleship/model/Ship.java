package battleship.model;

import javafx.scene.Parent;

// TODO: Auto-generated Javadoc
/**
 * The Class Ship.
 */
/**
 * @author Vsu Chuchra, Nitin Nanda, Simarpreet Kaur Jabbal, Vrind Gupta, Ayush Arya
 *
 */
public class Ship extends Parent {
    
    /** The type. */
    public int type;
    
    /** The vertical. */
    public boolean vertical = true;

    /** The health. */
    public int health;

    /**
     * Instantiates a new ship.
     *
     * @param type the type
     * @param vertical the vertical
     */
    public Ship(int type, boolean vertical) {
        this.type = type;
        this.vertical = vertical;
        health = type;
    }

    /**
     * Hit.
     * 
     * Integer variable health counts the 
     * length of the ship that has not been hit yet.
     * 
     */
    public void hit() {
        health--;
    }
    
    /**
     * Stores the length of the ship left to be destroyed
     */
    public int getHealth() {
    	return health;
    }

    /**
     * Checks if is alive.
     *
     * @return true, if is alive
     */
    public boolean isAlive() {
        return health > 0;
    }
}