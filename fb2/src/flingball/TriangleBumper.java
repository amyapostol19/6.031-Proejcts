package flingball;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
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
 * A mutable Flingball triangle bumper gadget.
 */
public class TriangleBumper implements Bumper {
    
    private static final int DEFAULT_ORIENTATION = 0;
    
    private final String name;
    private final Vect position;
    private final double reflectionCoeff;
    private final List<LineSegment> lines;
    private final List<Circle> circles;
    private final int orientation;
    private final List<Gadget> triggering = new ArrayList<>();
    
    /* 
     * Abstraction function:
     *     AF(name, position, reflectionCoeff, lines, circles, orientation): 
     *         a triangle-shaped bumper with leg length 1L, whose name is ``name``, 
     *         whose upper-left corner is at ``position``,
     *         and whose reflection coefficient is ``reflectionCoeff``.
     *         The triangle bumper's sides are the line segments in the list ``lines``, 
     *         and its corners are the zero-radius circles in the list ``circles``.
     *         The triangle bumper is rotated clockwise ``orientation`` degrees around 
     *         the center of its 1L x 1L bounding box. 
     *         Whenever hit by a ball, this bumper will trigger the actions of all 
     *         gadgets in the list ``triggering``.
     *         
     * Rep Invariant:
     *   - 0 <= position.x() <= 19 and 0 <= position.y() <= 19
     *   - reflectionCoeff > 0
     *   - orientation is 0, 90, 180, or 270
     *   - lines has size 3 and contains the three line segments representing the three sides 
     *     of a right triangle with leg length 1L whose rotation is orientation degrees clockwise
     *     and whose bounding box's upper-left corner is at position
     *   - circle has size 3 and contains three zero-radius circles centered at the corners
     *     of the triangle whose rotation is orientation degrees clockwise and 
     *     whose bounding box's upper-left corner is at position
     *     
     * Safety from rep exposure:
     *   - all fields except ``triggering`` are private, immutable, and final
     *   - ``triggering`` is private and final, and its reference is never shared with clients
     *     
     */
    
    /**
     * Constructs a new circle bumper with reflection coefficient 1.0 and orientation 0 degree,
     * whose origin (upper-left corner of the bounding box) is specified as (xPos,yPos)
     * and whose name is specified by the argument of the same name.
     * @param name name of the circle bumper
     * @param xPos x position of the circle bumper
     * @param yPos y position of the circle bumper
     */
    public TriangleBumper(final String name, final int xPos, final int yPos) {
        this(name, xPos, yPos, DEFAULT_ORIENTATION, Bumper.DEFAULT_REFLECTION_COEFF);
    }
    
    /**
     * Constructs a new circle bumper with reflection coefficient 1.0 and orientation 0 degree,
     * whose origin (upper-left corner of the bounding box) is specified as (xPos,yPos)
     * and whose name and orientation are specified by the arguments of the same name.
     * @param name name of the circle bumper
     * @param xPos x position of the circle bumper
     * @param yPos y position of the circle bumper
     * @param orientation clockwise rotation in degrees around the center of the bounding box of the triangle bumper
     */
    public TriangleBumper(final String name, final int xPos, final int yPos, final int orientation) {
        this(name, xPos, yPos, orientation, Bumper.DEFAULT_REFLECTION_COEFF);
    }
    
