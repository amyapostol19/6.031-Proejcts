package flingball;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import physics.Angle;
import physics.Circle;
import physics.LineSegment;
import physics.Physics;
import physics.Vect;

/**
 * Mutable type representing either a right or left flipper gadget in a flingball game
 */

public class Flipper implements Gadget{
    
    private final int RECTANGLE_SIDES = 4;
    private static final double CIRCLE_RADIUS = .25;
    private static final double BOX_DIM = 2;
    private static final double REFLECTION_COEFF = .95;
    private static final double ANGULAR_VELOCITY = 1080./1000. * Math.PI/180.; //radians per millisecond

    private final String type;
    private final String name;
    private final Vect position;
    private final LineSegment[] lines;
    private Circle movingCircle;
    private final Circle pivotCircle;
    private final List<Gadget> triggering = new ArrayList<>();
    
    private boolean rotateCounterClockwise;
    private boolean shouldMove = false;
    
    private final Circle initialPos;
    private final LineSegment leftInitialPos;
    private final LineSegment rightInitialPos;
    private final Circle movingSecondPos;    
    private final LineSegment leftSecondPos;
    private final LineSegment rightSecondPos;

    //Abstraction function:
    // AF(type, name, position, lines, movingCircle, pivotCircle, circles, triggering, rotateCounterClockwise, 
    //    shouldMove, initialPos, leftInitialPos, rightInitialPos, movingSecondPos, leftSecondPos, rightSecondPos)
    //      = represents a left flipper of type = "left" or a right flipper if type = "right". 
    //        The flipper has a unique name, a position on the board, two line segments that denote
    //        the boundaries of the straight part of the flipper, a moving circle that moves when the flipper is flipping,
    //        a pivot circle that never moves, a list of gadgets that it triggers once it is trigger. At a given point in time,
    //        the flipper is either next supposed to rotate clockwise or counter clockwise, and either should be moving or 
    //        shouldn't be moving. The flipper also has initial positions for its moving circle and left and right line segments,
    //        and second positions for its moving circle and left and right line segments that denote where these parts of the 
    //        flipper are located after the flipper flips. The flipper is bounded by a BOX_DIMxBOX_DIM bounding box with the pivot
    //        circle at the top left corner for a left flipper and the top right corner for the right flipper.
    //
    // Rep Invariant:
    //  type = "left" or type = "right"
    //  lines.size = 2
    //  all parts of the flipper are within the bounding box
    //  
    // Safety from rep exposure: 
    // - all fields are private
    // - none of the mutable fields are ever returned directly
    
