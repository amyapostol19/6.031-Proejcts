package warmup;

import physics.Angle;
import physics.Circle;
import physics.Vect;

/**
 * A mutable type representing the Flingball ball.
 */
public class CircleBall {
    
    private static final double RADIUS = 0.25;
    
    private double posX;
    private double posY;
    private Vect velocity;
    
    /**
     * Creates a new ball at the specified position traveling 
     * at the specified speed along the specified angle.
     * @param posX initial x position of the ball
     * @param posY initial x position of the ball
     * @param speed initial speed of the ball
     * @param angle initial angle to which the ball is bouncing
     */
    public CircleBall(double posX, double posY, double speed, double angle) {
        this.posX = posX;
        this.posY = posY;
        this.velocity = new Vect(new Angle(angle), speed);
    }
    
    public void move() {
        posX += velocity.x();
        posY += velocity.y();
    }
    
    public void setVelocity(Vect newVelocity) {
        velocity = newVelocity;
    }
    
    /** @return ball's circle */
    public Circle getCircle() {
        return new Circle(posX, posY, RADIUS);
    }
    
    /** @return ball's velocity */
    public Vect getVelocity() {
        return velocity;
    }
    
    /** @return ball's current position */
    public Vect getPosition() {
        return new Vect(posX, posY);
    }
    
    /** Prints ball's current position. */
    public void printPosition() {
        System.out.println("Position of ball is: (" + posX + ", " + posY + ").");
    }
    
}
