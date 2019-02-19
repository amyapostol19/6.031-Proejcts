package flingball;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import physics.LineSegment;

/**
 * 
 * Keeps track of all objects on the board
 *
 */
public class Board {
    
    private final int PIXELS_PER_L = 20;
    private final double minBoardDimensions = 0.0;
    private final double defaultFriction = 0.025;

    
    //field methods
    private final String name;
    private final List<Ball> balls;
    private final List<Gadget> gadgets;
    private final double gravity;
    private final Map<Gadget, Gadget> triggers;
    private final ArrayList<Double> friction;
    
    

    
    /**
     * AF(Ball, Walls, bumpers, gravity, friction, triggers): represents the board which flingball will be played on. Each board has a unqiue name, bumpers
     *      at specific locations, balls which move around the area of the board, gravity and friction that act on the balls, and triggers that track bumpers
     *      and absorbers that trigger absorbers to shoot/realease balls.
     * 
     * Rep Invariant:
     *      friction > 0
     *      board has at least 4 gadgets (walls)
     *      (No other invariants because board require non-wall gadgets or balls)
     *      
     * Safety from Rep Exposure:
     *      all fields are private
     *      return copies of mutable objects
     *      
     *      Other functions need to have the ability to remove balls from the list of balls, and this was a design
     *      decision that our group thought was necessary if balls were to get absorbed by absorbers.
     * 
     */
    
    /**
     * Creator method: creates a new instance of the board class.
     * Exists for testing purposes.
     * @param name String name that is unique to the object
     * @param balls List of all balls on the board
     * @param gadgets List of all gadgets on the board
     * @param gravity decimal value of gravity to be applied to balls
     * @param triggers Map where a Gadget key triggers another Gadget value
     */
    public Board(String name, List<Ball> balls, List<Gadget> gadgets, double gravity, Map<Gadget, Gadget> triggers) {
        this.name = name;
        this.balls = balls;
        this.gadgets = gadgets;
        
        final Wall topWall = new Wall(this, new LineSegment(minBoardDimensions, minBoardDimensions, PIXELS_PER_L*PIXELS_PER_L, minBoardDimensions));
        final Wall bottomWall = new Wall(this, new LineSegment(minBoardDimensions, PIXELS_PER_L*PIXELS_PER_L, PIXELS_PER_L*PIXELS_PER_L, PIXELS_PER_L*PIXELS_PER_L));
        final Wall leftWall = new Wall(this, new LineSegment(minBoardDimensions, minBoardDimensions, minBoardDimensions, PIXELS_PER_L*PIXELS_PER_L));
        final Wall rightWall = new Wall(this, new LineSegment(PIXELS_PER_L*PIXELS_PER_L, minBoardDimensions, PIXELS_PER_L*PIXELS_PER_L, PIXELS_PER_L*PIXELS_PER_L));
        this.addGadget(topWall);
        this.addGadget(bottomWall);
        this.addGadget(leftWall);
        this.addGadget(rightWall);
        
        
        
        this.gravity = gravity;
        this.friction = new ArrayList<Double>(Arrays.asList(defaultFriction, defaultFriction));
        this.triggers = triggers;
        
        checkRep();
    }
        

    
    /**
     * Creator method: creates a new instance of the board class
     * @param name String name that is unique to the object
     * @param balls List of all balls on the board
     * @param gadgets List of all gadgets on the board
     * @param gravity decimal value of gravity to be applied to balls
     * @param friction Array of decimal values of friction to be applied to the balls
     * @param triggers Map where a Gadget key triggers another Gadget value
     */
    public Board(String name, List<Ball> balls, List<Gadget> gadgets, double gravity, ArrayList<Double> friction, Map<Gadget, Gadget> triggers) {
        this.name = name;
        this.balls = balls;
        this.gadgets = gadgets;
        
        final Wall topWall = new Wall(this, new LineSegment(minBoardDimensions, minBoardDimensions, PIXELS_PER_L*PIXELS_PER_L, minBoardDimensions));
        final Wall bottomWall = new Wall(this, new LineSegment(minBoardDimensions, PIXELS_PER_L*PIXELS_PER_L, PIXELS_PER_L*PIXELS_PER_L, PIXELS_PER_L*PIXELS_PER_L));
        final Wall leftWall = new Wall(this, new LineSegment(minBoardDimensions, minBoardDimensions, minBoardDimensions, PIXELS_PER_L*PIXELS_PER_L));
        final Wall rightWall = new Wall(this, new LineSegment(PIXELS_PER_L*PIXELS_PER_L, minBoardDimensions, PIXELS_PER_L*PIXELS_PER_L, PIXELS_PER_L*PIXELS_PER_L));
        this.addGadget(topWall);
        this.addGadget(bottomWall);
        this.addGadget(leftWall);
        this.addGadget(rightWall);
        
        this.gravity = gravity;
        this.friction = friction;
        this.triggers = triggers;
        
        checkRep();
    }
    