    /**
     * @param name name of the flipper
     * @param xPos x position of the pivot of the flipper
     * @param yPos y position of the pivot of the flipper
     * @param orientation orientation of the flipper, must be 0, 90, 180, or 270
     * @param type type of the flipper, must be either "left" or "right"
     */
    public Flipper(final String name, final int xPos, final int yPos, final int orientation, String type) {
        //check preconditions of the flipper
        assert orientation == 0 || orientation == 90 || orientation == 180 || orientation == 270;
        assert type.equals("left") || type.equals("right");
        
        this.name = name;
        this.type = type;
        this.position = new Vect(xPos,yPos);
        double circleRadius = CIRCLE_RADIUS;
        LineSegment leftSide;
        LineSegment rightSide;
        
        //Initialize positions of the flipper for a left flipper
        if (type.equals("left")) {
            if (orientation == 0) {
                pivotCircle = new Circle(xPos+circleRadius, yPos+circleRadius, circleRadius);
                movingCircle = new Circle(xPos+circleRadius, yPos+BOX_DIM-circleRadius, circleRadius);
                leftSide =  new LineSegment(new Vect(xPos, yPos+circleRadius), new Vect(xPos, yPos+BOX_DIM-circleRadius));
                rightSide = new LineSegment(new Vect(xPos+2*circleRadius, yPos+circleRadius),
                                                        new Vect(xPos+2*circleRadius, yPos+BOX_DIM-circleRadius));
                movingSecondPos = new Circle(xPos+BOX_DIM-circleRadius, yPos+circleRadius, circleRadius);
                leftSecondPos = new LineSegment(new Vect(xPos+circleRadius, yPos+2*circleRadius), 
                        new Vect(xPos+BOX_DIM-circleRadius, yPos+2*circleRadius));
                rightSecondPos = new LineSegment(new Vect(xPos+circleRadius, yPos), 
                         new Vect(xPos+BOX_DIM-circleRadius, yPos));
                
            } else if (orientation == 90) {
                pivotCircle = new Circle(xPos+BOX_DIM-circleRadius, yPos+circleRadius, circleRadius);
                movingCircle = new Circle(xPos+circleRadius, yPos+circleRadius, circleRadius);
                leftSide =  new LineSegment(new Vect(xPos+circleRadius, yPos), new Vect(xPos+BOX_DIM-circleRadius, yPos));
                rightSide = new LineSegment(new Vect(xPos+circleRadius, yPos+2*circleRadius),
                                                        new Vect(xPos+BOX_DIM-circleRadius, yPos+2*circleRadius));
                movingSecondPos = new Circle(xPos+BOX_DIM-circleRadius, yPos+BOX_DIM-circleRadius, circleRadius);
                leftSecondPos = new LineSegment(new Vect(xPos+BOX_DIM-2*circleRadius, yPos+circleRadius), 
                                                new Vect(xPos+BOX_DIM-2*circleRadius, yPos+BOX_DIM-circleRadius));
                rightSecondPos = new LineSegment(new Vect(xPos+BOX_DIM, yPos+circleRadius), 
                                                 new Vect(xPos+BOX_DIM, yPos+BOX_DIM-circleRadius));
    
            } else if (orientation == 180) {
                pivotCircle = new Circle(xPos+BOX_DIM-circleRadius, yPos+BOX_DIM-circleRadius, circleRadius);
                movingCircle = new Circle(xPos+BOX_DIM-circleRadius, yPos+circleRadius, circleRadius);
                leftSide = new LineSegment(new Vect(xPos+BOX_DIM, yPos+circleRadius), 
                                                        new Vect(xPos+BOX_DIM, yPos+BOX_DIM-circleRadius));
                rightSide = new LineSegment(new Vect(xPos+BOX_DIM-2*circleRadius, yPos + circleRadius),
                                                        new Vect(xPos+BOX_DIM-2*circleRadius, yPos+BOX_DIM-circleRadius));
                movingSecondPos = new Circle(xPos+circleRadius, yPos+BOX_DIM-circleRadius, circleRadius);
                
                leftSecondPos = new LineSegment(new Vect(xPos+circleRadius, yPos+BOX_DIM-2*circleRadius), 
                        new Vect(xPos+BOX_DIM-circleRadius, yPos+BOX_DIM-2*circleRadius));
                rightSecondPos = new LineSegment(new Vect(xPos+circleRadius, yPos+BOX_DIM), 
                         new Vect(xPos+BOX_DIM-circleRadius, yPos+BOX_DIM));
    
            }else {
                pivotCircle = new Circle(xPos+circleRadius, yPos+BOX_DIM-circleRadius, circleRadius);
                movingCircle = new Circle(xPos+BOX_DIM-circleRadius, yPos+BOX_DIM-circleRadius, circleRadius);
                leftSide = new LineSegment(new Vect(xPos+circleRadius, yPos+BOX_DIM-2*circleRadius), 
                                                        new Vect(xPos+BOX_DIM-circleRadius, yPos+BOX_DIM-2*circleRadius));
                rightSide = new LineSegment(new Vect(xPos+circleRadius, yPos+BOX_DIM),
                                                        new Vect(xPos+BOX_DIM-circleRadius, yPos+BOX_DIM));
                movingSecondPos = new Circle(xPos+circleRadius, yPos+circleRadius, circleRadius);
                
                leftSecondPos = new LineSegment(new Vect(xPos, yPos+circleRadius), 
                        new Vect(xPos, yPos+BOX_DIM-circleRadius));
                rightSecondPos = new LineSegment(new Vect(xPos+2*circleRadius, yPos+circleRadius), 
                         new Vect(xPos+2*circleRadius, yPos+BOX_DIM-circleRadius));
            }
            rotateCounterClockwise = true;
        }
        //Initialize positions of the flipper if it is a right flipper
        else {
            if (orientation == 0) {
                pivotCircle = new Circle(xPos+BOX_DIM-circleRadius, yPos+circleRadius, circleRadius);
                movingCircle = new Circle(xPos+BOX_DIM-circleRadius, yPos+BOX_DIM-circleRadius, circleRadius);
                leftSide =  new LineSegment(new Vect(xPos+BOX_DIM-2*circleRadius, yPos+circleRadius), 
                                              new Vect(xPos+BOX_DIM-2*circleRadius, yPos+BOX_DIM-circleRadius));
                rightSide = new LineSegment(new Vect(xPos+BOX_DIM, yPos+circleRadius),
                                              new Vect(xPos+BOX_DIM, yPos+BOX_DIM-circleRadius));
                movingSecondPos = new Circle(xPos+circleRadius, yPos+circleRadius, circleRadius);
                leftSecondPos = new LineSegment(new Vect(xPos+circleRadius, yPos), 
                                           new Vect(xPos+BOX_DIM-circleRadius, yPos));
                rightSecondPos = new LineSegment(new Vect(xPos+circleRadius, yPos+2*circleRadius), 
                         new Vect(xPos+BOX_DIM-circleRadius, yPos+2*circleRadius));
                
                
            } else if (orientation == 90) {
                pivotCircle = new Circle(xPos+BOX_DIM-circleRadius, yPos+BOX_DIM-circleRadius, circleRadius);
                movingCircle = new Circle(xPos+circleRadius, yPos+BOX_DIM-circleRadius, circleRadius);
                leftSide = new LineSegment(new Vect(xPos+circleRadius, yPos+BOX_DIM-2*circleRadius), 
                         new Vect(xPos+BOX_DIM-circleRadius, yPos+BOX_DIM-2*circleRadius));
                rightSide = new LineSegment(new Vect(xPos+circleRadius, yPos+BOX_DIM),
                                                        new Vect(xPos+BOX_DIM-circleRadius, yPos+BOX_DIM));
                movingSecondPos = new Circle(xPos+BOX_DIM-circleRadius, yPos+circleRadius, circleRadius);
                leftSecondPos = new LineSegment(new Vect(xPos+BOX_DIM, yPos+circleRadius), 
                        new Vect(xPos+BOX_DIM, yPos+BOX_DIM-circleRadius));
                rightSecondPos = new LineSegment(new Vect(xPos+BOX_DIM-2*circleRadius, yPos+circleRadius), 
                        new Vect(xPos+BOX_DIM-2*circleRadius, yPos+BOX_DIM-circleRadius));            
                
            } else if (orientation == 180) {
                pivotCircle = new Circle(xPos+circleRadius, yPos+BOX_DIM-circleRadius, circleRadius);
                movingCircle = new Circle(xPos+circleRadius, yPos+circleRadius, circleRadius);
                leftSide =  new LineSegment(new Vect(xPos, yPos+circleRadius), new Vect(xPos, yPos+BOX_DIM-circleRadius));
                rightSide = new LineSegment(new Vect(xPos+2*circleRadius, yPos+circleRadius),
                                                        new Vect(xPos+2*circleRadius, yPos+BOX_DIM-circleRadius));
                movingSecondPos = new Circle(xPos+BOX_DIM-circleRadius, yPos+BOX_DIM-circleRadius, circleRadius);
                leftSecondPos = new LineSegment(new Vect(xPos+circleRadius, yPos+BOX_DIM-2*circleRadius), 
                                           new Vect(xPos+BOX_DIM-circleRadius, yPos+BOX_DIM-2*circleRadius));
                rightSecondPos = new LineSegment(new Vect(xPos+circleRadius, yPos+BOX_DIM), 
                         new Vect(xPos+BOX_DIM-circleRadius, yPos+BOX_DIM));
            } 
            else {
                pivotCircle = new Circle(xPos+circleRadius, yPos+circleRadius, circleRadius);
                movingCircle = new Circle(xPos+BOX_DIM-circleRadius, yPos+circleRadius, circleRadius);
                leftSide =  new LineSegment(new Vect(xPos+circleRadius, yPos+2*circleRadius), 
                        new Vect(xPos+BOX_DIM-circleRadius, yPos+2*circleRadius));
                rightSide = new LineSegment(new Vect(xPos+circleRadius, yPos),
                                                        new Vect(xPos+BOX_DIM-circleRadius, yPos));
                movingSecondPos = new Circle(xPos+circleRadius, yPos+BOX_DIM-circleRadius, circleRadius);
                leftSecondPos = new LineSegment(new Vect(xPos, yPos+circleRadius), 
                                           new Vect(xPos, yPos+BOX_DIM-circleRadius));
                rightSecondPos = new LineSegment(new Vect(xPos+2*circleRadius, yPos+circleRadius), 
                         new Vect(xPos+2*circleRadius, yPos+BOX_DIM-circleRadius));
            }
            rotateCounterClockwise = false;
        }
        initialPos = movingCircle;
        leftInitialPos = leftSide;
        rightInitialPos = rightSide;
        this.lines = new LineSegment[] {leftSide, rightSide};
        checkRep();
    }
    
