package flingball;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import physics.Angle;
import physics.Circle;
import physics.LineSegment;
import physics.Vect;

public class Absorber implements Gadget {


    private final Board board;
    private final int width;
    private final int height;
    private final Vect location;
    private final Angle orientation;
    private final String name;
    private final ArrayList<Ball> containsBalls;


    private final int PIXELS_PER_L = 20;
    private final double minBoardDimensions = 0.0;
    private final double maxBoardDimensions = 400.0;


    /*
     * AF(board, width, height, location, orientation, name) = represents an absorber object in flingball that has a corresponging board, 
     *                  width and height dimensions, a location, orientation, and unique name.  An Absorber can absorb balls and fires 
     *                  them when triggered.
     *      
     * RI:
     *      Width and height must be positive
     *      Reflection coefficient is zero
     *      All corner must be within the dimensions of the board
     *      
     * Safety from rep exposure:
     *      All fields are private
     *      Do not return mutable objects
     */


    /**
     * @param board Board that the object is apart of
     * @param width Integer width of the Absorber
     * @param height Integer height of the Absorber
     * @param location Vect location of the center of the Absorber (center of mass)
     * @param orientation Angle orientation of Absorber compared to center (0 degrees)
     */
    public Absorber(Board board, int width, int height, Vect location, Angle orientation, String name) {
        this.board = board;
        this.width = width;
        this.height = height;
        this.location = location;
        this.orientation = orientation;
        this.name = name;
        this.containsBalls = new ArrayList<Ball>();;
        checkRep();
    }

    /**
     * Guarantee that the dimensions of the Absorber are within the dimensions of the board
     */
    public void checkRep() {
        assert this.width >= minBoardDimensions;
        assert this.height >= minBoardDimensions;
        assert this.getReflectionCoefficient() == 0;
        assert this.location.x() >= minBoardDimensions;
        assert this.location.y() >= minBoardDimensions;
        assert this.location.x() <= maxBoardDimensions;
        assert this.location.y() <= maxBoardDimensions;
        assert this.location.x() + this.width <= maxBoardDimensions;
        assert this.location.y() + this.height<= maxBoardDimensions;
        assert this.name != null;
    }

    public String getName() {
        return this.name;
    }

    public Vect getLocation() {
        return this.location;
    }

    public double getReflectionCoefficient() {
        return 0;
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

    /**
     * Fires a held ball from it's bottom right corner if another ball is not currently being fired
     */
    public void action() {

        if (this.containsBalls.size() > 1 && !updateHold()) {
            // Get a ball from the Absorber's list of held balls
            Ball newBall = this.containsBalls.remove(0);
            Circle circle = new Circle(new Vect(getLocation().x() + getWidth() - .25*PIXELS_PER_L,getLocation().y() + getHeight() - .25*PIXELS_PER_L),.25*PIXELS_PER_L);
            newBall.changeLocation(circle);
            newBall.changeVelocity(new Vect(0,-PIXELS_PER_L));
            

            // release the ball with a new location and velocity
            board.addBall(newBall);
        }
    }

    /**
     * For a given ball, checks if it is in the bounds of the Absorber
     * @param b Ball potentially in the bounds of the absorber
     * @return true if b is inside the bounds of the Absorber, false otherwise
     */
    public boolean checkIfLaunchInProgress(Ball b) {
        if ( (this.getLocation().x() <= b.getLocation().x() 
                && b.getLocation().x() <= this.getLocation().x()+this.getWidth()
                && this.getLocation().y() <= b.getLocation().y()
                && b.getLocation().y() <= this.getLocation().y()+this.getHeight()
                && b.getVelocity().y()<0 )){
            return true;   
        }
        return false;
    }

    /**
     * Checks all of the balls on the board
     * @return true if the ball is inside the bounds of the Absorber, false otherwise
     */
    public boolean updateHold() {
        for (Ball b:board.getBalls()) {
            if(this.checkIfLaunchInProgress(b)) {
                return true;
            }
        }
        return false;
    }

    public void trigger() {
        board.activateTriggers(this);
    }

    /**
     * Checks if the absorber is holding any balls
     * @return true if holding any balls, false otherwise
     */
    public boolean containsBalls() {
        return this.containsBalls.size() > 0;
    }

    /**
     * Adds a ball to the list of balls that an absorber holds
     * @param ball Ball to be added
     */
    public void addBallToAbsorber(Ball ball) {
        this.containsBalls.add(ball);
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


    public Board getBoard() {
        return board;
    }


    public String getType() {
        return "Absorber";
    }

    public Shape fill() {
        return new Rectangle2D.Double(getLocation().x(), getLocation().y(),
                getWidth(), getHeight());
    }

    @Override 
    public int hashCode() {
        return getName().hashCode();
    }

    @Override 
    public boolean equals(Object obj) {
        if(!(obj instanceof Absorber)) {
            return false;
        }
        Absorber that = (Absorber) obj;
        return (getWidth() == that.getWidth() && 
                getHeight() == that.getHeight() &&
                getLocation().equals(that.getLocation()) &&
                getOrientation().equals(that.getOrientation())
                );
    }


    @Override 
    public String toString() {
        return "Absorber at " + this.getLocation() + ", width " +  this.getWidth() + ", and height " + this.getHeight();

    }
}
