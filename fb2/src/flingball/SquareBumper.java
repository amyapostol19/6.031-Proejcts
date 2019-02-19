package flingball;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import physics.Circle;
import physics.LineSegment;
import physics.Physics;
import physics.Vect;

/**
 * A mutable Flingball square bumper gadget.
 */
public class SquareBumper implements Bumper {
    
    private final String name;
    private final Vect position;
    private final double reflectionCoeff;
    private final List<LineSegment> lines;
    private final List<Circle> circles;
    private final List<Gadget> triggering = new ArrayList<>();
    
    /* 
     * Abstraction function:
     *     AF(name, position, reflectionCoeff, lines, circles): 
     *         a square-shaped bumper with edge length 1L, whose name is ``name``, 
     *         whose upper-left corner is at ``position``,
     *         and whose reflection coefficient is ``reflectionCoeff``.
     *         The square bumper's four sides are the four line segments in the list ``lines``, 
     *         and its four corners are the four zero-radius circles in the list ``circles``.
     *         Whenever hit by a ball, this bumper will trigger the actions of all 
     *         gadgets in the list ``triggering``.
     *         
     * Rep Invariant:
     *   - 0 <= position.x() <= 19 and 0 <= position.y() <= 19
     *   - reflectionCoeff > 0
     *   - lines has size 4 and contains the four line segments representing the four sides 
     *     of a square with edge length 1L whose upper-left corner is at position
     *   - circle has size 4 and contains four zero-radius circles centered at the four corners
     *     of a square with edge length 1L whose upper-left corner is at position
     *     
     * Safety from rep exposure:
     *   - all fields except ``triggering`` are private, immutable, and final
     *   - ``triggering`` is private and final, and its reference is never shared with clients
     *     
     */
    
    /**
     * Constructs a new square bumper with reflection coefficient 1.0,
     * whose upper-left corner is specified as (xPos,yPos) and
     * whose name is specified by the argument of the same name.
     * @param name name of the square bumper
     * @param xPos x position of the square bumper's origin
     * @param yPos y position of the square bumper's origin
     */
    public SquareBumper(final String name, final int xPos, final int yPos) {
        this(name, xPos, yPos, Bumper.DEFAULT_REFLECTION_COEFF);
    }
    
    /**
     * Constructs a new square bumper whose upper-left corner is specified as (xPos,yPos)
     * and whose name and reflection coefficient are specified by the arguments of the same name.
     * @param name name of the square bumper
     * @param xPos x position of the square bumper's origin
     * @param yPos y position of the square bumper's origin
     * @param reflectionCoeff reflection coefficient of the square bumper
     */
    public SquareBumper(final String name, final int xPos, final int yPos, final double reflectionCoeff) {
        this.name = name;
        this.position = new Vect(xPos, yPos);
        this.reflectionCoeff = reflectionCoeff;
        final Vect upperLeft = new Vect(xPos, yPos);
        final Vect upperRight = new Vect(xPos+1, yPos);
        final Vect lowerLeft = new Vect(xPos, yPos+1);
        final Vect lowerRight = new Vect(xPos+1, yPos+1);
        this.lines = Collections.unmodifiableList(Arrays.asList(new LineSegment(upperLeft, upperRight), 
                                                                new LineSegment(upperLeft, lowerLeft), 
                                                                new LineSegment(upperRight, lowerRight), 
                                                                new LineSegment(lowerLeft, lowerRight)));
        this.circles = Collections.unmodifiableList(Arrays.asList(new Circle(upperLeft, 0),
                                                                  new Circle(upperRight, 0),
                                                                  new Circle(lowerLeft, 0),
                                                                  new Circle(lowerRight, 0)));
        checkRep();
    }
    