    //checks the rep invariant of the flipper
    private void checkRep() {
        assert type.equals("left") || type.equals("right");
        assert lines.length == 2;
        assert ! outsideBox(lines[0]);
        assert ! outsideBox(lines[1]);
        assert ! outsideBox(movingCircle);
        assert ! outsideBox(pivotCircle);
        assert name != null;
        assert position != null;
        assert lines != null;
        assert movingCircle != null;
        assert pivotCircle != null;
        assert triggering != null;
        assert initialPos != null;
        assert leftInitialPos != null;
        assert rightInitialPos != null;
        assert movingSecondPos != null;
        assert leftSecondPos != null;
        assert rightSecondPos != null;
    }

    /**
     * Updates this flipper's position to reflect its movement during the specified period of time,
     * according to the rules of Flingball physics.
     * @param elapsedTime length of time in milliseconds
     * @param board board context on which the ball is moving
     */
    public void move(final long elapsedTime, final Board board) {
        if (shouldMove) {
            //Compute the angle in radians of how much it should rotate in this time step
            final Angle angle;
            if (! rotateCounterClockwise) {
                angle = new Angle(ANGULAR_VELOCITY * elapsedTime);
            } else {
                angle = new Angle(2*Math.PI - ANGULAR_VELOCITY * elapsedTime);
            }
            if (angle.radians() != 0) { 
                //Rotate the moving circle and line segments of the flipper around the pivot circle center
                movingCircle = Physics.rotateAround(movingCircle, pivotCircle.getCenter(), angle);
                for (int i = 0; i < lines.length; i ++) {
                    lines[i] = Physics.rotateAround(lines[i],  pivotCircle.getCenter(),  angle);
                }
                //Make sure everything is still within the bounding box and readjust positions if necessary 
                for (int i = 0; i < lines.length; i ++) {
                    LineSegment line = lines[i];
                    if (outsideBox(line)) {
                        if (type.equals("left")) {
                            if (rotateCounterClockwise) {
                                movingCircle = movingSecondPos;
                                lines[0] = leftSecondPos;
                                lines[1] = rightSecondPos;
                            } else {
                                movingCircle = initialPos;
                                lines[0] = leftInitialPos;
                                lines[1] = rightInitialPos;
                            }
                        } else {
                            if (! rotateCounterClockwise) {
                                movingCircle = movingSecondPos;
                                lines[0] = leftSecondPos;
                                lines[1] = rightSecondPos;
                            } else {
                                movingCircle = initialPos;
                                lines[0] = leftInitialPos;
                                lines[1] = rightInitialPos;
                            }
                        }
                        shouldMove = false;
                        rotateCounterClockwise = ! rotateCounterClockwise;
                    }
                }
            }
        }
        checkRep();
    }
    