    /**
     * Constructs a new triangle bumper 
     * whose origin (upper-left corner of the bounding box) is specified as (xPos,yPos)
     * and whose name, reflection coefficient, and orientation are specified by the arguments of the same name.
     * @param name name of the triangle bumper
     * @param xPos x position of the triangle bumper
     * @param yPos y position of the triangle bumper
     * @param orientation clockwise rotation in degrees around the center of the bounding box of the triangle bumper
     * @param reflectionCoeff reflection coefficient of the triangle bumper
     */
    public TriangleBumper(final String name, final int xPos, final int yPos, final int orientation, final double reflectionCoeff) {
        this.name = name;
        this.position = new Vect(xPos, yPos); // note this is the position of the upper left corner of its bounding box
        this.reflectionCoeff = reflectionCoeff;
        this.orientation = orientation;
        
        final Vect upperLeft = new Vect(xPos, yPos);
        final Vect upperRight = new Vect(xPos+1, yPos);
        final Vect lowerLeft = new Vect(xPos, yPos+1);
        final Vect lowerRight = new Vect(xPos+1, yPos+1);
        
        final int rightAngle = 90;
        final int straightAngle = 180;
        
        if (orientation == 0) {
            this.lines = Collections.unmodifiableList(Arrays.asList(new LineSegment(upperLeft, upperRight), 
                                                                    new LineSegment(upperLeft, lowerLeft), 
                                                                    new LineSegment(lowerLeft, upperRight)));
            this.circles = Collections.unmodifiableList(Arrays.asList(new Circle(upperLeft, 0),
                                                                      new Circle(upperRight, 0),
                                                                      new Circle(lowerLeft, 0)));
        } else if (orientation == rightAngle) {
            this.lines = Collections.unmodifiableList(Arrays.asList(new LineSegment(upperLeft, upperRight), 
                                                                    new LineSegment(upperLeft, lowerRight), 
                                                                    new LineSegment(lowerRight, upperRight)));
            this.circles = Collections.unmodifiableList(Arrays.asList(new Circle(upperLeft, 0),
                                                                      new Circle(upperRight, 0),
                                                                      new Circle(lowerRight, 0)));
        } else if (orientation == straightAngle) {
            this.lines = Collections.unmodifiableList(Arrays.asList(new LineSegment(lowerLeft, upperRight), 
                                                                    new LineSegment(lowerLeft, lowerRight), 
                                                                    new LineSegment(lowerRight, upperRight)));
            this.circles = Collections.unmodifiableList(Arrays.asList(new Circle(lowerLeft, 0),
                                                                      new Circle(upperRight, 0),
                                                                      new Circle(lowerRight, 0)));
        } else {
            this.lines = Collections.unmodifiableList(Arrays.asList(new LineSegment(lowerLeft, upperLeft), 
                                                                    new LineSegment(lowerLeft, lowerRight), 
                                                                    new LineSegment(upperLeft, lowerRight)));
            this.circles = Collections.unmodifiableList(Arrays.asList(new Circle(lowerLeft, 0),
                                                                      new Circle(upperLeft, 0),
                                                                      new Circle(lowerRight, 0)));
        }
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
        
        final int numSides = 3;
        final Vect upperLeft = new Vect(xPos, yPos);
        final Vect upperRight = new Vect(xPos+1, yPos);
        final Vect lowerLeft = new Vect(xPos, yPos+1);
        final Vect lowerRight = new Vect(xPos+1, yPos+1);
        
        final Set<LineSegment> expectedSides;
        final Set<Circle> expectedCircles;
        
        final int rightAngle = 90;
        final int straightAngle = 180;
        final int reflexAngle = 270;
        
        if (orientation == 0) {
            expectedSides = new HashSet<>(Arrays.asList(new LineSegment(upperLeft, upperRight), 
                                                        new LineSegment(upperLeft, lowerLeft), 
                                                        new LineSegment(lowerLeft, upperRight)));
            expectedCircles = new HashSet<>(Arrays.asList(new Circle(upperLeft, 0),
                                                          new Circle(upperRight, 0),
                                                          new Circle(lowerLeft, 0)));
        } else if (orientation == rightAngle) {
            expectedSides = new HashSet<>(Arrays.asList(new LineSegment(upperLeft, upperRight), 
                                                        new LineSegment(upperLeft, lowerRight), 
                                                        new LineSegment(lowerRight, upperRight)));
            expectedCircles = new HashSet<>(Arrays.asList(new Circle(upperLeft, 0),
                                                          new Circle(upperRight, 0),
                                                          new Circle(lowerRight, 0)));
        } else if (orientation == straightAngle) {
            expectedSides = new HashSet<>(Arrays.asList(new LineSegment(lowerLeft, upperRight), 
                                                        new LineSegment(lowerLeft, lowerRight), 
                                                        new LineSegment(lowerRight, upperRight)));
            expectedCircles = new HashSet<>(Arrays.asList(new Circle(lowerLeft, 0),
                                                          new Circle(upperRight, 0),
                                                          new Circle(lowerRight, 0)));
        } else {
            assert orientation == reflexAngle;
            expectedSides = new HashSet<>(Arrays.asList(new LineSegment(lowerLeft, upperLeft), 
                                                        new LineSegment(lowerLeft, lowerRight), 
                                                        new LineSegment(upperLeft, lowerRight)));
            expectedCircles = new HashSet<>(Arrays.asList(new Circle(lowerLeft, 0),
                                                          new Circle(upperLeft, 0),
                                                          new Circle(lowerRight, 0)));
        }
        
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
        final int npoints = circles.size();
        final int[] xpoints = new int[npoints];
        final int[] ypoints = new int[npoints];
        for (int i = 0; i < npoints; i++) {
            final Circle circle = circles.get(i);
            xpoints[i] = (int) circle.getCenter().x() * Flingball.PIXELS_PER_L;
            ypoints[i] = (int) circle.getCenter().y() * Flingball.PIXELS_PER_L;
        }
        g.setColor(Color.BLUE);
        g.fill(new Polygon(xpoints, ypoints, npoints));
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
    public double getHeight(){
        return lines.get(0).length();
    }
    
    @Override
    public double getWidth() {
        return lines.get(0).length();
    }
    
    @Override 
    public boolean sameValue(final Object that) {
        checkRep();
        if (!(that instanceof TriangleBumper)) return false;
        final TriangleBumper other = (TriangleBumper) that;
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
        formatter.format("TriangleBumper <%s> at position %s with reflectionCoeff=%f and orientation %d degrees\n", 
                         name, position, reflectionCoeff, orientation);
        formatter.format("Triggering %d gadgets\n", triggering.size());
        for (final Gadget gadget : triggering) {
            formatter.format("  - %s\n", gadget.getName());
        }
        string.deleteCharAt(string.length() - 1); // delete last new line character
        return string.toString();
    }
    
}
