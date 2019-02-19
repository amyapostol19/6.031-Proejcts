package flingball;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import physics.Circle;
import physics.LineSegment;
import physics.Physics;
import physics.Vect;

public class Portal implements Gadget{
    
    public final double RADIUS = .5;
    
    private final String name;
    private final Vect position;
    private final Circle circle;
    private final Optional<String> otherBoard;
    private final String otherPortal;
    private final List<Gadget> triggering = new ArrayList<>();
    private final List<Ball> balls = new LinkedList<>();
    private final Board board;
    
    /* 
     * Abstraction function:
     *     AF(name, position, circle, otherBoard, otherPortal, triggering, balls, board): 
     *         a circle-shaped portal with diameter length 1L, whose name is ``name``, 
     *         whose origin (upper-left corner of the bounding box) is at ``position``.
     *         If otherBoard.isPresent() is true, then otherBoard.get() is the name of the distinct
     *         board that contains the portal that this portal sends balls to, else the portal is
     *         linked to a portal on the same board.
     *         ``otherPortal`` is the name of the portal to which this portal will send balls.
     *         The circle representing this bumper's shape is ``circle``.
     *         Whenever hit by a ball, this bumper will trigger the actions of all 
     *         gadgets in the list ``triggering``.
     *         All balls that this portal is currently holding are in the list ``balls``.
     *         The board this portal is present in is ``board``.
     *         
     * Rep Invariant:
     *   - 0 <= position.x() <= 19 and 0 <= position.y() <= 19
     *   - circle has size 1 and contains a 1L-diameter circle centered at (position.x()+0.5, position.y()+0.5)
     *     
     * Safety from rep exposure:
     *   - all fields except ``triggering`` and ``balls`` are private, immutable, and final
     *   - ``triggering`` and ``balls`` are private and final, and its reference is never shared with clients
     *     
     */
    
    /**
     * Creates a new Portal, constructing it such that the portal must be linked to a portal on
     * the same board. 
     * @param name String of portal's name
     * @param xPos x-coordinate of upper left corner
     * @param yPos y-coordinate of upper left corner
     * @param board Board within which this portal is located
     * @param otherPortal portal to which this portal is linked
     */
    public Portal(final String name, final int xPos, final int yPos, Board board, String otherPortal) {
        this.name = name;
        this.position = new Vect(xPos,yPos);
        this.circle = new Circle(new Vect(xPos+RADIUS,yPos+RADIUS),RADIUS);
        this.otherBoard = Optional.empty();
        this.otherPortal = otherPortal;
        this.board = board;
        checkRep();
    }
    /**
     * Creates a new Portal, constructing it such that the portal must be linked to a portal on
     * a different board. 
     * @param name String of portal's name
     * @param xPos x-coordinate of upper left corner
     * @param yPos y-coordinate of upper left corner
     * @param board Board within which this portal is located
     * @param otherPortal portal to which this portal is linked
     * @param otherBoard String of other board's name
     */
    public Portal(final String name, final int xPos, final int yPos, Board board, String otherPortal, String otherBoard) {
        this.name = name;
        this.position = new Vect(xPos,yPos);
        this.circle = new Circle(new Vect(xPos,yPos),RADIUS);
        this.otherBoard = Optional.of(otherBoard);
        this.otherPortal = otherPortal; 
        this.board = board;
        checkRep();
    }
    
    // checkRep
    /**
     * Checks if rep invariant is obeyed
     */
    private void checkRep() {
        assert name != null;
        
        assert position != null;
        final int posUpperBound = 19;
        final double xPos = position.x();
        final double yPos = position.y();
        assert 0 <= xPos && xPos <= posUpperBound;
        assert 0 <= yPos && yPos <= posUpperBound;
        
        assert circle != null;
        assert new Circle(xPos+RADIUS, yPos+RADIUS, RADIUS).equals(circle);
        
        assert triggering != null;
    }
    
