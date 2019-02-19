package warmup;

import java.util.*;

import physics.LineSegment;
import physics.Physics;
import physics.Vect;

/**
 * An immutable type representing the boarder walls surrounding the playfield of one Flingball board.
 */
public class Board {
    
    private static final double SIZE = 20;
    private static final double DEFAULT_GRAVITY = 25.0;
    private static final double DEFAULT_FRICTION = 0.025;
    
    private final List<LineSegment> sides;
    private final double gravity;
    private final double friction1;
    private final double friction2;
    private final double reflectionCoeff = 1.0; // default
    
    /**
     * Creates a new board with default gravity (0.25) and friction values (mu1 = mu2 = 0.025).
     */
    public Board() {
        this(DEFAULT_GRAVITY, DEFAULT_FRICTION, DEFAULT_FRICTION);
    }
    
    /**
     * Creates a new board with the specified gravity and friction values.
     * @param gravity gravity of the Flingball board
     * @param friction1 global friction constant mu1
     * @param friction2 global friction constant mu2
     */
    public Board(double gravity, double friction1, double friction2) {
        this.sides = Collections.unmodifiableList(Arrays.asList(new LineSegment(0, 0, 0, SIZE), 
                                                                new LineSegment(0, 0, SIZE, 0), 
                                                                new LineSegment(0, SIZE, SIZE, SIZE), 
                                                                new LineSegment(SIZE, 0, SIZE, SIZE)));
        this.gravity = gravity;
        this.friction1 = friction1;
        this.friction2 = friction2;
    }
    
    /** @return immutable list containing the outer walls of the board */
    public List<LineSegment> getSides() {
        return sides;
    }
    
    /** @return gravity of the Flingball board */
    public double getGravity() {
        return gravity;
    }
    
    /** @return global friction constant mu1 */
    public double getFriction1() {
        return friction1;
    }
    /** @return global friction constant mu2 */
    public double getFriction2() {
        return friction2;
    }
    public double getReflectionCoeff() {
        return reflectionCoeff;
    }
    
    
    public static void main(String[] args) {
        final double posX = 10.0;
        final double posY = 10.0;
        final double speed = 5.0;
        final double angle = Math.toRadians(150);
        final CircleBall ball = new CircleBall(posX, posY, speed, angle);
        final Board board = new Board();
        
        double mintime = -1;
        LineSegment nextCollidingWall = null;
        
        while (true) {
            ball.printPosition();
            System.out.println(ball.getVelocity());
            if (mintime <= 1) { // at this point, the ball and the wall it is about to hit are exactly adjacent to one another
                if (mintime != -1) {
                    // call reflectWall() to calculate the change in the ball's velocity
                    Vect newVelocity = Physics.reflectWall(nextCollidingWall, ball.getVelocity(), board.getReflectionCoeff());
                    ball.setVelocity(newVelocity); // updates the ball's velocity
                }
                /*
                 * Call the timeUntilCollision() methods to calculate the times at which the ball will 
                 * collide with each of walls. The minimum of all these times (call it "mintime") 
                 * is the time of the next collision.
                 */
                List<LineSegment> walls = board.getSides();
                mintime = Physics.timeUntilWallCollision(walls.get(0), ball.getCircle(), ball.getVelocity());
                nextCollidingWall = walls.get(0);
                for (int i = 1; i < walls.size(); i++) {
                    final double collisionTime = Physics.timeUntilWallCollision(walls.get(i), ball.getCircle(), ball.getVelocity());
                    if (collisionTime < mintime) {
                        mintime = collisionTime;
                        nextCollidingWall = walls.get(i);
                    }
                }
            }
            else {
                ball.move(); // update the position of the ball to account for mintime passing
                mintime--;
            }
        }
    }

}
