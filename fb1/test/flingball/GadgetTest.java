package flingball;

import static org.junit.Assert.*;

import org.junit.Test;


public class GadgetTest {

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
    
    @Test public void test() {
        assertTrue(true);

    }

}
