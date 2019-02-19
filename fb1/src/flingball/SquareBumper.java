package flingball;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import physics.Angle;
import physics.LineSegment;
import physics.Vect;

public class SquareBumper implements Gadget {

    private final double minBoardDimensions = 0.0;
    private final double maxBoardDimensions = 400.0;

    private final Board board;
    private final int width;
    private final int height;
    private final Vect location;
    private final Angle orientation;
    private final String name;

    /*
     * AF(width, height, location, orientation, name) = represents a square bumper object in flingball that has width and height dimensions,
     *      a location, and orientation.  Bumper has a unique name to distinguish it form other bumpers
     *      
     * RI:
     *      Width and height must be positive
     *      Reflection coefficient is one
     *      height = width (square)
     *      
     * Safety from rep exposure:
     *      All fields are private
     *      Do not return mutable objects
     */

    /**
     * @param board Board that the object is apart of
     * @param sideLength Integer length of the sides of the SquareBumper
     * @param location Point location of the top left corner of the SquareBumper
     * @param orientation Angle orientation of Absorber compared to center (0 degrees)
     * @param name String name that is unique to the object
     */
    public SquareBumper(Board board, int sideLength, Vect location, Angle orientation, String name) {
        this.width = sideLength;
        this.height = sideLength;
        this.location = location;
        this.orientation = orientation;
        this.name = name;
        this.board = board;
        checkRep();
    }

    private void checkRep() {
        assert (location.x() <= maxBoardDimensions);
        assert (location.x() >= minBoardDimensions);
        assert (location.y() <= maxBoardDimensions);
        assert (location.y() >= minBoardDimensions);
        assert (width==height);
        assert (width >= minBoardDimensions);
        assert (height >= minBoardDimensions);
    }
    
    public String getName() {
        return this.name;
    }

    public Vect getLocation() {
        return this.location;
    }

    public double getReflectionCoefficient() {
        return 1.0;
    }

    public Angle getOrientation() {
        return this.orientation;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public List<LineSegment> getLineSegments() {
        List<LineSegment> lineList = new ArrayList<LineSegment>();
        //Top
        lineList.add(new LineSegment(new Vect(getLocation().x(), getLocation().y()),  new Vect(getLocation().x() + this.width, getLocation().y())));

        //Bottom
        lineList.add(new LineSegment(new Vect(getLocation().x(), getLocation().y()+this.height),  new Vect(getLocation().x() + this.width, getLocation().y()+this.height)));

        //Left
        lineList.add(new LineSegment(new Vect(getLocation().x(), getLocation().y()),  new Vect(getLocation().x(), getLocation().y() + this.height)));

        //Right
        lineList.add(new LineSegment(new Vect(getLocation().x()+this.width, getLocation().y()),  new Vect(getLocation().x()+this.width, getLocation().y() + this.height)));

        return lineList;
    }

    public String getType() {
        return "SquareBumper";
    }
    
    public Shape fill() {
        return new Rectangle2D.Double(getLocation().x(), getLocation().y(),
                                      getWidth(), getHeight());
   }


    public void trigger() {
        board.activateTriggers(this);
    }

    public void action() {
        // no action
        
    }

    public Board getBoard() {
        return this.board;
    }
    
    @Override public int hashCode() {
        return getName().hashCode();
    }

    @Override 
    public boolean equals(Object obj) {
        if(!(obj instanceof SquareBumper)) {
            return false;
        }
        SquareBumper that = (SquareBumper) obj;
        return (getWidth() == that.getWidth() && 
                getHeight() == that.getHeight() &&
                getLocation().equals(that.getLocation()) &&
                getOrientation().equals(that.getOrientation())
                );
    }

    @Override public String toString() {
        return "Square Bumper at " + this.getLocation().toString() + " with orientation " + this.getOrientation().toString() + " and side length " +  this.getWidth();

    }
}
