package flingball;

import java.awt.geom.Ellipse2D;
import java.util.List;

import physics.Angle;
import physics.Circle;
import physics.LineSegment;
import physics.Vect;

public class CircleBumper implements Gadget {


    private final double minBoardDimensions = 0.0;
    private final double maxBoardDimensions = 400.0;
    
    private final Board board;
    private final Circle circle;
    private final String name;
    
    /*
     * AF(board, circle, name) = represents a circle bumper that has a specific location on the board and a unique
     *                          name to distinguish it from other objects  
     *      
     * RI:
     *      radius > 0
     *      entirety of the bumper is on the board
     *      name is non null
     *      
     * Safety from rep exposure:
     *      All fields are private
     *      Do not return mutable objects
     */
    
    /**
     * @param board Board that the object is apart of
     * @param circle Circle containing the center and radius of the circle
     * @param name String name that is unique to the object
     */
    public CircleBumper(Board board, Circle circle, String name) {
        this.circle = circle;
        this.name = name;
        this.board = board;
        checkRep();
    }
    
    /**
     * Guarantee that the rep invariants are not broken
     */
    public void checkRep() {
        assert(this.circle.getRadius() > minBoardDimensions);
        assert(getLocation().x() >= minBoardDimensions);
        assert(getLocation().y() >= minBoardDimensions);
        assert(getLocation().x() + 2*this.circle.getRadius() <= maxBoardDimensions);
        assert(getLocation().y() + 2*this.circle.getRadius() <= maxBoardDimensions);
        assert(this.name != null);
        
    }
    
    public String getName() {
        return this.name;
    }
    

    public Vect getLocation() {
        Vect circleCenter =  this.circle.getCenter();
        return new Vect(circleCenter.x() - .5*this.getWidth(), circleCenter.y() - .5*this.getHeight());
    }
    
    public Circle getCircle() {
        return this.circle;
    }

    public double getReflectionCoefficient() {
        return 1.0;
    }

    public Angle getOrientation() {
        return new Angle(0);
    }

    public int getHeight() {
        return (int) Math.ceil(2*this.circle.getRadius());
    }

    public int getWidth() {
        return (int) Math.ceil(2*this.circle.getRadius());
    }
    
    public List<LineSegment> getLineSegments() {
        return null;
    }

    
    public String getType() {
        return "CircleBumper";
    }
    
    public Ellipse2D fill() {
        return new Ellipse2D.Double(getLocation().x(), 
                                    getLocation().y(), 
                                    2*this.circle.getRadius(), 2*this.circle.getRadius());
   }




    public void trigger() {
        getBoard().activateTriggers(this);
        
    }

    public void action() {
        // no action
        
    }

    public Board getBoard() {
        return this.board;
    }
    
    
    @Override 
    public int hashCode() {
        return getName().hashCode();
    }

    @Override 
    public boolean equals(Object obj) {
        if(!(obj instanceof CircleBumper)) {
            return false;
        }
        CircleBumper that = (CircleBumper) obj;
        return getName().equals(that.getName());
    }


    @Override 
    public String toString() {
        return "Circle Bumper at " + this.getLocation().toString() + " and diameter " +  this.getWidth();
    }


}
