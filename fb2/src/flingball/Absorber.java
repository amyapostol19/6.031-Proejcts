package flingball;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

import physics.Circle;
import physics.LineSegment;
import physics.Physics;
import physics.Vect;

/**
 * A mutable data type representing a Flingball absorber gadget.
 */
public class Absorber implements Gadget {
    
    private final String name;
    private final Vect position;
    private final int width;
    private final int height;
    private final List<LineSegment> lines;
    private final List<Circle> circles;
    private final List<Gadget> triggering = new ArrayList<>();
    private final List<Ball> balls = new LinkedList<>();
    
    /* 
     * Abstraction function:
     *     AF(name, position, width, height, lines, circles, triggering): 
     *         an absorber shaped as a rectangle ``width``L wide and ``height``L tall, 
     *         whose name is ``name``, whose upper-left corner is at ``position``.
     *         The absorber's four sides are the four line segments in the list ``lines``, 
     *         and its four corners are the four zero-radius circles in the list ``circles``.
     *         Whenever hit by a ball, this absorber will trigger the actions of all 
     *         gadgets in the list ``triggering`` in addition to capturing the ball.
     *         All balls that this absorber is currently holding are in the list ``balls``.
     *         
     * Rep Invariant:
     *   - 0 <= position.x() <= 19 and 0 <= position.y() <= 19
     *   - width > 0, height > 0
     *   - position.x() + width <= 20, position.y() + height <= 20
     *   - lines has size 4 and contains the four line segments representing the four sides 
     *     of a rectangle widthL wide and heightL tall whose upper-left corner is at position
     *   - circle has size 4 and contains four zero-radius circles centered at the four corners
     *     of a rectangle widthL wide and heightL tall whose upper-left corner is at position
     *     
     * Safety from rep exposure:
     *   - all fields except ``triggering`` and ``balls`` are private, immutable, and final
     *   - ``triggering`` and ``balls`` are private and final, and their references are never shared with clients
     *     
     */
    
    /**
     * Constructs a new absorber, whose origin (upper-left corner of the bounding box) 
     * is specified as (xPos, yPos), and whose name, width, and height are
     * specified by the arguments of the same name.
     * @param name name of the absorber
     * @param xPos x position of the absorber
     * @param yPos y position of the absorber
     * @param width width of the absorber
     * @param height height of the absorber
     */
    public Absorber(final String name, final int xPos, final int yPos, final int width, final int height) {
        this.name = name;
        this.position = new Vect(xPos, yPos);
        this.width = width;
        this.height = height;
        final Vect upperLeft = new Vect(xPos, yPos);
        final Vect upperRight = new Vect(xPos+width, yPos);
        final Vect lowerLeft = new Vect(xPos, yPos+height);
        final Vect lowerRight = new Vect(xPos+width, yPos+height);
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
 
        assert width > 0 && height > 0;
        assert xPos + width <= 20 && yPos + height <= 20;
        
        final int numSides = 4;
        final Vect upperLeft = new Vect(xPos, yPos);
        final Vect upperRight = new Vect(xPos+width, yPos);
        final Vect lowerLeft = new Vect(xPos, yPos+height);
        final Vect lowerRight = new Vect(xPos+width, yPos+height);
        
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
        assert balls != null;
    }
    
    /** @return set of the name of balls that this absorber currently holds */
    public Set<String> getBallsNames(){
        checkRep();
        final HashSet<String> ballsNames = new HashSet<>();
        for (Ball ball : balls) ballsNames.add(ball.getName());
        return ballsNames;
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
        if (!balls.isEmpty() && !balls.get(0).getExitingAbsorber()) {
            final Ball ball = balls.get(0);
            final double shootOutVelocity = -50.;
            ball.setVelocity(new Vect(0, shootOutVelocity));
            ball.setAbsorbed(false);
            ball.setExitingAbsorber(true);
        }
    }
    
    @Override
    public void render(final Graphics2D g) {
        checkRep();
        g.setColor(Color.YELLOW);
        g.fill(new Rectangle2D.Double(position.x() * Flingball.PIXELS_PER_L, 
                                      position.y() * Flingball.PIXELS_PER_L,
                                      width * Flingball.PIXELS_PER_L, 
                                      height * Flingball.PIXELS_PER_L));
    }
    
    @Override
    public boolean resolveCollision(final Ball ball) {
        // If the ball has not yet left this absorber,
        // do not count as another collision
        if (ball.getExitingAbsorber() && balls.contains(ball)) {
            assert balls.get(0).equals(ball);
            if (ball.getPosition().y() <= getPosition().y() 
                || ball.getPosition().y() >= getPosition().y() + height ) {
                balls.remove(0);
                ball.setExitingAbsorber(false);
            }
            return false;
        }
        // Otherwise:
        /* Call the appropriate timeUntilCollision() methods to calculate 
         * the times at which the ball will collide with each of lines/circles. 
         * The minimum of all these times is the time of the next collision.
         */
        long mintimeLine = Long.MAX_VALUE;
        for (int i = 0; i < lines.size(); i++) {
            final long collisionTime = (long) (Physics.timeUntilWallCollision(lines.get(i), ball.getCircle(), ball.getVelocity())*1000);
            if (collisionTime < mintimeLine) mintimeLine = collisionTime;
        }
        long mintimeCircle = Long.MAX_VALUE;
        for (int i = 0; i < circles.size(); i++) {
            final long collisionTime = (long) (Physics.timeUntilCircleCollision(circles.get(i), ball.getCircle(), ball.getVelocity())*1000);
            if (collisionTime < mintimeCircle) mintimeCircle = collisionTime;
        }
        
        if (mintimeLine <= 1 || mintimeCircle <= 1) {
            ball.setAbsorbed(true);
            final double posOffset = 0.25;
            final Vect newPosition = position.plus(new Vect(width - posOffset, height - posOffset));
            ball.setPosition(newPosition);
            final Vect newVelocity = new Vect(0, 0); // capture ball
            ball.setVelocity(newVelocity); // updates the ball's velocity
            balls.add(ball);
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
    public Vect getPosition() {
        return position;
    }
    
    @Override
    public double getHeight() {
        return height;
    }
    
    @Override
    public double getWidth() {
        return width;
    }
   
    @Override 
    public boolean sameValue(final Object that)  {
        checkRep();
        if (!(that instanceof Absorber)) return false;
        final Absorber other = (Absorber) that;
        if (this.name.equals(other.name) 
            && this.position.equals(other.position)
            && this.width == other.width
            && this.height == other.height
            && this.triggering.size() == other.triggering.size()
            && this.balls.size() == other.balls.size()) {
            for (int i = 0; i < this.triggering.size(); i++) {
                if (!this.triggering.get(i).getName().equals(other.triggering.get(i).getName()))
                    return false;
            }
            for (int i = 0; i < this.balls.size(); i++) {
                if (!this.balls.get(i).sameValue(other.balls.get(i)))
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
        formatter.format("Absorber <%s> at position %s with width=%dL and height=%dL\n", 
                         name, position, width, height);
        formatter.format("Triggering %d gadgets\n", triggering.size());
        for (final Gadget gadget : triggering) {
            formatter.format("  - %s\n", gadget.getName());
        }
        string.deleteCharAt(string.length() - 1); // delete last new line character
        return string.toString();
    }

}

