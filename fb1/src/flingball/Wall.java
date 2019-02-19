package flingball;

import java.awt.Polygon;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import physics.Angle;
import physics.LineSegment;
import physics.Vect;

public  class Wall implements Gadget{

    private final double maxBoardDimensions = 400.0;

    private final Board board;
    private final LineSegment wall;
    private final Boolean isVertical;
    
    /*
     * AF(wall) = represent a wall of the board.
     *      
     * RI:
     *      Wall must expand the entire edge of the board
     *      point 1 x coordinate must be <= point 2 x coordinate (this takes precedence)
     *      point 1 y coordinate must be <= point 2 y coordinate (this doens't take precedence)
     *      
     *      
     * Safety from rep exposure:
     *      All fields are private
     *      Do not return mutable objects
     */
    
    /**
     * A wall object that acts as the border of the board
     * @param board Board that the object is apart of
     * @param wall LineSegment that is the boarder
     */
    public Wall(Board board, LineSegment wall) {
        this.wall = wall;
        this.board = board;
        

        if (wall.p1().x() == wall.p2().x()) {
            isVertical = true;
        }
        else {
            isVertical = false;
        }
        
        checkRep();
        
    }
    
    /**
     * Guarantee that the invariant is not broken
     */
    private void checkRep() {
        assert wall.length() == maxBoardDimensions;
        
    }
    
    public String getName() {
        return "Wall";
    }
    
    
    public Vect getLocation() {
        if (wall.p1().x() <= wall.p2().x() && wall.p1().y() <= wall.p2().y()) {
            return this.wall.p1();
        }
        return wall.p2();
    }
    
    public List<LineSegment> getLineSegments() {
        List<LineSegment> wallList = new ArrayList<LineSegment>();
        wallList.add(this.wall);
        return wallList;
    }

    public double getReflectionCoefficient() {
        return 1.0;
    }

    public Angle getOrientation() {
        if (isVertical) {
        return new Angle(0);
        }
        else {
            return new Angle(Math.PI/2.0);
        }
    }

    public int getHeight() {
        if (isVertical){
            return (int) this.wall.length();
        }
        else {
            return 0;
        }
        
    }

    public int getWidth() {
        if (isVertical) {
            return 0;
        }
        else {
            return (int) wall.length();
        }
    }

    public String getType() {
        return "Wall";
    }
    
    public Shape fill() {
        Shape s = new Polygon();
        return s;
    }
    
    public void trigger() {
        // no trigger
        
    }

    public void action() {
        // no action
        
    }

    public Board getBoard() {
        return this.board;
    }

    @Override public int hashCode() {
        return getLineSegments().get(0).hashCode();
    }

    @Override public boolean equals(Object obj) {
        if(!(obj instanceof Wall)) {
            return false;
        }
        Wall that = (Wall) obj;
        return getLineSegments().get(0).equals(that.getLineSegments().get(0));
    }

    @Override public String toString() {
        return "Wall with points " + this.wall.p1() + " and " + this.wall.p2(); 
    }
   
}