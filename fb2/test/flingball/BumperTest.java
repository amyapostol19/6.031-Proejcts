package flingball;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BumperTest {
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // Testing Strategy
    // - getReflecitonCoefficient: partition into a bumper with a default coefficient, 
    //                             and a bumper with a coefficient greater than 1
    
    private static final Bumper CIRCLE_BUMPER_1 = new CircleBumper ("CircleBumper2",19,19);
    private static final Bumper CIRCLE_BUMPER_2 = new CircleBumper ("CircleBumper3",10,10,1.7);
    
    // This test covers: a bumper with a default coefficient
    @Test
    public void testGetReflectionCoefficientDefault() {
        assertEquals("Expected default coefficient of 1.0",1,CIRCLE_BUMPER_1.getReflectionCoeff(),0.0001);
    }
    
    // This test covers: a bumper with a coefficient greater than 1
    @Test
    public void testGetReflectionCoefficientGreaterThanOne() {
        assertEquals("Expected coefficient of 1.7",1.7,CIRCLE_BUMPER_2.getReflectionCoeff(),0.0001);
    }
    
}
