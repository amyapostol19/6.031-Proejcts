package flingball;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import physics.Angle;
import physics.Vect;


public class SquareBumperTest {
    
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
     * getLineSegments(): TODO
     * 
     * getType()
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
    
    //covers incorrect width and height
    @Test(expected=AssertionError.class)
    public void testCreatorIncorrectWidth() {
        Vect location = new Vect(5, 5);

        SquareBumper creator = new SquareBumper(testBoard,-5, location, Angle.ZERO, "SB1");

        assertTrue(creator.getType().equals("SquareBumper"));
    }
    
    //covers incorrect x location
    @Test(expected=AssertionError.class)
    public void testCreatorIncorrectX() {
        Vect location = new Vect(25*20.0, 5);

        SquareBumper creator = new SquareBumper(testBoard,2, location, Angle.ZERO, "SB1");

        assertTrue(creator.getType().equals("SquareBumper"));
    }
    
    //covers incorrect y location
    @Test(expected=AssertionError.class)
    public void testCreatorIncorrectY() {
        Vect location = new Vect(5, 30*20.0);

        SquareBumper creator = new SquareBumper(testBoard,2, location, Angle.ZERO, "SB1");

        assertTrue(creator.getType().equals("SquareBumper"));
    }
    
    //Test getReflectionCoefficient
    @Test
    public void testRelectionCoefficient() {
        Vect location = new Vect(0, 10);

        SquareBumper bumper = new SquareBumper(testBoard,2, location, Angle.ZERO, "SB1");

        assertTrue(bumper.getReflectionCoefficient() == 1);
        assertTrue(bumper.getReflectionCoefficient() == 1.0);
        assertFalse(bumper.getReflectionCoefficient() == 2);
    }
    
    //Test getLocation, getOrientation, getWidth, getHeight
    @Test
    public void testGetMethods() {
        Vect location = new Vect(14, 14);

        SquareBumper bumper = new SquareBumper(testBoard,7, location, Angle.ZERO, "SB1");

        
        assertEquals(new Vect(14, 14), bumper.getLocation());
        assertEquals(Angle.ZERO, bumper.getOrientation());
        assertEquals(7, bumper.getWidth());
        assertEquals(7, bumper.getHeight());
        assertEquals("SquareBumper", bumper.getType());
    }
    
    
    //Test Hashcode() and Equals() Method
    
    //covers all same fields for two absorbers
    //TODO hashcode is wrong
    @Test
    public void testHashcodeEqualsSameName() {
        Vect location = new Vect(8, 5);

        SquareBumper sb1 = new SquareBumper(testBoard,5, location, Angle.ZERO, "SB1");
        SquareBumper sb2 = new SquareBumper(testBoard,5, location, Angle.ZERO, "SB1");

        
        assertTrue(sb1.equals(sb2));
        assertEquals(sb1.hashCode(), sb2.hashCode());
    }
    
    
    //covers different location
    @Test
    public void testHashcodeEqualsDiffNameSameCoords() {
        Vect location = new Vect(3, 5);
        

        SquareBumper sb1 = new SquareBumper(testBoard,4, location, Angle.ZERO, "SB1");
        SquareBumper sb2 = new SquareBumper(testBoard,4, location, Angle.ZERO, "SB2");

        
        assertTrue(sb1.equals(sb2));
        assertTrue(sb1.hashCode() != sb2.hashCode());
    }
    
    
    //covers toString method TODO
    @Test
    public void testToString() {
        Vect location = new Vect(10, 10);

        SquareBumper bumper = new SquareBumper(testBoard,4, location, Angle.ZERO, "SB1");

        System.out.println(bumper.toString());
        assertEquals("Square Bumper at <10.0,10.0> with orientation Angle(1.0,0.0) and side length 4", bumper.toString());
    }

}
