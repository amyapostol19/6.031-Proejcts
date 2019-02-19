package flingball;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import physics.Circle;
import physics.LineSegment;
import physics.Physics;
import physics.Vect;

/**
 * A mutable Flingball circle bumper gadget.
 */
public class CircleBumper implements Bumper {
    
    private static final double RADIUS = 0.5;
    
    private final String name;
    private final Vect position;
    private final double reflectionCoeff;
    private final List<Circle> circles;
    private final List<Gadget> triggering = new ArrayList<>();
    
    /* 
     * Abstraction function:
     *     AF(name, position, reflectionCoeff, lines, circles, triggering): 
     *         a circle-shaped bumper with diameter length 1L, whose name is ``name``, 
     *         whose origin (upper-left corner of the bounding box) is at ``position``,
     *         and whose reflection coefficient is ``reflectionCoeff``.
     *         The circle representing this bumper's shape is in the list ``circles``.
     *         Whenever hit by a ball, this bumper will trigger the actions of all 
     *         gadgets in the list ``triggering``.
     *         
     * Rep Invariant:
     *   - 0 <= position.x() <= 19 and 0 <= position.y() <= 19
     *   - reflectionCoeff > 0
     *   - circle has size 1 and contains a 1L-diameter circle centered at (position.x()+0.5, position.y()+0.5)
     *     
     * Safety from rep exposure:
     *   - all fields except ``triggering`` are private, immutable, and final
     *   - ``triggering`` is private and final, and its reference is never shared with clients
     *     
     */
    
    /**
     * Constructs a new circle bumper with reflection coefficient 1.0,
     * whose origin (upper-left corner of the bounding box) is specified as (xPos, yPos)
     * and whose name is specified by the argument of the same name.
     * @param name name of the circle bumper
     * @param xPos x position of the circle bumper
     * @param yPos y position of the circle bumper
     */
    public CircleBumper(final String name, final int xPos, final int yPos) {
        this(name, xPos, yPos, Bumper.DEFAULT_REFLECTION_COEFF);
    }
    
    /**
     * Constructs a new square bumper 
     * whose origin (upper-left corner of the bounding box) is specified as (xPos,yPos)
     * and whose name and reflection coefficient are specified by the arguments of the same name.
     * @param name name of the square bumper
     * @param xPos x position of the square bumper
     * @param yPos y position of the square bumper
     * @param reflectionCoeff reflection coefficient of the square bumper
     */
    public CircleBumper(final String name, final int xPos, final int yPos, final double reflectionCoeff) {
        this.name = name;
        this.position = new Vect(xPos, yPos);
        this.reflectionCoeff = reflectionCoeff;
        this.circles = Collections.singletonList(new Circle(xPos+RADIUS, yPos+RADIUS, RADIUS));
        checkRep();
    }
    
    // checkRep
    private void checkRep() {
        assert name != null;
        
        assert position != null;
        final int posUpperBound = 19;
        final double xPos = position.x();
        final double yPos = position.y();
        assert 0 <= xPos && xPos <= posUpperBound;
        assert 0 <= yPos && yPos <= posUpperBound;
        
        assert reflectionCoeff > 0;
        
        assert circles != null;
        assert circles.size() == 1;
        assert new Circle(xPos+RADIUS, yPos+RADIUS, RADIUS).equals(circles.get(0));
        
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
        g.setColor(Color.GREEN);
        g.fill(new Ellipse2D.Double(position.x() * Flingball.PIXELS_PER_L, 
                                    position.y() * Flingball.PIXELS_PER_L,
                                    Flingball.PIXELS_PER_L, Flingball.PIXELS_PER_L));
    }
    
    @Override
    public boolean resolveCollision(final Ball ball) {
        /*
         * A note on implemenatation:
         *      1) call Physics.timeUntilWallCollision() to calculate the times at which the ball is colliding with each the lines/circles
         *      2) the minimum of all these times (call it "mintime") is the time of the next collision; also save the colliding line/circle to a variable
         *      3) mintime is in seconds, but our time scale is milliseconds, so time 1000 to it 
         *      4) if mintime <= 1 (or < 1?), there is a collision
         *          5) call one of the appropriate reflect method in Physics to resolve collision + update ball's new velocity, return true
         *      6) else, do nothing and return false
         *      
         */
        final Circle nextCollidingCircle = circles.get(0);
        final double seconds = Physics.timeUntilCircleCollision(nextCollidingCircle, ball.getCircle(), ball.getVelocity());
        final long mintimeCircle = (long) (seconds *1000);
        if (mintimeCircle <= 1) {
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
        return new ArrayList<>();
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
    public double getHeight() {
        return 2*RADIUS;
    }
    
    @Override
    public double getWidth() {
        return 2*RADIUS;
    }
    
    @Override 
    public boolean sameValue(final Object that) {
        checkRep();
        if (!(that instanceof CircleBumper)) return false;
        final CircleBumper other = (CircleBumper) that;
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
        formatter.format("CircleBumper <%s> at position %s with reflectionCoeff=%f\n", 
                         name, position, reflectionCoeff);
        formatter.format("Triggering %d gadgets\n", triggering.size());
        for (final Gadget gadget : triggering) {
            formatter.format("  - %s\n", gadget.getName());
        }
        string.deleteCharAt(string.length() - 1); // delete last new line character
        return string.toString();
    }

}

