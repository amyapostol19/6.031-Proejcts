package flingball;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import physics.Circle;
import physics.LineSegment;
import physics.Physics;
import physics.Physics.VectPair;
import physics.Vect;

public class Ball{
    private Circle ball;
    private Vect velocity;
    private String name;

    /**
     * AF(ball, velocity, name) = represents a ball with a location and radius
     *                      that has a velocity in a 2D space and unique name
     * 
     * RI:
     *   locations cannot be negative
     *   locations must be within game board size (use static variable)
     *   ball and circle not null
     *   ball radius must be positive
     *   name not null
     *   
     * 
     * Safety from rep exposure:
     *   all returned values are immutable
     *   only ball object change a ball's velocity
     *   
     *   Object if mutable and has public mutators because other ball's have change the location and velocity
     *   of a ball if there is a collision between them.  This was a design decision because we wanted collisions
     *   between balls and this seemed like the only way for that to happen.
     */

    private final int TIMER_INTERVAL_MILLISECONDS = 50;
    private final double minBoardDimensions = 0.0;
    private final double maxBoardDimensions = 400.0;


    /**
     * 
     * @param ball non null Circle that contains a location
     * @param velocity non null Vect that contains the vector velocity
     */
    public Ball(Circle ball, Vect velocity, String name) {
        this.ball = ball;
        this.velocity = velocity;
        this.name = name;
        checkRep();
    }

    /**
     * Guarantee that the rep invariants are not broken
     */
    public void checkRep() {
        assert(this.ball.getCenter().x() >= minBoardDimensions);
        assert(this.ball.getCenter().y() >= minBoardDimensions);
        assert(this.ball.getCenter().x() <= maxBoardDimensions);
        assert(this.ball.getCenter().y() <= maxBoardDimensions);
        assert(this.ball != null);
        assert(this.ball.getRadius() > minBoardDimensions);
        assert this.name != null;
    }

    /**
     * @return name Name of the ball
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return Vect location of the center of the ball
     */
    public Vect getLocation() {
        return this.ball.getCenter();
    }

    /**
     * @return the Circle that contains the location and radius of the ball
     */
    public Circle getBall() {
        return this.ball;
    }

    /**
     * @return the velocity vector Vect of the ball
     */
    public Vect getVelocity(){
        return this.velocity;
    }

    /**
     * @return the double radius of the ball
     */
    public double getRadius() {
        return this.ball.getRadius();
    }

    /**
     * Change the location of the ball
     * @param newBall new location of the ball, must be in bounds of the board
     */
    public void changeLocation(Circle newBall) {
        this.ball = newBall;
        checkRep();
    }

    /**
     * Change the velocity of the ball
     * @param newVelocity Vect new velocity of the ball
     */
    public void changeVelocity(Vect newVelocity) {
        this.velocity = newVelocity;
    }

    /**
     * Add gravity to the velocity vector of the ball and sets the ball's
     * new velocity.
     * @param gravity double quantity of velocity in the y direction
     */
    public void addGravity(double gravity) {
        changeVelocity(new Vect(getVelocity().x(), getVelocity().y() + gravity/TIMER_INTERVAL_MILLISECONDS));
    }

    /**
     * Calculate the coeff of friction and apply it to the ball.  Change the ball's
     * velocity with friction applied.
     * @param friction double quantity of friction to be applied in the direciton
     * opposite of movement.
     */
    public void addFriction(List<Double> friction) {
        final double mu = friction.get(0)*5;
        final double mu2 = friction.get(1)*5;
        final double fric = ( 1 - mu*1/TIMER_INTERVAL_MILLISECONDS - mu2*1/TIMER_INTERVAL_MILLISECONDS);
        changeVelocity(new Vect(getVelocity().x()*fric, getVelocity().y()*fric));
    }

