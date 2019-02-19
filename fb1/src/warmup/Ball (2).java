package warmup;

import physics.Angle;
import physics.Circle;
import physics.Vect;

public class Ball {
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
    public Ball(double posX, double posY, double speed, double angle) {
        this.posX = posX;
        this.posY = posY;
        this.velocity = new Vect(new Angle(angle), speed);
    }
    
    public void move(long elapsedTime) {
        posX += velocity.x() * (double) elapsedTime / 1000;
        posY += velocity.y() * (double) elapsedTime / 1000;
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
    
    /**
     * Moves the ball from its starting position downwards then when it hits the bottom wall
     * it moves at the same speed but at a random angle
     */
    /*
    public void move() {
        //initial velocity is velocityX = 0, velocityY = 5
        Thread thread = new Thread() {
            @Override public void run() {
                while (true) {
                    double currentX = position.x();
                    double currentY = position.y();
                    position = new Vect(currentX += velocity.x(), currentY += velocity.y());
                    
                    //double randomNewAngle = Math.toRadians(180.0 * Math.random());
                    if((position.y() + radius) > maxL) { //bottom wall, velocityY has to now be negative
                        position = new Vect(position.x(),maxL);
                        velocity = Physics.reflectWall(bottomWall, velocity);
                    }
                    
                    if ((position.y() - radius) < 0) {//top wall, velocityY now has to be positive
                        position = new Vect(position.x(),radius);
                        velocity = Physics.reflectWall(topWall, velocity);
                    }
                    
                    if((position.x() + radius) > maxL) { //right wall, velocityX has to now be negative
                        position = new Vect(maxL,position.y());
                        velocity = Physics.reflectWall(rightWall, velocity);
                    }
                    
                    if ((position.x() - radius) < 0) {//left wall, velocityX now has to be positive
                        position = new Vect(radius,position.y());
                        velocity = Physics.reflectWall(leftWall, velocity);
                    }

                    System.out.println("Position of ball is: ("+position+")");
                }
            }
        };
        thread.start();
    }
    
   */ 

}
