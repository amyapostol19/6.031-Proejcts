package flingball;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Test;

import physics.Vect;

public class AbsorberTest {
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    //Testing Strategy
    //getBalls:
    //  Partition into no balls, holding more than one ball
    //respondToTrigger:
    //  Partition into absorber holding no balls, holding one ball, holding more than one ball
    //resolveCollision:
    //  Check to see if ball was absorbed or not
    
    private static final Vect SHOOTOUT_VELOCITY = new Vect(0, -50);
    
    private static final Absorber ABSORBER1 = new Absorber("Absorber1",12,12,5,5);
    private static final Absorber ABSORBER2 = new Absorber("Absorber2",16,16,2,2);
    private static final Ball BALL1 = new Ball("Ball1",12,11.9,0,5);
    private static final Ball BALL2 = new Ball("Ball2",12,11.9,0,5);
    
    //Covers test of getBalls of an absorber with no balls
    @Test
    public void testGetBallsNoBalls() {
        assertEquals("Expected no balls", Collections.emptySet(), ABSORBER1.getBallsNames());
    }
    
    //covers test of getBalls of an absorber that is holding many balls
    @Test
    public void testGetBallsManyBalls() {
        final Absorber example = new Absorber("Example",12,12,1,1);
        example.resolveCollision(BALL1);
        example.resolveCollision(BALL2);
        assertEquals("Expected 2 balls", new HashSet<String>(Arrays.asList(BALL1.getName(), BALL2.getName())),
                                         example.getBallsNames());
    }
    
    //covers test of respondToTrigger of an absorber holding no balls
    @Test
    public void testRespondToTriggerNoBalls() {
        final Absorber example = new Absorber("Absorber2",16,16,2,2);
        assertTrue("Expected nothing to have changed",example.sameValue(ABSORBER2));
    }
    
    //covers test of respondToTrigger of an absorber holding one ball
    @Test
    public void testRespondToTriggerOneBall() {
        final Absorber example = new Absorber("Example",12,12,1,1);
        final Ball ball = new Ball("Ball",12,11.9,0,5);
        example.resolveCollision(ball);
        example.respondToTrigger();
        assertEquals("Expected ball to be moving at velocity <0, -50>", SHOOTOUT_VELOCITY, ball.getVelocity());
    }
    
    //covers test of respondToTrigger of an absorber holding more than one ball
    @Test
    public void testRespondToTriggerManyBalls() {
        final Absorber example = new Absorber("Example",12,12,1,1);
        final Ball ball1 = new Ball("Ball1",12,11.9,0,5);
        final Ball ball2 = new Ball("Ball2",12,11.9,0,5);
        example.resolveCollision(ball1);
        example.resolveCollision(ball2);
        example.respondToTrigger();
        assertEquals("Expected ball to be moving at velocity <0, -50>", SHOOTOUT_VELOCITY, ball1.getVelocity());
        assertEquals("Velocity of ball2 should be <0, 0>", new Vect(0, 0), ball2.getVelocity());
    }

}