    /**
     * Check if a line segment is outside of or on the border of the bounding box of the flipper
     * @param line line segment that is being tested to see if its outside of the box
     * @return boolean that is true if the line is outside or on the border of the box
     */
    private boolean outsideBox(LineSegment line) {
        if (line.p1().x() < position.x() || line.p1().x() > position.x() + BOX_DIM
                || line.p2().x() < position.x() || line.p2().x() > position.x() + BOX_DIM
                || line.p1().y() < position.y() || line.p1().y() > position.y() + BOX_DIM
                || line.p2().y() < position.y() || line.p2().y() > position.y() + BOX_DIM) {
            return true;
        }
        return false;
    }
    
    /**
     * Check if a circle is outside of or on the border of the bounding box of the flipper
     * @param circle circle that is being tested to see if its outside of the box
     * @return boolean that is true if the circle is outside or on the border of the box
     */
    private boolean outsideBox(Circle circle) {
        if (circle.getCenter().x() < position.x() || circle.getCenter().x() > position.x() + BOX_DIM
                || circle.getCenter().x() < position.x() || circle.getCenter().x() > position.x() + BOX_DIM
                || circle.getCenter().y() < position.y() || circle.getCenter().y() > position.y() + BOX_DIM
                || circle.getCenter().y() < position.y() || circle.getCenter().y() > position.y() + BOX_DIM) {
            return true;
        }
        return false;
    }
    