    public void checkRep() {
        assert(getFriction().get(0) >= minBoardDimensions);
        assert(getFriction().get(1) >= minBoardDimensions);
        assert(this.gadgets.size() >= 4);
        
        
    }
    
    /**
     * @return String name of the board
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Defensive copy to prevent rep exposure.  
     * @return a copy of the gadgets list
     */
    public List<Gadget> getGadgets(){
        List<Gadget> gadgetsCopy = new ArrayList<Gadget>(this.gadgets);
        return gadgetsCopy;
    }
    
    /**
     * Defensive copy to prevent rep exposure
     * @return a copy of the balls list
     */
    public List<Ball> getBalls(){
        List<Ball> ballsCopy = new ArrayList<Ball>(this.balls);
        return ballsCopy;
    }
    
    /**
     * Defensive copy to prevent rep exposure
     * @return a copy of the triggers dictionary
     */
    public Map<Gadget, Gadget> getTriggers(){
        Map<Gadget, Gadget> triggerCopy = new HashMap<Gadget, Gadget>(this.triggers);
        return triggerCopy;
    }
    
    /**
     * Add a relationship between g1 and g2 so that g2 responds (fires) when g1 is triggered
     * @param g1 gadget that is the trigger
     * @param g2 gadget that fires/action when trigger is hit
     * 
     */
    public void addTrigger(Gadget g1, Gadget g2) {
        triggers.put(g1, g2);
    }
    
    /**
     * Function that adds new gadgets to our list of gadgets if user wants to add another one
     * @param gadget to be added to board list of gadgets
     */
    public void addGadget(Gadget gadget) {
        this.gadgets.add(gadget);
    }
    
    /**
     * Removes the gadget specified 
     * @param gadget Gadget removed from board
     */
    public void removeGadget(Gadget gadget) {
        this.gadgets.remove(gadget);
    }
    
    /**
     * Removes a ball from the board
     * @param ball Ball to be removed from board
     */
    public void removeBall(Ball ball) {
        this.balls.remove(ball);
    }
    
    /**
     * Adds a ball to the board
     * @param ball Ball to be added to the board
     */
    public void addBall(Ball ball) {
        this.balls.add(ball);
    }
    
    /**
     * Given the name of a gadget, find the Gadget object corresponding to it
     * @param gadgetName String that is unique to the gadget
     * @return the gadget if found else null to fail fast
     */
    public Gadget getGadgetByName(String gadgetName) {
        for(Gadget g : this.gadgets) {
            if (g.getName().equals(gadgetName)){
                return g;
            }
        
        }
        return null;
        
    }
    
    /**
     * Get gravity value
     * @return gravity double of gravity
     */
    public double getGravity() {
        return this.gravity;
    }
    
    /**
     * get the coeffs of frictions
     * @return array containing coeffs of friction
     */
    public ArrayList<Double> getFriction(){
        return this.friction;
    }
    
    
    /**
     * 
     * @param gadget
     * this will be called when gadget is triggered. Fires all things that should be 
     * fired when gadget is triggered
     */
    public void activateTriggers(Gadget gadget) {
        System.out.println(triggers.size());
        for (Gadget g :triggers.keySet()) {
            if (g.equals(gadget)) {
                Absorber ab = (Absorber) triggers.get(g);
                ab.action();
            }
        }
    }
    
    
    /**
     * Compare two boards and check if they are the same.
     * Two boards are the same even if they have different names
     * Two boards are the same if they have the same walls, balls, gadgets, friction, triggers, and gravity 
     * @return true if they are the same else false
     */
    @Override 
    public boolean equals(Object obj) {
        if(!(obj instanceof Board)) {
            return false;
        }
        Board that = (Board) obj;
        
        return getBalls().size() == that.getBalls().size() && getGadgets().size() == that.getGadgets().size() && this.gravity == that.gravity
                && friction.equals(that.friction) && triggers.size() == that.triggers.size();
    }
    
    /**
     * Create an integer hash value for the board determined by the board's components
     * @return integer hash value
     */
    @Override
    public int hashCode() {
        return (int) (getBalls().hashCode() * getGadgets().hashCode() * getGravity());
    }
    
    /**
     * Create a basic human readable form of the board that dictates the number of balls
     * and bumpers on the board
     * @return String of
     */
    @Override
    public String toString() {
        return "Board " + getName() + " with " + getBalls().size() + " balls and " + getGadgets().size() + " gadgets";
    }
}
