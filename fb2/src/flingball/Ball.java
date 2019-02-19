package flingball;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.Formatter;

import physics.Circle;
import physics.Vect;

/**
 * A mutable type representing the Flingball ball.
 */
public class Ball {
    private static final double RADIUS = 0.25;
    private static final double POS_LOWER_BOUND = 0.25;
    private static final double POS_UPPER_BOUND = 19.75;
    
    private final String name;
    private Vect pos;
    private Vect velocity;
    private boolean absorbed;
    private boolean exitingAbsorber;
    private boolean exitingPortal;
    
    /* 
     * Abstraction function:
     *     AF(name, pos, velocity, absorbed, exitingAbsorber,exitingPortal): 
     *         a Flingball ball, whose name is ``name``.
     *         The ball's center is at the location represented by the vector ``pos``,
     *         and the ball has velocity represented by the vector ``velocity``.
     *         If ``absorbed`` is true, then the ball is currently captured by an absorber;
     *         otherwise, the ball is free to move.
     *         If ``exitingAbsorder`` is true, then the ball is currently exiting an absorber
     *         and cannot be absorbed again until ``exitingAbsorder`` becomes false.
     *         If ``exitingPortal`` is true, then the ball is currently exiting a portal
     *         and cannot be teleported again until ``exitingAbsorder`` becomes false.
     * Rep Invariant:
     *   - 0.25 <= position.x() <= 19.75 and 0.25 <= position.y() <= 19.75
     *     
     * Safety from rep exposure:
     *   - ``name`` is private, immutable, and final
     *   - ``pos``, ``velocity``, ``absorbed``, and ``exitingAbsorber`` ``exitingPortal``are private and immutable,
     *     so returning their references to clients will not result in clients' inadvertently 
     *     mutating the rep of the board. However, clients are allowed to reassign the above 
     *     variable's values with the appropriate setter methods.
     *     
     */
    
    /**
     * Creates a new ball at the specified position traveling 
     * at the specified speed along the specified angle.
     * @param posX initial x position of the ball
     * @param posY initial x position of the ball
     * @param xVelocity ball's initial velocity in the horizontal direction
     * @param yVelocity ball's initial velocity in the vertical direction
     */
    public Ball(final String name, final double posX, final double posY, final double xVelocity, final double yVelocity) {
        this.name = name;
        this.pos = new Vect(posX, posY);
        this.velocity = new Vect(xVelocity, yVelocity);
        this.absorbed = false;
        this.exitingAbsorber = false;
        checkRep();
    }
    
    // checkRep
    private void checkRep() {
        assert name != null;
        assert pos != null;
        final double xPos = pos.x();
        final double yPos = pos.y();
        assert POS_LOWER_BOUND <= xPos && xPos <= POS_UPPER_BOUND;
        assert POS_LOWER_BOUND <= yPos && yPos <= POS_UPPER_BOUND;
        assert velocity != null;
    }
    
    /**
     * Displays this ball on the window.
     * @param g graphics for the drawing buffer for the window. 
     *          Modifies this graphics by drawing the state of the ball on it.
     */
    public void render(final Graphics2D g) {
        checkRep();
        if (!absorbed) { // only display ball if not absorbed
            final double diameter = (RADIUS + RADIUS) * Flingball.PIXELS_PER_L;
            g.setColor(Color.RED);
            g.fill(new Ellipse2D.Double((pos.x() - RADIUS) * Flingball.PIXELS_PER_L, 
                                         (pos.y() - RADIUS) * Flingball.PIXELS_PER_L, 
                                         diameter, diameter));
        }
    }
    
    /**
     * Updates this ball's position to reflect its movement during the specified period of time,
     * according to the rules of Flingball physics.
     * @param elapsedTime length of time in milliseconds
     * @param board board context on which the ball is moving
     */
    public void move(final long elapsedTime, final Board board) {
        if (!absorbed) {
            final double timeInSeconds = (double) elapsedTime / 1000;
            Vect newPos = pos.plus(velocity.times(timeInSeconds));
            // Cap newPos to prevent ball from going out of bounds
            newPos = new Vect(Math.min(Math.max(newPos.x(), POS_LOWER_BOUND), POS_UPPER_BOUND), 
                              Math.min(Math.max(newPos.y(), POS_LOWER_BOUND), POS_UPPER_BOUND));
            setPosition(newPos);
            Vect newVelocity = velocity.plus(new Vect(0, board.getGravity()).times(timeInSeconds)); // plus b/c positive direction points down
            newVelocity = newVelocity.times(1 - board.getFriction1()*timeInSeconds 
                                              - board.getFriction2()*velocity.length()*timeInSeconds);
            setVelocity(newVelocity);
        }
        checkRep();
    }
    
