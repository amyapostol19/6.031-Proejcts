package flingball;
import static org.junit.Assert.*;

import org.junit.Test;

import physics.Vect;

public class FlipperTest {
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // Testing Strategy: test some things manually, some with unit tests
    //
    //  Unit tests: 
    // - toString: left flipper, right flipper
    // - sameValue: same flippers 
    //              left flipper, right flipper
    //              different orientations
    //              moving, not moving
    // - reflectionCoeff: make sure it defaults to proper value
    // - height/width: make sure they default to proper value
    // - position: make sure position is correct
    //
    // Manual tests:
    // - Constructor: left flipper, right flipper
    //                orientation 0, 90, 180, 270
    //                make sure starting position is correct
    // - Render: left flipper, right flipper
    //                orientation 0, 90, 180, 270
    //                initial position, while moving, second position
    // - Move: left flipper, right flipper
    //                orientation 0, 90, 180, 270
    //                moving counterClockwise, moving clockwise
    //
    // Other behavioral tests
    // - Make sure reflection coefficient is properly reducing ball's velocity by making sure a ball
    //    vertically bouncing on a flipper eventually stops bouncing
    // - Make sure triggering a flipper properly triggers the other gadgets that it should
    // - Make sure ball properly collides with flipper: when flipper is moving and not moving, 
    //    and regardless of where on the flipper the ball hits
    // - Make sure flipper starts moving in the proper direction when it is triggered
    // - Make sure it responds properly to keypressed triggers 
    
    private static final Flipper LEFT_FLIPPER = new Flipper ("Left flipper", 1,1, 0, "left");
    private static final Flipper LEFT_FLIPPER2 = new Flipper ("Left flipper", 1,1, 90, "left");

    private static final Flipper RIGHT_FLIPPER = new Flipper ("Right flipper", 1,1, 0, "right");
    private static final Flipper RIGHT_FLIPPER2 = new Flipper ("Right flipper", 1,1, 0, "right");
    private static final Flipper RIGHT_FLIPPER3 = new Flipper ("Right flipper", 1,1, 0, "right");

    // Covers reflection coefficient 
    @Test
    public void testGetReflectionCoefficientDefault() {
        assertEquals("Expected default coefficient of 0.95",.95,LEFT_FLIPPER.getReflectionCoeff(),0.0001);
    }
    
    // Covers toString left flipper
    @Test
    public void testToStringLeft() {
        assertEquals("Expected proper toString","left flipper at position<1.0,1.0>",LEFT_FLIPPER.toString());
    }
    
    // Covers toString right flipper
    @Test
    public void testToStringRight() {
        assertEquals("Expected proper toString","right flipper at position<1.0,1.0>",RIGHT_FLIPPER.toString());
    }
    
    // Covers sameValue same flipper
    @Test
    public void testSameValueEqual() {
        assertEquals("Expected same value",true,RIGHT_FLIPPER.sameValue(RIGHT_FLIPPER2));
    }
    
    // Covers sameValue left vs right
    @Test
    public void testSameValueLeftVsRight() {
        assertEquals("Expected not same value",false,RIGHT_FLIPPER.sameValue(LEFT_FLIPPER));
    }
    
    // Covers sameValue different orientations
    @Test
    public void testSameValueDiffOrientationsl() {
        assertEquals("Expected not same value",false,LEFT_FLIPPER.sameValue(LEFT_FLIPPER2));
    }
    
    // Covers sameValue different positions
    @Test
    public void testSameValueDiffPositions() {
        RIGHT_FLIPPER3.respondToTrigger();        
        assertEquals("Expected not same value",false,RIGHT_FLIPPER.sameValue(RIGHT_FLIPPER3));
    }
    
    // Covers height at right value
    @Test
    public void testGetHeight() {
        assertTrue("Expected height of 2",2 == LEFT_FLIPPER.getHeight());
    }
    
    // Covers width at right value
    @Test
    public void testGetWidth() {
        assertTrue("Expected width of 2",2 == LEFT_FLIPPER.getWidth());
    }
    
    // Covers position at right value
    @Test
    public void testGetPosition() {
        assertEquals("Expected position of 1,1",new Vect(1,1), LEFT_FLIPPER.getPosition());
    }
}