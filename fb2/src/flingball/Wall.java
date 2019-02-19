package flingball;

import java.util.Optional;

import physics.LineSegment;

/**
 * Mutable type representing a wall in a flingball game
 */
public class Wall {
	
	//fields
	private final LineSegment lineSegment;
	private final String orientation;
	private boolean removed;
	private Optional<String> neighboringBoard = Optional.empty();
	
	// Abstraction function: 
	//  AF(lineSegment, orientation, removed, neighboringBoard) = 
	//    A wall on a flingball board that is physically represented by a line segment. 
	//    It has an orientation, that is one of 'Left', 'Right', 'Bottom', or 'Top', 
	//    denoting which wall of the board the Wall represents. The wall is either on the
	//    board or removed from the board, which is represented by the removed variable. 
	//    The wall may be neighboring another board, as represented by 'neighboringBoard'.
	// Rep Invariant: 
	//  orientation = 'Left' or orientation = 'Right' or orientation = 'Bottom' or orientation = 'Top'
	// Safety from rep exposure: 
	// - all fields are private
	// - all fields besides neighboringBoard are immutable, and neighboringBoard is never returned directly
	
	/**
	 * Makes a new Wall instance
	 * @param lineSegment lineSegment object representing the Wall
	 * @param orientation String representing the orientation of the board,
	 *        must be one of 'Left', 'Right', 'Bottom', or 'Top'
	 */
	public Wall(LineSegment lineSegment, String orientation) {
		this.lineSegment = lineSegment;
		this.orientation = orientation;
		removed = false;
		checkRep();
	}
	
	//check the rep invariant
	private void checkRep() {
	    assert lineSegment != null;
	    assert orientation.equals("Top") || orientation.equals("Bottom") ||
	           orientation.equals("Left") || orientation.equals("Right");
	    assert neighboringBoard != null;
	}
	
	/**
	 * @return the line segment that makes up this wall
	 */
	public LineSegment getLineSegment() {
		return this.lineSegment;
	}
	
	/**
	 * @return whether this wall is a left, right, bottom or top wall
	 */
	public String getOrientation() {
		return orientation;
	}
	
	/**
	 * @return if the wall is removed from the board or not
	 */
	public boolean isRemoved() {
		return removed;
	}
	
	/**
     * @return whether the wall has a neighboring board
     */
    public boolean hasNeighboringBoard() {
        return neighboringBoard.isPresent();
    }
	
	/**
	 * @return the name of the board that neighbors this wall, 
	 *         requires the wall has a neighboring board
	 */
	public String getNeighboringBoard() {
		return neighboringBoard.get();
	}
	
	/**
	 * Removes the wall from the Board it is on
	 */
	public void removeWall() {
		removed = true;
	}
	
	/**
	 * Adds the side wall to the board it is on
	 */
	public void addWall() {
		removed = false;
	}
	
	/**
	 * Adds the name of the board that neighbors this wall
	 * @param boardName name of the neighboring board
	 */
	public void addNeighboringBoard(String boardName) {
		this.neighboringBoard = Optional.of(boardName);
	}
	
	/**
	 * removes the board next to this wall
	 */
	public void removeNeighboringBoard() {
		this.neighboringBoard = Optional.empty();
	}
}
