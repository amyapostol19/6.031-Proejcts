package flingball;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import physics.Angle;
import physics.Vect;


public class TriangleBumperTest {
    
    /**
     * Testing Strategy
     * 
     * checkRep():
     *      Invalidate Rep with creator
     *      Good construction
     *      
     * getLocation()
     *      Inside the board
     *      Outside the board
     *      
     * getReflectionCoefficient():
     *      Return correct coeff >= 0.0
     *      
     * getOrientation():
     *      Orientation values: can be any value where value%90 == 0
     *          partitions: test when 
     *              0 < value < 360, 
     *              value < 0, 
     *              value < 360
     *      
     * getHeight():
     *      Positive height
     *      No height
     *      
     *      
     * getWidth():
     *      Positive height
     *      No height
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

    
    
    //Test Creator Method
    
    //covers incorrect leg length
    @Test(expected=AssertionError.class)
    public void testCreatorIncorrectLeg() {
        Vect location = new Vect(5, 5);

        TriangleBumper creator = new TriangleBumper(testBoard,-5, location, Angle.ZERO, "TB1");

        assertTrue(creator.getType().equals("TriangleBumper"));
    }
    
    //covers incorrect x location
    @Test(expected=AssertionError.class)
    public void testCreatorIncorrectX() {
        Vect location = new Vect(25*20.0, 5);

        TriangleBumper creator = new TriangleBumper(testBoard,2, location, Angle.ZERO, "TB1");

        assertTrue(creator.getType().equals("TriangleBumper"));
    }
    
    //covers incorrect y location
    @Test(expected=AssertionError.class)
    public void testCreatorIncorrectY() {
        Vect location = new Vect(5, 30*20.0);

        TriangleBumper creator = new TriangleBumper(testBoard,2, location, Angle.ZERO, "TB1");

        assertTrue(creator.getType().equals("TriangleBumper"));
    }
    
    //covers incorrect orientation
    @Test(expected=AssertionError.class)
    public void testCreatorIncorrectOrientation() {
        Vect location = new Vect(5, 5);

        TriangleBumper creator = new TriangleBumper(testBoard,2, location, Angle.DEG_135, "TB1");

        assertTrue(creator.getType().equals("TriangleBumper"));
    }
    
    //Test getReflectionCoefficient
    @Test
    public void testRelectionCoefficient() {
        Vect location = new Vect(0, 10);

        TriangleBumper bumper = new TriangleBumper(testBoard,2, location, Angle.ZERO, "TB1");

        assertTrue(bumper.getReflectionCoefficient() == 1);
        assertTrue(bumper.getReflectionCoefficient() == 1.0);
        assertFalse(bumper.getReflectionCoefficient() == 2);
    }
    
    //Test getLocation, getOrientation, getWidth, getHeight
    @Test
    public void testGetMethods() {
        Vect location = new Vect(14, 14);

        TriangleBumper bumper = new TriangleBumper(testBoard,7, location, Angle.ZERO, "TB1");

        
        assertEquals(new Vect(14, 14), bumper.getLocation());
        assertEquals(Angle.ZERO, bumper.getOrientation());
        assertEquals(7, bumper.getWidth());
        assertEquals(7, bumper.getHeight());
        assertEquals("TriangleBumper", bumper.getType());
    }
    
    
    //Test Hashcode() and Equals() Method
    
    //covers all same fields for two absorbers
    @Test
    public void testHashcodeEqualsDiffName() {
        Vect location = new Vect(8, 5);

        TriangleBumper correct = new TriangleBumper(testBoard,5, location, Angle.ZERO, "TB1");
        TriangleBumper other = new TriangleBumper(testBoard,5, location, Angle.ZERO, "TB2");

        
        assertTrue(correct.equals(other));
        assertTrue(correct.hashCode() != other.hashCode());
    }
    
    
    //covers different location
    @Test
    public void testHashcodeEqualsSameName() {
        Vect location = new Vect(3, 5);
        
        TriangleBumper tb1 = new TriangleBumper(testBoard,4, location, Angle.ZERO, "TB1");
        TriangleBumper tb2 = new TriangleBumper(testBoard,4, location, Angle.ZERO, "TB1");

        
        assertTrue(tb1.equals(tb2));
        assertTrue(tb1.hashCode() == tb2.hashCode());
    }
    

    
    //covers toString method
    @Test
    public void testToString() {
        Vect location = new Vect(10, 10);

        TriangleBumper bumper = new TriangleBumper(testBoard,4, location, Angle.ZERO, "TB1");

        assertEquals("Triangle Bumper at <10.0,10.0> with orientation Angle(1.0,0.0) and leg length 4", bumper.toString());
    }

}
