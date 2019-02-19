package flingball;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import physics.Angle;
import physics.Circle;
import physics.Vect;


public class BallTest {

    /**
     * Testing Strategy
     * 
     * CheckRep
     *      Invalid
     *      Valid
     *      
     * getName
     * getLocation
     * getBall
     * getVelocity
     * getRadius
     * changeLocation
     * changeVelocity
     * addGravity
     * addFriction
     * move() - call this function once with an empty board
     * fill
     * equals
     * hashCode
     * toString
     */

    //create blank board for tests
    List<Ball> balls = new ArrayList<>();
    List<Gadget> gadgets = new ArrayList<>();
    Double gravity = 25.0;
    Map<Gadget,Gadget> triggers = new HashMap<>();
    Board testBoard = new Board("bd", balls, gadgets, gravity, triggers);

    @Test(expected=IllegalArgumentException.class)
    public void testBadRadiusCreator() {
        new Ball(new Circle (new Vect(3, 5), -1), new Vect(2, 3), "Ball1");
    }

    // Less than minX
    @Test(expected=AssertionError.class)
    public void testBadLocationX() {
        new Ball(new Circle (new Vect(-3.0, 5.0), 2.0), new Vect(2.0, 3.0), "Ball1");

    }
    
    // Greater than maxX
    @Test(expected=AssertionError.class)
    public void testBadLocationX2() {
        new Ball(new Circle (new Vect(450.0, 5.0), 2.0), new Vect(2.0, 3.0), "Ball1");

    }
    
    // Less than minY
    @Test(expected=AssertionError.class)
    public void testBadLocationY() {
        new Ball(new Circle (new Vect(3.0, -5.0), 2.0), new Vect(2.0, 3.0), "Ball1");

    }
    
    // Greater than maxY
    @Test(expected=AssertionError.class)
    public void testBadLocationY2() {
        new Ball(new Circle (new Vect(3.0, 500.0), 2.0), new Vect(2.0, 3.0), "Ball1");

    }
    
    // Null Name
    @Test(expected=AssertionError.class)
    public void testNullName() {
        new Ball(new Circle (new Vect(3.0, 5.0), 2.0), new Vect(2.0, 3.0), null);

    }
    
    // getLocation, getName, getBall, getVelocity, getRadius
    @Test
    public void testGetMethods() {
        Ball ball = new Ball(new Circle (new Vect(30.0, 20.0), 2.0), new Vect(2.0, 3.0), "Ball1");

        assertTrue(ball.getLocation().equals(new Vect(30.0, 20.0)));
        assertTrue(ball.getName().equals("Ball1"));
        assertTrue(ball.getBall().equals(new Circle (new Vect(30.0, 20.0), 2.0)));
        assertTrue(ball.getVelocity().equals(new Vect(2.0, 3.0)));
        assertTrue(ball.getRadius() == 2.0);
        
    }
    
    // changeLocation
    @Test
    public void testChangeLocation() {
        Ball ball = new Ball(new Circle (new Vect(30.0, 20.0), 2.0), new Vect(2.0, 3.0), "Ball1");
        Circle newLocation = new Circle(new Vect(40.0, 20.0), 2.0);        
        ball.changeLocation(newLocation);
        assertTrue(ball.getLocation().equals(newLocation.getCenter()));
        
    }
    
    // changVelocity
    @Test
    public void testChangeVelocity() {
        Ball ball = new Ball(new Circle (new Vect(30.0, 20.0), 2.0), new Vect(2.0, 3.0), "Ball1");
        
        Vect newVelocity = new Vect(40.0, 20.0);        
        ball.changeVelocity(newVelocity);
        assertTrue(ball.getVelocity().equals(newVelocity));
        
    }
    
 // addGravity
    @Test
    public void testAddGravity() {
        Ball ball = new Ball(new Circle (new Vect(30.0, 20.0), 2.0), new Vect(2.0, 3.0), "Ball1");
        ball.addGravity(25.0);
        
        assertTrue(ball.getVelocity().equals(new Vect(2.0, 3.5)));
        
    }
    
 // addFriction
    @Test
    public void testAddFriction() {
        Ball ball = new Ball(new Circle (new Vect(30.0, 20.0), 2.0), new Vect(2.0, 3.0), "Ball1");
        ball.addFriction(Arrays.asList(0.025, 0.025));
        
        assertTrue(ball.getVelocity().equals(new Vect(1.9900000000000002,2.9850000000000003)));
        
    }
    