    // checkRep
    private void checkRep() {
        assert name != null;
        
        assert position != null;
        final double xPos = position.x();
        final double yPos = position.y();
        assert 0 <= xPos && xPos <= 19;
        assert 0 <= yPos && yPos <= 19;
        
        assert reflectionCoeff > 0;
        
        final int numSides = 4;
        final Vect upperLeft = new Vect(xPos, yPos);
        final Vect upperRight = new Vect(xPos+1, yPos);
        final Vect lowerLeft = new Vect(xPos, yPos+1);
        final Vect lowerRight = new Vect(xPos+1, yPos+1);
        
        final Set<LineSegment> expectedSides = new HashSet<>(Arrays.asList(new LineSegment(upperLeft, upperRight), 
                                                                           new LineSegment(upperLeft, lowerLeft), 
                                                                           new LineSegment(upperRight, lowerRight), 
                                                                           new LineSegment(lowerLeft, lowerRight)));
        final Set<Circle> expectedCircles = new HashSet<>(Arrays.asList(new Circle(upperLeft, 0),
                                                                        new Circle(upperRight, 0),
                                                                        new Circle(lowerLeft, 0),
                                                                        new Circle(lowerRight, 0)));
        assert lines != null;
        assert lines.size() == numSides;
        assert expectedSides.equals(new HashSet<>(lines));
        
        assert circles != null;
        assert circles.size() == numSides;
        assert expectedCircles.equals(new HashSet<>(circles));
        
        assert triggering != null;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void addTrigger(final Gadget triggeredGadget) {
        triggering.add(triggeredGadget);
        checkRep();
    }
    
    @Override
    public void trigger() {
        for (final Gadget triggeredGadget : triggering) {
            triggeredGadget.respondToTrigger();
        }
        checkRep();
    }
    
    @Override
    public void respondToTrigger() {
        checkRep(); // do nothing because a circle bumper has no action for triggers
    }
    
    @Override
    public void render(final Graphics2D g) {
        checkRep();
        g.setColor(Color.CYAN);
        g.fill(new Rectangle2D.Double(position.x() * Flingball.PIXELS_PER_L, 
                                      position.y() * Flingball.PIXELS_PER_L,
                                      Flingball.PIXELS_PER_L, Flingball.PIXELS_PER_L));
    }
    
    @Override
    public boolean resolveCollision(final Ball ball) {
        /*
         * Call the appropriate timeUntilCollision() methods to calculate 
         * the times at which the ball will collide with each of lines/circles. 
         * The minimum of all these times is the time of the next collision.
         */
        long mintimeLine = Long.MAX_VALUE;
        LineSegment nextCollidingLine = null;
        for (int i = 0; i < lines.size(); i++) {
            final long collisionTime = (long) (Physics.timeUntilWallCollision(lines.get(i), ball.getCircle(), ball.getVelocity())*1000);
            if (collisionTime < mintimeLine) {
                mintimeLine = collisionTime;
                nextCollidingLine = lines.get(i);
            }
        }
        long mintimeCircle = Long.MAX_VALUE;
        Circle nextCollidingCircle = null;
        for (int i = 0; i < circles.size(); i++) {
            final long collisionTime = (long) (Physics.timeUntilCircleCollision(circles.get(i), ball.getCircle(), ball.getVelocity())*1000);
            if (collisionTime < mintimeCircle) {
                mintimeCircle = collisionTime;
                nextCollidingCircle = circles.get(i);
            }
        }
        
        if (mintimeLine <= mintimeCircle && mintimeLine <= 1) {
            // call reflectWall() to calculate the change in the ball's velocity
            final Vect newVelocity = Physics.reflectWall(nextCollidingLine, ball.getVelocity(), reflectionCoeff);
            ball.setVelocity(newVelocity); // updates the ball's velocity
            trigger(); // trigger upon collision
            return true;
        }
        else if (mintimeLine > mintimeCircle && mintimeCircle <= 1) {
            // call reflectWall() to calculate the change in the ball's velocity
            final Vect newVelocity = Physics.reflectCircle(nextCollidingCircle.getCenter(), ball.getPosition(), ball.getVelocity(), reflectionCoeff);
            ball.setVelocity(newVelocity); // updates the ball's velocity
            trigger(); // trigger upon collision
            return true;
        }
        return false; // no collision
    }

    @Override
    public List<LineSegment> getLineSegments() {
        checkRep();
        return new ArrayList<>(lines);
    }
    
    @Override
    public double getHeight() {
        LineSegment line = lines.get(0);
        return line.length();
    }
    
    @Override
    public double getWidth() {
        LineSegment line = lines.get(0);
        return line.length();
    }
    
    @Override
    public List<Circle> getCircles() {
        checkRep();
        return new ArrayList<>(circles);
    }
    
    @Override
    public double getReflectionCoeff() {
        checkRep();
        return reflectionCoeff;
    }
    
    @Override
    public Vect getPosition() {
        checkRep();
        return position;
    }
    
    @Override 
    public boolean sameValue(final Object that) {
        checkRep();
        if (!(that instanceof SquareBumper)) return false;
        final SquareBumper other = (SquareBumper) that;
        if (this.name.equals(other.name) 
            && this.position.equals(other.position)
            && this.reflectionCoeff == other.reflectionCoeff
            && this.triggering.size() == other.triggering.size()) {
            for (int i = 0; i < this.triggering.size(); i++) {
                if (!this.triggering.get(i).getName().equals(other.triggering.get(i).getName()))
                    return false;
            }
            return true;
        }
        return false;
    }
    
    @Override 
    public String toString() {
        checkRep();
        final StringBuilder string = new StringBuilder();
        final Formatter formatter = new Formatter(string);
        formatter.format("SquareBumper <%s> at position %s with reflectionCoeff=%f\n", 
                         name, position, reflectionCoeff);
        formatter.format("Triggering %d gadgets\n", triggering.size());
        for (final Gadget gadget : triggering) {
            formatter.format("  - %s\n", gadget.getName());
        }
        string.deleteCharAt(string.length() - 1); // delete last new line character
        return string.toString();
    }
    
}
