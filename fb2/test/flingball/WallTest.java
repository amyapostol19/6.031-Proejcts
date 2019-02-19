package flingball;

import static org.junit.Assert.*;

import org.junit.Test;

import physics.LineSegment;

public class WallTest {

	/*
	 * Partitions for testing Wall class
     * 	constructor: make sure all fields are properly initialized
     * 	getLineSegment: make sure properly returns line segment
     * 	getOrientation: make sure properly returns orientation
     * 	isRemoved: is removed, isn't removed
     * 	getNeighboringBoard: make sure properly returns name of neighboring board
     * 	removeWall: make sure wall is removed
     * 	addWall: make sure wall is added
     * 	addNeighboringBoard: make sure board is added
     * 	removeNeighboringBoard: make sure board is removed
	 * 
	 */
    private static final LineSegment LINE_SEGMENT = new LineSegment(1, 5, 5, 5);
    private static final Wall WALL = new Wall (LINE_SEGMENT, "Left");

    // covers constructor, get line segment, get orientation, isn't removed
	@Test
	public void testConstructor() {
		assertTrue("expected proper line segment", WALL.getLineSegment().equals(LINE_SEGMENT));
	    assertTrue("expected proper orientation", WALL.getOrientation().equals("Left"));
	    assertTrue("expected not removed", ! WALL.isRemoved());
	}
	
    // covers is removed, remove wall
    @Test
    public void testGetLineSegment() {
        final Wall wall = new Wall (LINE_SEGMENT, "left");
        wall.removeWall();
        assertTrue("expected wall to be removed", wall.isRemoved());
    }
    
    // covers get neighboring board, add neighboring board
    @Test
    public void testGetNeighboringBoard() {
        final Wall wall = new Wall (LINE_SEGMENT, "left");
        wall.addNeighboringBoard("neighbor");
        assertTrue("expected wall to be removed", wall.getNeighboringBoard().equals("neighbor"));
    }
    
    // covers add wall
    @Test
    public void testAddWall() {
        final Wall wall = new Wall (LINE_SEGMENT, "left");
        wall.removeWall();
        wall.addWall();
        assertTrue("expected wall to not be removed", ! wall.isRemoved());
    }
    
    // covers get neighboring board, add neighboring board
    @Test
    public void testRemoveNeighboringBoard() {
        final Wall wall = new Wall (LINE_SEGMENT, "left");
        wall.addNeighboringBoard("neighbor");
        wall.removeNeighboringBoard();
        assertTrue("expected wall to be removed", ! wall.hasNeighboringBoard());
    }
}
