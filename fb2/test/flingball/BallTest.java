package flingball;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import physics.Circle;
import physics.Vect;

public class BallTest {
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // Testing strategy
    //  - setPosition: partition into setting a position that is the same as before, not the same as before
    //  - setVelocity: partition into setting a velocity that is the same as before, not the same as before
    //  - setAbsorbed: partition into setting absorbed state as true and as false
    //  - setExitingAborber: partition in setting true and setting false
    //  - getName: checks to see if name is correct
    //  - getCircle: checks to see if circle is correct
    //  - getVelocity: checks to see if velocity is correct
    //  - getPosition: checks to see if position is correct
    //  - getAbsorbed: partition into a ball that is being absorbed and one that isn't
    //  - getExitingAbsorber: partition into is exiting and absorber and not exiting an absorber
    //  - sameValue: partition into two balls that have the same fields (name, position, velocity, absorbed, exitingAbsorber), 
    //               two that don't
    
    private static final Ball BALL1 = new Ball("Ball1",10,10,10,10);
    private static final Ball BALL2 = new Ball("Ball2",12,12,10,10);
    private static final Ball BALL3 = new Ball("Ball3",15,15,15,15);
    private static final Ball BALL1A = new Ball("Ball1",10,10,10,10);
    
    //covers test of setPosition of setting a position that is the same as before and test of position
    @Test
    public void testSetPositionSame() {
        BALL3.setPosition(new Vect(15,15));
        assertEquals("Expected position of <15,15>",new Vect(15,15),BALL3.getPosition());
    }
    
    //covers test of setPosition of setting a position that is not the same as before and test of position
    @Test
    public void testSetPositionDiff() {
        final Ball example = new Ball("Example",12,13,1,1);
        example.setPosition(new Vect(15,15));
        assertEquals("Expected position of <15,15>",new Vect(15,15),example.getPosition());
    }
    
    //covers test of setVelocity of setting a velocity that is the same as before and test of velocity
    @Test
    public void testSetVelocitySame() {
        BALL3.setVelocity(new Vect(15,15));
        assertEquals("Expected velocity of <15,15>",new Vect(15,15),BALL3.getVelocity());
    }
    
    //covers test of setVelocity of setting a velocity that is not the same as before and test of velocity
    @Test
    public void testSetvelocityDiff() {
        final Ball example = new Ball("Example",12,13,1,1);
        example.setVelocity(new Vect(15,15));
        assertEquals("Expected position of <15,15>",new Vect(15,15),example.getVelocity());
    }
    
    //covers test of setAbsorbed of setting absorbed as true, also covers getAbsorbed of absorbed of true
    @Test
    public void testGetSetAbsorbedTrue() {
        BALL1.setAbsorbed(true);
        assertTrue("Expected absored to be true",BALL1.getAbsorbed());
    }
    
    //covers test of setAbsorbed of setting absorbed as false, also covers getAbsorbed of absorbed of false
    @Test
    public void testGetSetAbsorbedFalse() {
        BALL1.setAbsorbed(false);
        assertFalse("Expected absored to be true",BALL1.getAbsorbed());
    }
    
    //covers test of setExitingAbsorber of setting exiting absorber as true, also covers getExitingAbsorber of is exiting an absorber
    @Test
    public void testGetSetExitingAbsorberTrue() {
        BALL1.setExitingAbsorber(true);
        assertTrue("Expected absored to be true",BALL1.getExitingAbsorber());
    }
    
    //covers test of setExitingAbsorber of setting exiting absorber as false, also covers getExitingAbsorber of is not exiting an absorber
    @Test
    public void testGetSetExitingAbsorberFalse() {
        BALL1.setExitingAbsorber(false);
        assertFalse("Expected absored to be true",BALL1.getExitingAbsorber());
    }
    
    //covers test of sameValue of two balls having the same fields, also covers test of name
    @Test
    public void testSameValueSame() {
        assertTrue("Expected two balls with the same fields to be the same ball", BALL1.sameValue(BALL1A));
    }
    
    //covers test of sameValue of two balls having different name, also covers test of name
    @Test
    public void testSameValueDifferent() {
        assertFalse("Expected two balls with different names to not be the same ball", BALL1.sameValue(BALL2));
    }
    
    //covers test of getCircle 
    @Test
    public void testGetCircle() {
        final Circle expected = new Circle(new Vect(10,10),0.25);
        assertEquals("Expected correct circle",expected,BALL1.getCircle());
    }
    
}
