package flingball;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import physics.Angle;
import physics.LineSegment;


public class WallTest {

    /* methods that we need to test:
     *  -creator method (incorrect)
     *  -getLocation
     *  -getLineSegments
     *  -getReflectionCoefficient
     *  -getOrientation
     *  -getHeight
     *  -get Width
     *  -getType()
     *  -hashcode()
     *  -equals()
     *  -toString()
     *  
     *  partitions for creator:
     *      input is LineSegment
     *      line segment
     *      
     *  partitions getReflectionCoefficient:
     *      should be zero every time. Check once
     *      
     *  partitions for getLocation, getOrientation, getWidth, getHeight
     *      not many partitions, check they return the correct values
     *  
     *  
     *  hashcode and equals can have the same partitions because they test
     *  similar things; Partitions:

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
    public void testGetType() {
        Wall wall = new Wall(testBoard, new LineSegment(0.0, 0.0, 401, 0.0));

        assertTrue(wall.getType().equals("Wall"));

    }

    //covers incorrect width
    @Test(expected=AssertionError.class)
    public void testCreatorIncorrectWidth() {

        Wall wall = new Wall(testBoard, new LineSegment(0.0, 0.0, 401, 0.0));

        assertTrue(wall.getType().equals("Wall"));
    }

    //covers incorrect height
    @Test(expected=AssertionError.class)
    public void testCreatorIncorrectHeight() {
        Wall wall = new Wall(testBoard, new LineSegment(0.0, 0.0, 0.0, 401.0));

        assertTrue(wall.getType().equals("Wall"));
    }


    //Test getReflectionCoefficient
    @Test
    public void testRelectionCoefficient() {
        Wall wall = new Wall(testBoard, new LineSegment(0.0, 0.0, 0.0, 400.0));

        assertTrue(wall.getType().equals("Wall"));
    
        assertTrue(wall.getReflectionCoefficient() == 1);
        assertTrue(wall.getReflectionCoefficient() == 1.0);
        assertFalse(wall.getReflectionCoefficient() == 2);
    }

    //Test getLocation, getOrientation, getWidth, getHeight
    @Test
    public void testGetMethods() {
        Wall wall = new Wall(testBoard, new LineSegment(0.0, 0.0, 0.0, 400.0));


        assertEquals(new LineSegment(0.0, 0.0, 0.0, 400.0).p1(), wall.getLocation());
        assertEquals(new Angle(1.0,0.0), wall.getOrientation());
        assertEquals(0, wall.getWidth());
        assertEquals(400, wall.getHeight());
    }


    //Test Hashcode() and Equals() Method

    @Test
    public void testHashcodeEqualsSameName() {
        Wall wall1 = new Wall(testBoard, new LineSegment(0.0, 0.0, 0.0, 400.0));
        Wall wall2 = new Wall(testBoard, new LineSegment(0.0, 0.0, 0.0, 400.0));


        assertTrue(wall1.equals(wall2));
        assertEquals(wall1.hashCode(), wall2.hashCode());
    }

    //covers different line segments
    @Test
    public void testEqualsDifferentLineSegments() {
        Wall wall1 = new Wall(testBoard, new LineSegment(0.0, 0.0, 400.0, 0.0));
        Wall wall2 = new Wall(testBoard, new LineSegment(0.0, 0.0, 0.0, 400.0));


        assertFalse(wall1.equals(wall2));

        
    }

    @Test
    public void testToString() {
        Wall wall = new Wall(testBoard, new LineSegment(0.0, 0.0, 0.0, 400.0));

        assertTrue(wall.toString().equals("Wall with points <0.0,0.0> and <0.0,400.0>"));
    }

}