    /**
     * Sets the ball's position to a new position.
     * @param newPos ball's new position
     */
    public void setPosition(final Vect newPos) {
        pos = newPos;
        checkRep();
    }
    
    /**
     * Sets the ball's velocity to a new velocity.
     * @param newVelocity ball's new velocity
     */
    public void setVelocity(final Vect newVelocity) {
        velocity = newVelocity;
        checkRep();
    }
    
    /**
     * Add gravity to the velocity vector of the ball and set the ball's new velocity
     * @param gravity double quantity of velocity in the y direction
     */
    public void addGravity(double gravity) {
        
    }
    
    /**
     * Calculate the coefficient of friction and apply it to the ball. Change the ball's
     * velocity with friction applied
     * @param friction1 : double quantity of friction to be applied in direction
     *                      opposite of movement
     * @param friction2 : double quantity of friction to be applied in direction
     *                      opposite of movement
     */
    public void addFriction(double friction1, double friction2) {
        
    }
    
    /**
     * Sets the ball's absorbed state to the new specified state.
     * @param newAbsorbed ball's new absorbedState
     */
    public void setAbsorbed(final boolean newAbsorbed) {
        absorbed = newAbsorbed;
        checkRep();
    }
    
    /**
     * Sets the ball's existing absorber state to the new specified state.
     * @param newExitingAbsorber true if ball is exiting absorber, false otherwise
     */
    public void setExitingAbsorber(final boolean newExitingAbsorber) {
        exitingAbsorber = newExitingAbsorber;
        checkRep();
    }
    /**
     * Sets the ball's existing absorber state to the new specified state.
     * @param newExitingPortal true if ball is exiting absorber, false otherwise
     */
    public void setExitingPortal(final boolean newExitingPortal) {
        exitingPortal = newExitingPortal;
        checkRep();
    }
    /** @return ball's name */
    public String getName() {
        checkRep();
        return name;
    }
    
    /** @return ball's circle */
    public Circle getCircle() {
        checkRep();
        return new Circle(pos, RADIUS);
    }
    
    /** @return ball's velocity */
    public Vect getVelocity() {
        checkRep();
        return velocity;
    }
    
    
    /** @return ball's current position */
    public Vect getPosition() {
        checkRep();
        return pos;
    }
    
    /** 
     * @return true if this ball is held by an absorber, 
     *         false otherwise. 
     */
    public boolean getAbsorbed() {
        checkRep();
        return absorbed;
    }
    
    /** 
     * @return true if this ball is exiting an absorber, 
     *         false otherwise. 
     */
    public boolean getExitingAbsorber() {
        checkRep();
        return exitingAbsorber;
    }
    /** 
     * @return true if this ball is exiting an portal, 
     *         false otherwise. 
     */
    public boolean getExitingPortal() {
        checkRep();
        return exitingPortal;
    }
    
    /**
     * Indicates whether some other object has the same value as this ball.
     * @param that an object with which to compare
     * @return true if that object is observationally equal to this one,
     *         false otherwise.
     */
    public boolean sameValue(final Object that)  {
        checkRep();
        if (!(that instanceof Ball)) return false;
        final Ball other = (Ball) that;
        return this.name.equals(other.name)
               && this.pos.equals(other.pos)
               && this.velocity.equals(other.velocity)
               && this.absorbed == other.absorbed
               && this.exitingAbsorber == other.exitingAbsorber;
    }
    
    @Override 
    public String toString() {
        checkRep();
        final StringBuilder string = new StringBuilder();
        final Formatter formatter = new Formatter(string);
        formatter.format("Ball <%s> at position %s with velocity %s (absorbed=%B, exitingAbsorber=%B)\n", 
                         name, pos, velocity, absorbed, exitingAbsorber);
        return string.toString();
    }

}
