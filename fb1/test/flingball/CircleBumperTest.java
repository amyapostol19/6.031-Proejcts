package flingball;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import physics.Angle;
import physics.Circle;
import physics.Vect;


public class CircleBumperTest {

    /**
     * Testing Strategy
     * 
     * Use checkRep() to test Creator Method
     *      Bad construction (invalid creator)
     *      Good construction
     *      
     * partitions for getLocation, getOrientation, getWidth, getHeight, getName, getBoard
     *      not many partitions, check they return the correct values
     *      
     * getCircle() 
     * 
     * getReflectionCoefficient():
     *      Return correct coeff >= 0.0
     * 
     * getLineSegments()
     * 
     * getType()
     *      
     *  hashcode and equals can have the same partitions because they test
     *  similar things; Partitions:
     *      partition based on fields of absorber
     *          - Reflexive
     *          - Symmetrical
     *          - Transitive
     *      
     * hashCode():
     *      Like elements have same hashCode
     *      
     * equals():
     *      Reflexive
     *      Symmetrical
     *      Transitive
     *      
     * toString():
     *      Return correct output
     */
    
    //create blank board for tests
    List<Ball> balls = new ArrayList<>();
    List<Gadget> gadgets = new ArrayList<>();
    Double gravity = 25.0;
    Map<Gadget,Gadget> triggers = new HashMap<>();
    Board testBoard = new Board("bd", balls, gadgets, gravity, triggers);
    
    
    //CheckRep Failures
    //covers negative radius
    @Test(expected=IllegalArgumentException.class)
    public void testBadRadiusCreator() {
        new CircleBumper(testBoard,new Circle (new Vect(3, 5), -1), "CB1");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBadLocationCheckRep() {
        new CircleBumper(testBoard,new Circle (new Vect(-1, 5), -1), "CB1");
    }
    
    
    //Test getReflectionCoefficient
    @Test
    public void testRelectionCoefficient() {
        Circle circle = new Circle(new Vect(20, 40), 2);
        CircleBumper bumper = new CircleBumper(testBoard, circle, "CB1");

        assertTrue(bumper.getReflectionCoefficient() == 1);
        assertTrue(bumper.getReflectionCoefficient() == 1.0);
        assertFalse(bumper.getReflectionCoefficient() == 2);
    }
    
    //Test getLocation, getCircle, getOrientation, getWidth, getHeight, getName, getBoard
    @Test
    public void testGetMethods() {
        Vect location = new Vect(14, 14);
        CircleBumper bumper = new CircleBumper(testBoard,new Circle(location, 2), "CB1");

        assertTrue(new Vect(12, 12).equals(bumper.getLocation()));
        assertTrue(bumper.getCircle().equals(new Circle(location, 2)));
        assertEquals(Angle.ZERO, bumper.getOrientation());
        assertEquals(4, bumper.getWidth());
        assertEquals(4, bumper.getHeight());
        assertEquals("CB1", bumper.getName());
        assertEquals(testBoard, bumper.getBoard());
    }
    
    //test getType and getLineSegments
    @Test
    public void testGetTypeLineSegmentsCircle() {
        Vect location = new Vect(60, 20);
        CircleBumper bumper = new CircleBumper(testBoard,new Circle(location, 4), "CB1");

        assertEquals("CircleBumper", bumper.getType());
        assertEquals(null,  bumper.getLineSegments());
        assertTrue(new Circle(location, 4).equals(bumper.getCircle()));
    }
    
    
    //Test Hashcode() and Equals() Method
    
    //Equals Symmetric, Reflective
    @Test
    public void testAllSameHashcodeEquals() {
        Vect location = new Vect(new Angle(20, 40), 5);
        double radius = 2;
        CircleBumper correct = new CircleBumper(testBoard,new Circle(location, radius), "CB1");
        CircleBumper other = new CircleBumper(testBoard,new Circle(location, radius), "CB1");

        
        assertTrue(correct.equals(other));
        assertEquals(correct.hashCode(), other.hashCode());
        assertTrue(correct.equals(correct));
        assertEquals(correct.hashCode(), correct.hashCode());
        
    }
    
    // Covers not equal gadgets
    @Test
    public void testHashcodeEqualsDiffName() {
        Vect location = new Vect(40, 50);
        
        CircleBumper cb1 = new CircleBumper(testBoard,new Circle(location, 3), "CB1");
        CircleBumper cb2 = new CircleBumper(testBoard,new Circle(location, 3), "CB2");

        
        assertFalse(cb1.equals(cb2));
        assertFalse(cb1.hashCode() == cb2.hashCode());
    }
    
    
    
  //covers toString method
    @Test
    public void testToString() {
        Vect location = new Vect(3, 5);
        CircleBumper bumper = new CircleBumper(testBoard,new Circle(location, 2), "CB1");
        assertEquals("Circle Bumper at <1.0,3.0> and diameter 4", bumper.toString());
    }

}