    // Move do not hit bumper
    @Test
    public void testMoveNoCollision() {
        Ball ball = new Ball(new Circle (new Vect(30.0, 20.0), 2.0), new Vect(2.0, 3.0), "Ball1");
        testBoard.addBall(ball);

        Vect ballStartLocation = ball.getLocation();
        ball.move(testBoard);
        Vect ballNewLocation = ball.getLocation();
        
        assertTrue(ballNewLocation.equals(new Vect(32.0,23.0)));
        assertFalse(ballStartLocation.equals(ballNewLocation));
        
        testBoard.removeBall(ball);
    }
    
    // Move hits ball
    @Test
    public void testMoveHitsBall() {
        Ball ball1 = new Ball(new Circle (new Vect(30.0, 20.0), 2.0), new Vect(2.0, 0.0), "Ball1");
        Ball ball2 = new Ball(new Circle (new Vect(33.0, 20.0), 2.0), new Vect(-2.0, 0.0), "Ball2");

        testBoard.addBall(ball1);
        testBoard.addBall(ball2);

        ball1.move(testBoard);
        ball2.move(testBoard);
        Vect ball1NewLocation = ball1.getLocation();
        Vect ball2NewLocation = ball2.getLocation();
        
        assertTrue(ball1NewLocation.equals(new Vect(28.0,20.0)));
        assertTrue(ball2NewLocation.equals(new Vect(35.0,20.0)));
        
        testBoard.removeBall(ball1);
        testBoard.removeBall(ball2);
    }
    
    // Move hits Square Bumper gadget
    @Test
    public void testMoveHitsSBGadget() {
        Ball ball1 = new Ball(new Circle (new Vect(30.0, 20.0), 2.0), new Vect(2.0, 0.0), "Ball1");

        SquareBumper sb1 = new SquareBumper(testBoard, 7, new Vect(31.0, 20.0), Angle.ZERO, "SB1");
        testBoard.addBall(ball1);
        testBoard.addGadget(sb1);

        ball1.move(testBoard);
        Vect ball1NewLocation = ball1.getLocation();
        
        assertTrue(ball1NewLocation.equals(new Vect(28.0,20.0)));
        
        testBoard.removeBall(ball1);
        testBoard.removeGadget(sb1);
    }
    
    // Move hits Circle Bumper gadget
    @Test
    public void testMoveHitsCBGadget() {
        Ball ball1 = new Ball(new Circle (new Vect(30.0, 20.0), 2.0), new Vect(2.0, 0.0), "Ball1");

        Vect location = new Vect(32.0, 20.0);
        double radius = 2;
        CircleBumper cb1 = new CircleBumper(testBoard,new Circle(location, radius), "CB1");
        
        testBoard.addBall(ball1);
        testBoard.addGadget(cb1);

        ball1.move(testBoard);
        
        Vect ball1NewLocation = ball1.getLocation();
        
        assertTrue(ball1NewLocation.equals(new Vect(28.0,20.0)));
        
        testBoard.removeBall(ball1);
        testBoard.removeGadget(cb1);
    }
    
    // Move hits Circle Bumper gadget
    @Test
    public void testMoveHitsTBGadget() {
        Ball ball1 = new Ball(new Circle (new Vect(30.0, 20.0), 2.0), new Vect(2.0, 0.0), "Ball1");

        Vect location = new Vect(32.5, 18);

        TriangleBumper tb1 = new TriangleBumper(testBoard,7, location, new Angle(270), "TB1");

        testBoard.addBall(ball1);
        testBoard.addGadget(tb1);

        ball1.move(testBoard);
        
        Vect ball1NewLocation = ball1.getLocation();
        
        // Hits hypotenuse
        assertTrue(ball1NewLocation.equals(new Vect(31.67157287525381,20.32842712474619)));
        
        testBoard.removeBall(ball1);
        testBoard.removeGadget(tb1);
    }
    
 // Move hits Circle Bumper gadget
    @Test
    public void testMoveHitsAbsorberGadget() {
        Ball ball1 = new Ball(new Circle (new Vect(30.0, 20.0), 2.0), new Vect(2.0, 0.0), "Ball1");

        Vect location = new Vect(34.5, 20);
        Absorber absorber = new Absorber(testBoard,1, 5, location, Angle.ZERO, "AB1");
        
        testBoard.addBall(ball1);
        testBoard.addGadget(absorber);

        ball1.move(testBoard);
        ball1.move(testBoard);
            
        assertTrue(testBoard.getBalls().size() == 0);
        assertTrue(absorber.containsBalls());
        
        testBoard.removeGadget(absorber);
    }
    
    

}
