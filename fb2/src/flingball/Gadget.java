package flingball;

import java.awt.Graphics2D;
import java.util.List;

import physics.Circle;
import physics.LineSegment;
import physics.Vect;

/**
 * A mutable data type representing a general Flingball gadget.
 */
public interface Gadget {
    
    /**
     * Displays this gadget on the window.
     * @param g graphics for the drawing buffer for the window. 
     *          Modifies this graphics by drawing the state of the gadget on it.
     */
    public void render(final Graphics2D g);
    
    /**
     * Resolve collision if a ball is colliding with this gadget,
     * according to the rules of Flingball Physics.
     * @param ball ball of the Flingball game
     * @return true if the ball collides with this gadget, 
     *         false otherwise
     */
    public boolean resolveCollision(final Ball ball);
    
    /** @return gadget's name */
    public String getName();
    
    /** @return list of line segments making up this gadget */
    public List<LineSegment> getLineSegments();
    
    /** @return list of circles making up this gadget */
    public List<Circle> getCircles();
    
    /** @return position vector of this gadget's origin */
    public Vect getPosition();
    
    /**
     * @return the width of the gadget
     */
    public double getWidth();
    
    /**
     * @return the height of the gadget
     */
    public double getHeight();
    
    /** 
     * Adds to this gadget a consumer gadget that can be triggered by this gadget.
     * @param triggeredGadget gadget to which this gadget triggers an action
     */
    public void addTrigger(final Gadget triggeredGadget);
    
    /** 
     * Triggers the actions of all consumer gadgets of this gadget's trigger events. 
     */
    public void trigger();
    
    /** 
     * Responds to a triggering event. 
     */
    public void respondToTrigger();
   
    /**
     * Indicates whether some other object has the same value as this gadget.
     * @param that an object with which to compare
     * @return true if that object is observationally equal to this one,
     *         false otherwise.
     */
    public boolean sameValue(final Object that);
    
    @Override 
    public String toString();

}
