package flingball;

import physics.Angle;
import physics.Vect;
import java.util.List;
import physics.LineSegment;
import java.awt.Shape;


public interface Gadget {
    
    /*
     * Datatype definition:
     *      Gadget = Absorber(width:Integer, height:Integer, location: Vect, orientation:Angle) 
     *              + CircleBumper(circle:Circle)
     *              + SquareBumper(width:Integer, height:Integer, location: Vect, orientation:Angle)
     *              + TriangleBumper(width:Integer, height:Integer, location: Vect, orientation:Angle)
     *              + Wall(wall:LineSegment)
     */
        
    
    /**
     * 
     * @return x,y coordinates of the Gadget's location
     * location is defined as the center of a ball gadget and the 
     * upper left corner of the bounding box for other types of gadgets
     * (note that for a triangle bumper rotated 180 degrees, this point is actually outside 
     * of the bumper)
     * 
     */
    public Vect getLocation();
    
    
    /**
     * 
     * @return Reflection Coefficient (must be non-negative)
     * absorber has Ref. Coeff. of 0 because the ball doesn't rebound???
     */
    public double getReflectionCoefficient();
    
    
    
    /**
     * 
     * @return angle of orientation (limited to 0,90,180,270) (measured clockwise)
     * absorber gadgets should always return an orientation of 0
     */
    public Angle getOrientation();
    
    /**
     * 
     * @return height of gadget
     */
    public int getHeight();
    
    /**
     * 
     * @return width of gadget
     */
    public int getWidth();
    
    /**
    *
    * @return list of all line segments in object
    */
    public List<LineSegment> getLineSegments();
    
    /**
     * 
     * @return the type of gadget this represents
     */
    public String getType();
    
    /**
     * should be called every time this gadget is triggered
     * calls action on the gadgets which this gadget's trigger is hooked up to
     * 
     */
    public void trigger();
    
    /**
     * executes the action of a gadget
     */
    public void action();
    
    /**
     * @return the board which this gadget is on
     */
    public Board getBoard();
    
    
    /**
     * Create a Shape object
     * @return shape Shape of the object to be filled in the GUI
     */
    public Shape fill();
    
    /**
     * @return the unique name of the Gadget
     */
    public String getName();
    
    
    /*
     * Create a human readable form of the gadget.
     * @return a String of the object that shows certain charateristics
     */
    @Override
    public String toString();
    
    /*
     * Determine if two objects are the same object
     * @return true if two objects are the same by either having the same name
     *         or many of the same characteristics.  Other wise false.
     */
    @Override
    public boolean equals(Object that);
    
    
    /*
     * @return a hashed integer value for a gadget
     */
    @Override
    public int hashCode();    
}