    /**
     * Check if a ball is inside of the body of the flipper
     * @param ball ball that is being tested to see if it is inside of the flipper
     * @return boolean that is true if the ball is inside the flipper
     */
    private boolean insideBox(Ball ball) {
        Polygon rectangle = makeRectangle();
        return rectangle.contains(ball.getPosition().x()*Flingball.PIXELS_PER_L, ball.getPosition().y()*Flingball.PIXELS_PER_L);
    }
    
    @Override public void render(Graphics2D g) {
        final double diameter = (CIRCLE_RADIUS + CIRCLE_RADIUS) * Flingball.PIXELS_PER_L;
        g.setColor(Color.ORANGE);
        //Pivot Circle
        g.fill(new Ellipse2D.Double((this.pivotCircle.getCenter().x()-CIRCLE_RADIUS) * Flingball.PIXELS_PER_L, 
                (this.pivotCircle.getCenter().y()-CIRCLE_RADIUS) * Flingball.PIXELS_PER_L, 
                diameter, diameter));
        //Moving Circle
        g.fill(new Ellipse2D.Double((this.movingCircle.getCenter().x()-CIRCLE_RADIUS) * Flingball.PIXELS_PER_L, 
                (this.movingCircle.getCenter().y()-CIRCLE_RADIUS) * Flingball.PIXELS_PER_L, 
                diameter, diameter));
        //Rectangle
        g.fill(makeRectangle());
        checkRep();
    }
    
    /**
     * Make the rectangle shape in the middle of the flipper, with appropriate for rendering on the board
     * @return the polygon that represents the rectangle
     */
    private Polygon makeRectangle() {
        int npoints = RECTANGLE_SIDES;
        int[] xpoints = new int[npoints];
        xpoints[0] = (int) (this.lines[0].p1().x()*Flingball.PIXELS_PER_L);
        xpoints[1] = (int) (this.lines[0].p2().x()*Flingball.PIXELS_PER_L);
        xpoints[2] = (int) (this.lines[1].p2().x()*Flingball.PIXELS_PER_L);
        xpoints[3] = (int) (this.lines[1].p1().x()*Flingball.PIXELS_PER_L);
        int[] ypoints = new int[npoints];
        ypoints[0] = (int) (this.lines[0].p1().y()*Flingball.PIXELS_PER_L);
        ypoints[1] = (int) (this.lines[0].p2().y()*Flingball.PIXELS_PER_L);
        ypoints[2] = (int) (this.lines[1].p2().y()*Flingball.PIXELS_PER_L);
        ypoints[3] = (int) (this.lines[1].p1().y()*Flingball.PIXELS_PER_L);
        return new Polygon(xpoints, ypoints, npoints);
    }
    