    /*
     * Move the ball for 1 timestep.  Check if the ball collides with any other objects (bumpers, absorbers, or balls)
     * and calculate the new velocity and location post collision.  If there is no collision, adjust the balls location
     * according to its initial velocity.
     * @param board the board object containing all balls and bumpers
     */
    public void move(Board board) {

        // Somehow pass is list of balls and gadgets and detect if there is a collision at the next timestep

        List<Gadget> gadgets = board.getGadgets();
        List<Ball> balls = board.getBalls();
        double time = 1.0;

        while(time > 0.) {

            double timeUntilCollisionGadget = time;
            double timeUntilCollisionBall = time;
            double timeUntilCollisionCorner = time;
            Vect newVec = null; 
            VectPair newVecBalls = null; 
            Ball hitsBall = null; 
            Gadget hitsGadget = null;

            Set<Circle> corners = new HashSet<Circle>();

            // Iterate through all the gadgets to check for collision
            for(Gadget g : gadgets) {
                
                // Do not wall collision with circle bumpers
                if(!g.getType().equals("CircleBumper")) {
                    
                    
                    for(LineSegment side : g.getLineSegments()) {
                        
                        // Line segments have corners that are possible hit spots
                        // corners do not register as hitting the line segments
                        Circle corner1 = new Circle(side.p1(), minBoardDimensions);
                        Circle corner2 = new Circle(side.p2(), minBoardDimensions);
                        corners.addAll(Arrays.asList(corner1, corner2));
                        
                        // Calculate the time until a collision between the ball and the given line segments
                        double timeUntil = Physics.timeUntilWallCollision(side, getBall(), getVelocity());
                        
                        // New lowest time
                        if( timeUntil < timeUntilCollisionGadget) {

                            // Do not register collisions with absorber if inside absorber
                            if(g.getType().equals("Absorber")) {
                                Absorber gg = (Absorber) g;
                                if(!gg.checkIfLaunchInProgress(this)) {

                                    timeUntilCollisionGadget = timeUntil;
                                    newVec = Physics.reflectWall(side, getVelocity());
                                    hitsGadget = g;
                                }
                            }
                            // Square or triangle bumper so register potential collision
                            else {
                                timeUntilCollisionGadget = timeUntil;
                                newVec = Physics.reflectWall(side, getVelocity());
                                hitsGadget = g;
                            }
                        }
                    }
                } 

                else {
                    // Potential collision with a circle bumper
                    CircleBumper cb = (CircleBumper) g;
                    double timeUntil = Physics.timeUntilCircleCollision(cb.getCircle(), getBall(), getVelocity());
                    if( timeUntil < timeUntilCollisionGadget) {
                        timeUntilCollisionGadget = timeUntil;
                        newVec = Physics.reflectCircle(cb.getCircle().getCenter(), getLocation(), getVelocity());
                        hitsGadget = g;
                    }

                } 

            }

            // Collisions with all corners/end of line segments
            for(Circle corner : corners) {
                double timeUntil = Physics.timeUntilCircleCollision(corner, getBall(), getVelocity());
                if( timeUntil < timeUntilCollisionCorner) {
                    timeUntilCollisionCorner = timeUntil;
                    newVec = Physics.reflectCircle(corner.getCenter(), getLocation(), getVelocity());
                }
            }


            for(Ball b : balls) {
                if(!this.equals(b)) {
                    double timeUntil = Physics.timeUntilBallBallCollision(getBall(), getVelocity(), b.getBall(), b.getVelocity());
                    if ( timeUntil < timeUntilCollisionBall){
                        timeUntilCollisionBall = timeUntil;
                        newVecBalls = Physics.reflectBalls(getLocation(), 1.0, getVelocity(), b.getLocation(), 1.0, b.getVelocity());
                        hitsBall = b;
                    }
                }

            }

            // Compare times
            if(timeUntilCollisionBall < timeUntilCollisionGadget && timeUntilCollisionBall < timeUntilCollisionCorner) {
                // Two balls hit first

                // change their velocities and positions
                Vect newCenter = new Vect(getLocation().x() + getVelocity().x()*timeUntilCollisionBall, getLocation().y() + getVelocity().y()*timeUntilCollisionBall);
                changeLocation(new Circle(newCenter, getRadius()));
                changeVelocity(newVecBalls.v1);

                Vect hitsBallNewCenter = new Vect(hitsBall.getLocation().x() + hitsBall.getVelocity().x()*timeUntilCollisionBall, hitsBall.getLocation().y() + hitsBall.getVelocity().y()*timeUntilCollisionBall);
                hitsBall.changeLocation(new Circle(hitsBallNewCenter, hitsBall.getRadius()));
                hitsBall.changeVelocity(newVecBalls.v2);


                time -= timeUntilCollisionBall;
            }
            else if (timeUntilCollisionGadget < timeUntilCollisionCorner) {
                // Bounce off of the proper gadget

                // Update velocity of the ball
                // Update the positition of the ball
                Vect newCenter = new Vect(getLocation().x() + getVelocity().x()*timeUntilCollisionGadget, getLocation().y() + getVelocity().y()*timeUntilCollisionGadget);
                changeLocation(new Circle(newCenter, getRadius()));
                changeVelocity(newVec);


                if(hitsGadget.getType().equals("Absorber")) {
                    Absorber g = (Absorber) hitsGadget;
                    g.addBallToAbsorber(this);
                    board.removeBall(this); 
                    
                }

                hitsGadget.trigger();





                time -= timeUntilCollisionGadget;


            }
            else if(timeUntilCollisionCorner < time) {
                Vect newCenter = new Vect(getLocation().x() + getVelocity().x()*timeUntilCollisionGadget, getLocation().y() + getVelocity().y()*timeUntilCollisionGadget);
                changeLocation(new Circle(newCenter, getRadius()));
                changeVelocity(newVec);

                time -= timeUntilCollisionCorner;
            }
            else {
                // No Objects hit in one timestep
                Vect newCenter = new Vect(getLocation().x() + getVelocity().x()*time, getLocation().y() + getVelocity().y()*time);
                changeLocation(new Circle(newCenter, getRadius()));
                //Update the balls position based on its velocity

                //break out of while loop
                break;
            }


        }

        addGravity(board.getGravity());
        addFriction(board.getFriction());
    }


    /**
     * Create a shape object that is a circle with the right dimensions
     * Will be used by the GUI to fill in the image
     * @return Shape Ellipse with proper dimensions
     */
    public Shape fill() {
        return new Ellipse2D.Double(getLocation().x()-getRadius(), 
                getLocation().y()-getRadius(), 
                2*getRadius(), 2*getRadius());
    }



    /**
     * Integer hash value for the Ball object
     * @return integer hashCode
     */
    @Override public int hashCode() {
        return getName().hashCode();
    }

    /**
     * Equal Balls are only Ball with the same name.  All balls have unique names.
     * @return true if equals false otherwise
     */
    @Override 
    public boolean equals(Object obj) {
        if(!(obj instanceof Ball)) {
            return false;
        }
        Ball that = (Ball) obj;
        return getName().equals(that.getName());
    }

    /**
     * Human readable form of the ball.  Contains all important information, 
     * such as name, location, and velocity
     * @return String containing name, location, velocity
     */
    @Override public String toString() {
        return getName() + " at " + this.getLocation() + "with velocity Vx = " + getVelocity().x() + ", Vy = " + getVelocity().y();

    }

}