    @Override
    public void render(final Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fill(new Ellipse2D.Double(position.x() * Flingball.PIXELS_PER_L, 
                                    position.y() * Flingball.PIXELS_PER_L,
                                    Flingball.PIXELS_PER_L, Flingball.PIXELS_PER_L));
    }

    public boolean resolveCollision(Ball ball) {
        if (ball.getExitingPortal() && balls.contains(ball)) {
            //assert balls.get(0).equals(ball);
            if ((ball.getPosition().y() + ball.getCircle().getRadius()<= getPosition().y() 
                || ball.getPosition().y() >= getPosition().y() + this.getHeight()) ||
                    (ball.getPosition().x() + ball.getCircle().getRadius() <= getPosition().x() 
                    || ball.getPosition().x() >= getPosition().x() + this.getWidth())) {
                balls.remove(0);
                ball.setExitingAbsorber(false);
            }
            checkRep();
            return false;
        }
        /*
         * A note on implementation:
         *      1) call Physics.timeUntilWallCollision() to calculate the times at which the ball is colliding with each the lines/circles
         *      2) the minimum of all these times (call it "mintime") is the time of the next collision; also save the colliding line/circle to a variable
         *      3) mintime is in seconds, but our time scale is milliseconds, so time 1000 to it 
         *      4) if mintime <= 1 (or < 1?), there is a collision
         *          5) call one of the appropriate reflect method in Physics to resolve collision + update ball's new velocity, return true
         *      6) else, do nothing and return false
         *      
         */
        final Circle nextCollidingCircle = circle;
        final double seconds = Physics.timeUntilCircleCollision(nextCollidingCircle, ball.getCircle(), ball.getVelocity());
        final long mintimeCircle = (long) (seconds *1000);
        if (mintimeCircle <= 1) {
            //This block handles when the portal sends balls to a portal
            //on another board
            if (this.otherBoard.isPresent()) {
                trigger(); // trigger upon collision
                //send info to server
                board.getClient().sendRequest(ball, otherPortal, otherBoard.get());
                checkRep();
                return true;
            //This block handles when the portal sends balls to a portal
            //on another board
            } else {
                if (this.board.hasGadget(this.otherPortal)) {
                    double destinationX = this.board.getGadgetByName(this.otherPortal).getPosition().x()+RADIUS;
                    double destinationY = this.board.getGadgetByName(this.otherPortal).getPosition().y()+RADIUS;
                    Portal destinationPortal = this.board.getPortalByName(this.otherPortal);
                    destinationPortal.addBall(ball);
                    ball.setExitingPortal(true);
                    ball.setPosition(new Vect(destinationX,destinationY));
                    trigger(); // trigger upon collision
                    checkRep();
                    return true;
                }else {
                    trigger(); // trigger upon collision
                    checkRep();
                    return false;
                }
            }
        }
        checkRep();
        return false; // no collision

    }
    /**
     * Adds a ball to balls, meaning a ball has been teleported to the portal
     * is within it
     * @param ball Ball object to be added
     */
    public void addBall(Ball ball) {
        this.balls.add(ball);
    }
    
    public String getName() {
        return this.name;
    }

    public List<LineSegment> getLineSegments() {
        return new ArrayList<LineSegment>();
    }

    public List<Circle> getCircles() {
        return Collections.singletonList(circle);
    }

    public Vect getPosition() {
        return this.position;
    }

    public double getWidth() {
        return RADIUS*2;
    }

    public double getHeight() {
        return RADIUS*2;
    }

    @Override
    public void addTrigger(final Gadget triggeredGadget) {
        triggering.add(triggeredGadget);
    }
    
    @Override
    public void trigger() {
        for (final Gadget triggeredGadget : triggering) {
            triggeredGadget.respondToTrigger();
        }
    }

    @Override
    public void respondToTrigger() {
        // do nothing because a circle bumper has no action for triggers
    }
    @Override
    public boolean sameValue(Object that) {
        if (!(that instanceof Absorber)) return false;
        final Portal other = (Portal) that;
        if (this.name.equals(other.name) 
            && this.position.equals(other.position)
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

}