    @Override public boolean resolveCollision(Ball ball) {
        if (insideBox(ball)) {
            final double standardTimestep = .05;
            Vect newPos = ball.getPosition().plus(ball.getVelocity().times(standardTimestep));
            ball.setPosition(newPos);
        }
        
        double ang_velocity = ANGULAR_VELOCITY;
        if (! rotateCounterClockwise) {
            ang_velocity = - ang_velocity;
        }
        long mintimeLine = Long.MAX_VALUE;
        LineSegment nextCollidingLine = null;
        //first check for line collisions and keep track of the closer one
        for (int i = 0; i < lines.length; i ++) { 
            final long collisionTime;
            if (! shouldMove) {
                collisionTime = (long) (Physics.timeUntilWallCollision(lines[i], ball.getCircle(), ball.getVelocity())*1000);
            } else {
                collisionTime = (long) (Physics.timeUntilRotatingWallCollision(
                        lines[i], pivotCircle.getCenter(), ang_velocity, ball.getCircle(), ball.getVelocity())*1000);
            }
            if (collisionTime < mintimeLine) {
                mintimeLine = collisionTime;
                nextCollidingLine = lines[i];
            }
        }
        //now check for circle collisions and keep track of the closer one
        long mintimeCircle = Long.MAX_VALUE;
        Circle nextCollidingCircle = null;
        
        long collisionTime = (long) (Physics.timeUntilCircleCollision(pivotCircle, ball.getCircle(), ball.getVelocity())*1000);
        if (collisionTime < mintimeCircle) {
            mintimeCircle = collisionTime;
            nextCollidingCircle = pivotCircle;
        }
        
        collisionTime = (long) (Physics.timeUntilRotatingCircleCollision(
                movingCircle, pivotCircle.getCenter(), ang_velocity, ball.getCircle(), ball.getVelocity())*1000);
        if (collisionTime < mintimeCircle) {
            mintimeCircle = collisionTime;
            nextCollidingCircle = pivotCircle;
        }
        
        //Reflect off closer line if it is shorter than min circle time and within the next timestep 
        if (mintimeLine <= mintimeCircle && mintimeLine <= 1) {
            final Vect newVelocity; 
            //change how to reflect depending on whether or not the wall is moving
            if (! shouldMove) {
                newVelocity = Physics.reflectWall(nextCollidingLine, ball.getVelocity(), REFLECTION_COEFF);
            }
            else {
                newVelocity = Physics.reflectRotatingWall(nextCollidingLine,  pivotCircle.getCenter(), ang_velocity, 
                                                          ball.getCircle(), ball.getVelocity(), REFLECTION_COEFF);
            }
            ball.setVelocity(newVelocity); // updates the ball's velocity
            trigger(); // trigger upon collision
            return true;
        }
        //Reflect off closer circle if it is shorter than the min line time and within the next timestep
        else if (mintimeLine > mintimeCircle && mintimeCircle <= 1) {
            final Vect newVelocity;
            //change how to reflect depending on whether or not the wall is moving
            if (nextCollidingCircle.equals(movingCircle) && shouldMove) {
                newVelocity = Physics.reflectRotatingCircle(nextCollidingCircle, pivotCircle.getCenter(), ang_velocity, 
                                                        ball.getCircle(), ball.getVelocity(), REFLECTION_COEFF);
    
            } 
            else {
                newVelocity = Physics.reflectCircle(nextCollidingCircle.getCenter(), ball.getPosition(), ball.getVelocity(), REFLECTION_COEFF);
            }
            ball.setVelocity(newVelocity); // updates the ball's velocity
            trigger(); // trigger upon collision
            return true;
        }
        return false;
    }   
    
    @Override public String getName() {
        return this.name;
    }
    
    @Override public List<LineSegment> getLineSegments() {
        List<LineSegment> lineSegments = new ArrayList<>();
        lineSegments.add(lines[0]);
        lineSegments.add(lines[1]);
        return lineSegments;
     }

     @Override public List<Circle> getCircles() {
         List<Circle >circles = new ArrayList<>();
         circles.add(pivotCircle);
         circles.add(movingCircle);
         return circles;
     }
     
     @Override public Vect getPosition() {
         return this.position;
     }

     @Override public double getWidth() {
         return BOX_DIM;
     }

     @Override public double getHeight() {
         return BOX_DIM;
     }

     @Override public void addTrigger(Gadget triggeredGadget) {
         triggering.add(triggeredGadget);
     }
  
     @Override public void trigger() {
         for (final Gadget triggeredGadget : triggering) {
             triggeredGadget.respondToTrigger();
         }
     }

     @Override public void respondToTrigger() {
         //initialize it to start rotating at the next timeframe
         shouldMove = true;
     } 
     
     public double getReflectionCoeff() {
         return REFLECTION_COEFF;
     }

     @Override public String toString() {
         return type + " flipper at position" + position.toString();
     }
     
     @Override public boolean sameValue(Object that) {
         if (! (that instanceof Flipper)) {
             return false;
         }
         Flipper newThat = (Flipper) that;
         return newThat.name.equals(this.name) && newThat.type.equals(this.type) && newThat.position.equals(this.position) 
                 && newThat.movingCircle.equals(this.movingCircle) 
                 && newThat.pivotCircle.equals(this.pivotCircle) && newThat.rotateCounterClockwise == this.rotateCounterClockwise
                 && newThat.shouldMove == this.shouldMove;
     }
}