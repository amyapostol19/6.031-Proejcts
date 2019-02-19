package flingball;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import physics.Angle;
import physics.Circle;
import physics.Vect;


public class AbsorberTest {

    private static final int PIXELS_PER_L = 20;
    // Need to add some specific tests for triggers
    
    /* methods that we need to test:
     *  -creator method (incorrect)
     *  -getLocation
     *  -getReflectionCoefficient
     *  -getOrientation
     *  -getHeight
     *  -getWidth
     *  -getName
     *  -getBoard
     *  -trigger() TODO
     *  -getLineSegments()
     *  -getType()
     *  -hashcode()
     *  -equals()
     *  -toString()
     *  
     *  partitions for creator:
     *      width, height, location, orientation: all correct
     *      each parameter incorrect
     *      
     *  partitions getReflectionCoefficient:
     *      should be ZERO, "AB1" every time. Check once
     *      
     *  partitions for getLocation, getOrientation, getWidth, getHeight
     *      not many partitions, check they return the correct values
     *  
     *  trigger methods: TODO
     *      contains ball
     *      doesn't contain ball
     *  
     *  hashcode and equals are based on object names (identities)
     * 
     * toString() Method
     *      
     *      
     */
    
    //create blank board for tests
    List<Ball> balls = new ArrayList<>();
    List<Gadget> gadgets = new ArrayList<>();
    Double gravity = 25.0;
    Map<Gadget,Gadget> triggers = new HashMap<>();
    Board testBoard = new Board("bd", balls, gadgets, gravity, triggers);
    
    //Test Creator Method
    


    //covers incorrect width
    @Test(expected=AssertionError.class)
    public void testCreatorIncorrectWidth() {
        Vect location = new Vect(Angle.ZERO, 5);
        new Absorber(testBoard,-1, 5, location, Angle.DEG_45, "AB1");
    }
    
    //covers incorrect height
    @Test(expected=AssertionError.class)
    public void testCreatorIncorrectHeight() {
        Vect location = new Vect(Angle.ZERO, 5);
        new Absorber(testBoard,2, -5, location, Angle.DEG_45, "AB1");
    }
    
  //covers incorrect height
    @Test(expected=AssertionError.class)
    public void testCreatorIncorrectLocation() {
        Vect location = new Vect(Angle.ZERO, -5);
        new Absorber(testBoard,2, -5, location, Angle.DEG_45, "AB1");
        }
    
    
    //Test getReflectionCoefficient
    @Test
    public void testRelectionCoefficient() {
        Vect location = new Vect(0, 10);
        Absorber absorber = new Absorber(testBoard,1, 5, location, Angle.ZERO, "AB1");

        assertTrue(absorber.getReflectionCoefficient() == 0);
        assertTrue(absorber.getReflectionCoefficient() == 0.0);
        assertFalse(absorber.getReflectionCoefficient() == 2);
    }
    
    //Test getLocation, getOrientation, getWidth, getHeight, getName, getBoard, getType
    @Test
    public void testGetMethods() {
        Vect location = new Vect(14, 14);
        Absorber absorber = new Absorber(testBoard,6, 7, location, Angle.DEG_180, "AB1");

        
        assertEquals(new Vect(14, 14), absorber.getLocation());
        assertEquals(Angle.DEG_180, absorber.getOrientation());
        assertEquals(6, absorber.getWidth());
        assertEquals(7, absorber.getHeight());
        assertEquals("AB1", absorber.getName());
        assertEquals(testBoard, absorber.getBoard());
        assertEquals("Absorber", absorber.getType());

    }
    
    
    //Test Hashcode() and Equals() Method
    
    // Covers Equals and hashCode Symmetric and Reflexive
    @Test
    public void testAllSameHashcodeEquals() {
        Vect location = new Vect(new Angle(0, 10), 5);
        Absorber ab1 = new Absorber(testBoard,5, 5, location, Angle.DEG_90, "AB1");
        Absorber ab2 = new Absorber(testBoard,5, 5, location, Angle.DEG_90, "AB1");

        
        assertTrue(ab1.equals(ab2));
        assertEquals(ab1.hashCode(), ab2.hashCode());
        assertTrue(ab1.equals(ab1));
        assertEquals(ab1.hashCode(), ab1.hashCode());
    }
    
    //covers different name, not equals
    @Test
    public void testHashCodeEqualsDiffName() {
        Vect location = new Vect(Angle.DEG_90, 6);
        
        Absorber ab1 = new Absorber(testBoard,4, 2, location, Angle.ZERO, "AB1");
        Absorber ab2 = new Absorber(testBoard,4, 2, location, Angle.ZERO, "AB2");

        
        assertFalse(!ab1.equals(ab2));
        assertTrue(ab1.hashCode() != ab2.hashCode());
        
    }
    
    @Test
    public void testToString() {
        Vect location = new Vect(Angle.DEG_45, 10);
        Absorber absorber = new Absorber(testBoard,4, 1, location, Angle.ZERO, "AB1");

        assertTrue(absorber.toString().equals("Absorber at <7.0710678118654755,7.0710678118654755>, width 4, and height 1"));
    }

    @Test
    public void testAction() {
        Vect location = new Vect(10*PIXELS_PER_L, 10*PIXELS_PER_L);
        
        Absorber ab1 = new Absorber(testBoard,6*PIXELS_PER_L, 6*PIXELS_PER_L, location, Angle.ZERO, "AB1");
        Ball ball = new Ball (new Circle(new Vect(3*PIXELS_PER_L,3*PIXELS_PER_L),.5*PIXELS_PER_L), new Vect(2*PIXELS_PER_L,8*PIXELS_PER_L), "ball1");
        Ball ball2 = new Ball (new Circle(new Vect(4*PIXELS_PER_L,5*PIXELS_PER_L),.5*PIXELS_PER_L), new Vect(7*PIXELS_PER_L,9*PIXELS_PER_L), "ball2");
        ab1.addBallToAbsorber(ball);
        ab1.action();
        assertTrue(ab1.containsBalls());
        assertFalse(ball.getVelocity().y()==-PIXELS_PER_L);
        ab1.addBallToAbsorber(ball2);
        ab1.action();
        System.out.println(ab1.getHeight());
        assertEquals(new Vect(0,-PIXELS_PER_L), ball.getVelocity());
        assertEquals(new Vect(315,315),ball.getLocation());
        assertTrue(ab1.containsBalls());
        
    }
}
