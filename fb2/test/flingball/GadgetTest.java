package flingball;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import physics.Circle;
import physics.LineSegment;
import physics.Vect;

/**
 * Tests for the Gadget interface as well as constructors of the concrete ADTs.
 */
public class GadgetTest {
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    //Testing Strategy
    //getLineSegments: 
    //  CircleBumper - Check that the list is empty
    //  SquareBumper - Partition into origin is (0,0), origin is between 0 and 19, origin is (19,19)
    //  TriangleBumper - 
    //  -Partition into a bumper whose orientation is 0, a bumper whose orientation is 90, a bumper whose orientation is 
    //  180, and a bumper whose orientation is 270
    //  -Partition into origin is (0,0), origin is between 0 and 19, origin is (19,19)
    //  Absorber - 
    //  -Partition into origin is (0,0), origin is between 0 and 19, origin is (19,19)
    //  -Partition width into 1, greater than 1
    //  -Partition height into 1, greater than 1
    //
    //getCircles:
    //  CircleBumper - Partition into a bumper whose origin is (0,0), a bumper whose origin is between 0 and 19, a bumper whose origin
    //  is (19,19)
    //  SquareBumper - Partition into a bumper whose origin is (0,0), a bumper whose origin is between 0 and 19, a bumper whose origin
    //  is (19,19)
    //  TriangleBumper - 
    //  -Partition into a bumper whose orientation is 0, a bumper whose orientation is 90, a bumper whose orientation is 
    //  180, and a bumper whose orientation is 270
    //  -Partition into a bumper whose origin is (0,0), a bumper whose origin is between 0 and 19, a bumper whose origin
    //  is (19,19)
    //  Absorber - 
    //  -Partition into origin is (0,0), origin is between 0 and 19, origin is (19,19)
    //  -Partition width into 1, greater than 1
    //  -Partition height into 1, greater than 1
    //
    //getPosition:
    //  Partition into a gadget whose origin is (0,0), a gadget whose origin is between 0 and 19, a gadget whose origin
    //  is (19,19)
    //
    //addTrigger:
    //  Partition into initial gadget has no target triggering gadget, initial gadget has at least one triggering gadget
    //
    //trigger:
    //  Partition into initial gadget has no triggering gadgets, initial gadget has at least one triggering gadget that performs an action (absorber)
    //
    //sameValue:
    //  Bumper - Partition two bumpers that have same name, position, reflection coefficient; two bumpers with same name, position,
    //  different reflection coefficient; two bumpers with same name, reflection coefficient, different position; two bumpers with same 
    //  position, reflection coefficient, different names
    //  Absorber - Partition into two absorbers that have same name, position, width, height; two absorbers with same name, position, different
    //  width and height; two absorbers with same name, width and height, different position; two absorbers with same position, width and 
    //  height, different names
    
    private static final Vect SHOOTOUT_VELOCITY = new Vect(0, -50);

    private static final Gadget CIRCLE_BUMPER_1 = new CircleBumper ("CircleBumper1",0,0);
    private static final Gadget CIRCLE_BUMPER_2 = new CircleBumper ("CircleBumper2",19,19);
    private static final Gadget CIRCLE_BUMPER_3 = new CircleBumper ("CircleBumper3",10,10,1.7);
    private static final Gadget SQUARE_BUMPER_1 = new SquareBumper ("SquareBumper1",0,0);
    private static final Gadget SQUARE_BUMPER_2 = new SquareBumper ("SquareBumper2",10,10);
    private static final Gadget SQUARE_BUMPER_3 = new SquareBumper ("SquareBumper3",19,19);
    private static final Gadget TRIANGLE_BUMPER_1 = new TriangleBumper ("TriangleBumper1",0,0);
    private static final Gadget TRIANGLE_BUMPER_2 = new TriangleBumper ("TriangleBumper2",10,10,90,1.8);
    private static final Gadget TRIANGLE_BUMPER_3 = new TriangleBumper ("TriangleBumper3",19,19,180,2.0);
    private static final Gadget TRIANGLE_BUMPER_4 = new TriangleBumper ("TriangleBumper3",19,19,270,0.8);
    private static final Gadget ABSORBER_1 = new Absorber("Absorber1",0,0,1,1);
    private static final Gadget ABSORBER_2 = new Absorber("Absorber2",10,10,5,5);
    private static final Gadget ABSORBER_3 = new Absorber("Absorber3",19,19,1,1);
    
    private static final Vect ZERO = new Vect(0,0);
    private static final Vect TEN = new Vect(10,10);
    private static final Vect NINETEEN = new Vect(19,19);
    private static final Vect ONE_ZERO = new Vect(1,0);
    private static final Vect ZERO_ONE = new Vect(0,1);
    private static final Vect ONE = new Vect(1,1);
    private static final Vect ELEVEN_TEN = new Vect(11,10);
    private static final Vect TEN_ELEVEN = new Vect(10,11);
    private static final Vect ELEVEN = new Vect(11,11);
    private static final Vect TWENTY_NINETEEN = new Vect(20,19);
    private static final Vect NINETEEN_TWENTY = new Vect(19,20);
    private static final Vect END = new Vect(20,20);
    private static final Vect TEN_FIFTEEN = new Vect(10,15);
    private static final Vect FIFTEEN_TEN = new Vect(15,10);
    private static final Vect FIFTEEN = new Vect(15,15);
  
    private static final Circle ZERO_C = new Circle(ZERO,0);
    private static final Circle TEN_C = new Circle(TEN,0);
    private static final Circle NINETEEN_C = new Circle(NINETEEN,0);
    private static final Circle ONE_ZERO_C = new Circle(ONE_ZERO,0);
    private static final Circle ZERO_ONE_C = new Circle(ZERO_ONE,0);
    private static final Circle ONE_C = new Circle(ONE,0);
    private static final Circle ELEVEN_TEN_C = new Circle(ELEVEN_TEN,0);
    private static final Circle TEN_ELEVEN_C = new Circle(TEN_ELEVEN,0);
    private static final Circle ELEVEN_C = new Circle(ELEVEN,0);
    private static final Circle TWENTY_NINETEEN_C = new Circle(TWENTY_NINETEEN,0);
    private static final Circle NINETEEN_TWENTY_C = new Circle(NINETEEN_TWENTY,0);
    private static final Circle END_C = new Circle(END,0);
    private static final Circle TEN_FIFTEEN_C = new Circle(TEN_FIFTEEN,0);
    private static final Circle FIFTEEN_TEN_C = new Circle(FIFTEEN_TEN,0);
    private static final Circle FIFTEEN_C = new Circle(FIFTEEN,0);
        
    private static final LineSegment ZERO_TO_RIGHT = new LineSegment(ZERO,ONE_ZERO);
    private static final LineSegment ZERO_FROM_RIGHT = new LineSegment(ONE_ZERO,ZERO);
    private static final LineSegment ZERO_TO_DOWN = new LineSegment(ZERO,ZERO_ONE);
    private static final LineSegment ZERO_FROM_DOWN = new LineSegment(ZERO_ONE,ZERO);
    private static final LineSegment ONE_TO_UP = new LineSegment(ONE,ONE_ZERO);
    private static final LineSegment ONE_FROM_UP = new LineSegment(ONE_ZERO,ONE);
    private static final LineSegment ONE_TO_LEFT = new LineSegment(ONE,ZERO_ONE);
    private static final LineSegment ONE_FROM_LEFT = new LineSegment(ZERO_ONE,ONE);
    private static final LineSegment ONE_ZERO_TO_ZERO_ONE = new LineSegment(ONE_ZERO,ZERO_ONE);
    private static final LineSegment ZERO_ONE_TO_ONE_ZERO = new LineSegment(ZERO_ONE,ONE_ZERO);
      
    private static final LineSegment TEN_TO_RIGHT = new LineSegment(TEN,ELEVEN_TEN);
    private static final LineSegment TEN_FROM_RIGHT = new LineSegment(TEN_ELEVEN,TEN);
    private static final LineSegment TEN_TO_DOWN = new LineSegment(TEN,ELEVEN_TEN);
    private static final LineSegment TEN_FROM_DOWN = new LineSegment(ELEVEN_TEN,TEN);
    private static final LineSegment ELEVEN_TO_UP = new LineSegment(ELEVEN,ELEVEN_TEN);
    private static final LineSegment ELEVEN_FROM_UP = new LineSegment(ELEVEN_TEN,ELEVEN);
    private static final LineSegment ELEVEN_TO_LEFT = new LineSegment(ELEVEN,TEN_ELEVEN);
    private static final LineSegment ELEVEN_FROM_LEFT = new LineSegment(TEN_ELEVEN,ELEVEN);
    private static final LineSegment TEN_TO_ELEVEN = new LineSegment(TEN,ELEVEN);
    private static final LineSegment ELEVEN_TO_TEN = new LineSegment(ELEVEN,TEN);
       
    private static final LineSegment NINETEEN_TO_RIGHT = new LineSegment(NINETEEN,TWENTY_NINETEEN);
    private static final LineSegment NINETEEN_FROM_RIGHT = new LineSegment(TWENTY_NINETEEN,NINETEEN);
    private static final LineSegment NINETEEN_TO_DOWN = new LineSegment(NINETEEN,NINETEEN_TWENTY);
    private static final LineSegment NINETEEN_FROM_DOWN = new LineSegment(NINETEEN_TWENTY,NINETEEN);
    private static final LineSegment TWENTY_TO_UP = new LineSegment(END,TWENTY_NINETEEN);
    private static final LineSegment TWENTY_FROM_UP = new LineSegment(TWENTY_NINETEEN,END);
    private static final LineSegment TWENTY_TO_LEFT = new LineSegment(END,NINETEEN_TWENTY);
    private static final LineSegment TWENTY_FROM_LEFT = new LineSegment(NINETEEN_TWENTY,END);
    private static final LineSegment TWENTY_NINETEEN_TO_NINETEEN_TWENTY = new LineSegment(TWENTY_NINETEEN,NINETEEN_TWENTY);
    private static final LineSegment NINETEEN_TWENTY_TO_TWENTY_NINETEEN = new LineSegment(NINETEEN_TWENTY,TWENTY_NINETEEN);
    private static final LineSegment NINETEEN_TO_TWENTY = new LineSegment(NINETEEN,END);
    private static final LineSegment TWENTY_TO_NINETWEEN = new LineSegment(END,NINETEEN);
            
    private static final LineSegment TEN_TO_FIVE_RIGHT = new LineSegment(TEN,FIFTEEN_TEN);
    private static final LineSegment TEN_FROM_FIVE_RIGHT = new LineSegment(FIFTEEN_TEN,TEN);
    private static final LineSegment TEN_TO_FIVE_DOWN = new LineSegment(TEN,TEN_FIFTEEN);
    private static final LineSegment TEN_FROM_FIVE_DOWN = new LineSegment(TEN_FIFTEEN,TEN);
    private static final LineSegment FIFTEEN_TO_FIVE_UP = new LineSegment(FIFTEEN,FIFTEEN_TEN);
    private static final LineSegment FIFTEEN_FROM_FIVE_UP = new LineSegment(FIFTEEN_TEN,FIFTEEN);
    private static final LineSegment FIFTEEN_TO_FIVE_LEFT = new LineSegment(FIFTEEN,TEN_FIFTEEN);
    private static final LineSegment FIFTEEN_FROM_FIVE_LEFT = new LineSegment(TEN_FIFTEEN,FIFTEEN);

    //covers test of getLineSegments checking CircleBumper's list is empty
    @Test
    public void testGetLineSegmentsCircleEmpty() {
        assertEquals("Expected CircleBumper to have no lines",Collections.EMPTY_LIST,CIRCLE_BUMPER_1.getLineSegments());
    }
    
    //covers test of getLineSegments checking a square bumper whose origin is (0,0)
    @Test
    public void testGetLineSegmentsSquareBumperZero() {
        final List<LineSegment> lines = SQUARE_BUMPER_1.getLineSegments();
        assertEquals("Expected 4 lines in a square",4,lines.size());
        assertTrue("expected line passing through"+ZERO_TO_RIGHT,lines.contains(ZERO_TO_RIGHT)||lines.contains(ZERO_FROM_RIGHT));
        assertTrue("expected line passing through"+ZERO_TO_DOWN,lines.contains(ZERO_TO_DOWN)||lines.contains(ZERO_FROM_DOWN));
        assertTrue("expected line passing through"+ONE_TO_UP,lines.contains(ONE_TO_UP)||lines.contains(ONE_FROM_UP));
        assertTrue("expected line passing through"+ONE_TO_LEFT,lines.contains(ONE_TO_LEFT)||lines.contains(ONE_FROM_LEFT));
    }
    
    //covers test of getLineSegments checking a square bumper whose origin is between 0 and 19
    @Test
    public void testGetLineSegmentsSquareBumperMiddle() {
        final List<LineSegment> lines = SQUARE_BUMPER_2.getLineSegments();
        assertEquals("Expected 4 lines in a square",4,lines.size());
        assertTrue("expected line passing through"+TEN_TO_RIGHT,lines.contains(TEN_TO_RIGHT)||lines.contains(TEN_FROM_RIGHT));
        assertTrue("expected line passing through"+TEN_TO_DOWN,lines.contains(TEN_TO_DOWN)||lines.contains(TEN_FROM_DOWN));
        assertTrue("expected line passing through"+ELEVEN_TO_UP,lines.contains(ELEVEN_TO_UP)||lines.contains(ELEVEN_FROM_UP));
        assertTrue("expected line passing through"+ELEVEN_TO_LEFT,lines.contains(ELEVEN_TO_LEFT)||lines.contains(ELEVEN_FROM_LEFT));
    }
    
    //covers test of getLineSegments checking a square bumper whose origin is at (19,19)
    @Test
    public void testGetLineSegmentsSquareBumperEnd() {
        final List<LineSegment> lines = SQUARE_BUMPER_3.getLineSegments();
        assertEquals("Expected 4 lines in a square",4,lines.size());
        assertTrue("expected line passing through"+NINETEEN_TO_RIGHT,lines.contains(NINETEEN_TO_RIGHT)||lines.contains(NINETEEN_FROM_RIGHT));
        assertTrue("expected line passing through"+NINETEEN_TO_DOWN,lines.contains(NINETEEN_TO_DOWN)||lines.contains(NINETEEN_FROM_DOWN));
        assertTrue("expected line passing through"+TWENTY_TO_UP,lines.contains(TWENTY_TO_UP)||lines.contains(TWENTY_FROM_UP));
        assertTrue("expected line passing through"+TWENTY_TO_LEFT,lines.contains(TWENTY_TO_LEFT)||lines.contains(TWENTY_FROM_LEFT));
        
    }
    
    //covers test of getLineSegments checking a triangle bumper whose origin is (0,0) and whose orientation is 0
    @Test
    public void testGetLineSegmentsTriangleBumperZeroOrientationZero() {
        final List<LineSegment> lines = TRIANGLE_BUMPER_1.getLineSegments();
        assertEquals("Expected 3 lines in a triangle",3,lines.size());
        assertTrue("expected line passing through"+ZERO_TO_RIGHT,lines.contains(ZERO_TO_RIGHT)||lines.contains(ZERO_FROM_RIGHT));
        assertTrue("expected line passing through"+ZERO_TO_DOWN,lines.contains(ZERO_TO_DOWN)||lines.contains(ZERO_FROM_DOWN));
        assertTrue("expected line passing through"+ONE_ZERO_TO_ZERO_ONE,lines.contains(ONE_ZERO_TO_ZERO_ONE)||lines.contains(ZERO_ONE_TO_ONE_ZERO));
    }
    
    //covers test of getLineSegments checking a triangle bumper whose origin is between 0 and 19 and whose orientation is 90
    @Test
    public void testGetLineSegmentsTriangleBumperMidOrientation90() {
        final List<LineSegment> lines = TRIANGLE_BUMPER_2.getLineSegments();
        assertEquals("Expected 3 lines in a triangle",3,lines.size());
        assertTrue("expected line passing through"+TEN_TO_RIGHT,lines.contains(TEN_TO_RIGHT)||lines.contains(TEN_FROM_RIGHT));
        assertTrue("expected line passing through"+ELEVEN_TO_UP,lines.contains(ELEVEN_TO_UP)||lines.contains(ELEVEN_FROM_UP));
        assertTrue("expected line passing through"+ELEVEN_TO_TEN,lines.contains(ELEVEN_TO_TEN)||lines.contains(TEN_TO_ELEVEN));
    }
    
    //covers test of getLineSegments checking a triangle bumper whose origin is (19,19) and whose orientation is 180
    @Test
    public void testGetLineSegmentsTriangleBumperEndOrientation180() {
        final List<LineSegment> lines = TRIANGLE_BUMPER_3.getLineSegments();
        assertEquals("Expected 3 lines in a triangle",3,lines.size());
        assertTrue("expected line passing through"+NINETEEN_TWENTY_TO_TWENTY_NINETEEN,lines.contains(NINETEEN_TWENTY_TO_TWENTY_NINETEEN)||lines.contains(TWENTY_NINETEEN_TO_NINETEEN_TWENTY));
        assertTrue("expected line passing through"+TWENTY_TO_UP,lines.contains(TWENTY_TO_UP)||lines.contains(TWENTY_FROM_UP));
        assertTrue("expected line passing through"+TWENTY_TO_LEFT,lines.contains(TWENTY_TO_LEFT)||lines.contains(TWENTY_FROM_LEFT));
        
    } 
    
    //covers test of getLineSegments checking a triangle bumper whose origin is (19,19) and whose orientation is 270
    @Test
    public void testGetLineSegmentsTriangleBumperEndOrientation270() {
        final List<LineSegment> lines = TRIANGLE_BUMPER_4.getLineSegments();
        assertEquals("Expected 3 lines in a triangle",3,lines.size());
        assertTrue("expected line passing through"+NINETEEN_TO_DOWN,lines.contains(NINETEEN_TO_DOWN)||lines.contains(NINETEEN_FROM_DOWN));
        assertTrue("expected line passing through"+TWENTY_TO_NINETWEEN,lines.contains(TWENTY_TO_NINETWEEN)||lines.contains(NINETEEN_TO_TWENTY));
        assertTrue("expected line passing through"+TWENTY_TO_LEFT,lines.contains(TWENTY_TO_LEFT)||lines.contains(TWENTY_FROM_LEFT));
    } 
    
    //covers test of getLineSegments checking an absorber whose origin is (0,0) and whose width is 1, and whose height is 1
    @Test
    public void testGetLineSegmentsAbsorberZeroWidth1Height1() {
        final List<LineSegment> lines = ABSORBER_1.getLineSegments();
        assertEquals("Expected 4 lines",4,lines.size());
        assertTrue("expected line passing through"+ZERO_TO_RIGHT,lines.contains(ZERO_TO_RIGHT)||lines.contains(ZERO_FROM_RIGHT));
        assertTrue("expected line passing through"+ZERO_TO_DOWN,lines.contains(ZERO_TO_DOWN)||lines.contains(ZERO_FROM_DOWN));
        assertTrue("expected line passing through"+ONE_TO_UP,lines.contains(ONE_TO_UP)||lines.contains(ONE_FROM_UP));
        assertTrue("expected line passing through"+ONE_TO_LEFT,lines.contains(ONE_TO_LEFT)||lines.contains(ONE_FROM_LEFT));
    }
    
    //covers test of getLineSegments checking an absorber whose origin is between 0 and 19 and whose width is greater than 1, and whose height is greater than 1
    @Test
    public void testGetLineSegmentsAbsorberMidWidthLargeHeightLarge() {
        final List<LineSegment> lines = ABSORBER_2.getLineSegments();
        assertEquals("Expected 4 lines",4,lines.size());
        assertTrue("expected line passing through"+TEN_TO_FIVE_RIGHT,lines.contains(TEN_TO_FIVE_RIGHT)||lines.contains(TEN_FROM_FIVE_RIGHT));
        assertTrue("expected line passing through"+TEN_TO_FIVE_DOWN,lines.contains(TEN_TO_FIVE_DOWN)||lines.contains(TEN_FROM_FIVE_DOWN));
        assertTrue("expected line passing through"+FIFTEEN_TO_FIVE_UP,lines.contains(FIFTEEN_TO_FIVE_UP)||lines.contains(FIFTEEN_FROM_FIVE_UP));
        assertTrue("expected line passing through"+FIFTEEN_TO_FIVE_LEFT,lines.contains(FIFTEEN_TO_FIVE_LEFT)||lines.contains(FIFTEEN_FROM_FIVE_LEFT));
    }
    
    //covers test of getLineSegments checking an absorber whose origin is (19,19) and whose width is 1, and whose height is 1
    @Test
    public void testGetLineSegmentsAbsorberEndWidth1Height1() {
        final List<LineSegment> lines = ABSORBER_3.getLineSegments();
        assertEquals("Expected 4 lines",4,lines.size());
        assertTrue("expected line passing through"+NINETEEN_TO_RIGHT,lines.contains(NINETEEN_TO_RIGHT)||lines.contains(NINETEEN_FROM_RIGHT));
        assertTrue("expected line passing through"+NINETEEN_TO_DOWN,lines.contains(NINETEEN_TO_DOWN)||lines.contains(NINETEEN_FROM_DOWN));
        assertTrue("expected line passing through"+TWENTY_TO_UP,lines.contains(TWENTY_TO_UP)||lines.contains(TWENTY_FROM_UP));
        assertTrue("expected line passing through"+TWENTY_TO_LEFT,lines.contains(TWENTY_TO_LEFT)||lines.contains(TWENTY_FROM_LEFT));
    }
    
    //covers test of getCircles of a circle bumper whose origin is (0,0)
    @Test
    public void testGetCirclesCircleBumperZero() {
        final List<Circle> circles = CIRCLE_BUMPER_1.getCircles();
        assertEquals("Expected one circle at (0.5,0.5)",Arrays.asList(new Circle(new Vect(0.5,0.5),0.5)),circles);
        
    }
    
    //covers test of getCircles of a circle bumper whose origin is between 0 and 19
    @Test
    public void testGetCirclesCircleBumperMid() {
        final List<Circle> circles = CIRCLE_BUMPER_3.getCircles();
        assertEquals("Expected one circle at (10.5,10.5)",Arrays.asList(new Circle(new Vect(10.5,10.5),0.5)),circles);
    }
    
    //covers test of getCircles of a circle bumper whose origin is (19.5,19.5)
    @Test
    public void testGetCirclesCircleBumperEnd() {
        final List<Circle> circles = CIRCLE_BUMPER_2.getCircles();
        assertEquals("Expected one circle at (19.5,19.5)",Arrays.asList(new Circle(new Vect(19.5,19.5),0.5)),circles);
    }
    
    //covers test of getCircles of a square bumper whose origin is (0,0)
    @Test
    public void testGetCirclesSquareBumperZero() {
        final List<Circle> circles = SQUARE_BUMPER_1.getCircles();
        assertEquals("Expected 4 circles at (0,0), (0,1), (1,0), (1,1)",new HashSet<Circle>(Arrays.asList(ZERO_C,ZERO_ONE_C,ONE_ZERO_C,ONE_C)),new HashSet<Circle>(circles));//covert to set
        //because order of the circles in the list doesn't matter
        
    }
    
    //covers test of getCircles of a square bumper whose origin is between 0 and 19
    @Test
    public void testGetCirclesSquareBumperMid() {
        final List<Circle> circles = SQUARE_BUMPER_2.getCircles();
        assertEquals("Expected 4 circles at (10,10), (10,11), (11,10), (11,11)",new HashSet<Circle>(Arrays.asList(TEN_C,TEN_ELEVEN_C,ELEVEN_TEN_C,ELEVEN_C)),new HashSet<Circle>(circles));
    }
    
    //covers test of getCircles of a square bumper whose origin is (19,19)
    @Test
    public void testGetCirclesSquareBumperEnd() {
        final List<Circle> circles = SQUARE_BUMPER_3.getCircles();
        assertEquals("Expected 4 circles at (19,19), (19,20), (20,19), (20,20)",new HashSet<Circle>(Arrays.asList(NINETEEN_C,NINETEEN_TWENTY_C,TWENTY_NINETEEN_C,END_C)),new HashSet<Circle>(circles));
    }
    
    //covers test of getCircles checking a triangle bumper whose origin is (0,0) and whose orientation is 0
    @Test
    public void testGetCirclesTriangleBumperZeroOrientationZero() {
        final List<Circle> circles = TRIANGLE_BUMPER_1.getCircles();
        assertEquals("Expected 3 circles at (0,0), (1,0), (0,1)",new HashSet<Circle>(Arrays.asList(ZERO_C,ONE_ZERO_C,ZERO_ONE_C)),new HashSet<Circle>(circles));
    }
    
    //covers test of getCircles checking a triangle bumper whose origin is between 0 and 19 and whose orientation is 90
    @Test
    public void testGetCirclesTriangleBumperMidOrientation90() {
        final List<Circle> circles = TRIANGLE_BUMPER_2.getCircles();
        assertEquals("Expected 3 circles at (10,10), (11,10), (11,11)",new HashSet<Circle>(Arrays.asList(TEN_C,ELEVEN_TEN_C,ELEVEN_C)),new HashSet<Circle>(circles));
    }
    
    //covers test of getCircles checking a triangle bumper whose origin is (19,19) and whose orientation is 180
    @Test
    public void testGetCirclesTriangleBumperEndOrientation180() {
        final List<Circle> circles = TRIANGLE_BUMPER_3.getCircles();
        assertEquals("Expected 3 circles at (19,20), (20,20), (20,19)",new HashSet<Circle>(Arrays.asList(NINETEEN_TWENTY_C,END_C,TWENTY_NINETEEN_C)),new HashSet<Circle>(circles));
        
    } 
    
    //covers test of getCircles checking a triangle bumper whose origin is (19,19) and whose orientation is 270
    @Test
    public void testGetCirclesTriangleBumperEndOrientation270() {
        final List<Circle> circles = TRIANGLE_BUMPER_4.getCircles();
        assertEquals("Expected 3 circles at (19,19), (19,20), (20,20)",new HashSet<Circle>(Arrays.asList(NINETEEN_C,END_C,NINETEEN_TWENTY_C)),new HashSet<Circle>(circles));
    } 
    
    //covers test of getCircles checking an absorber whose origin is (0,0) and whose width is 1 and height is 1
    @Test
    public void testGetCirclesAbsorberZeroWidth1Height1() {
        final List<Circle> circles = ABSORBER_1.getCircles();
        assertEquals("Expected 4 circles at (0,0), (0,1), (1,0), (1,1)",new HashSet<Circle>(Arrays.asList(ZERO_C,ZERO_ONE_C,ONE_ZERO_C,ONE_C)),new HashSet<Circle>(circles));
        
    }
    
    //covers test of getCircles of an absorber whose origin is between 0 and 19, width is greater than 1, and height is greater than 1
    @Test
    public void testGetCirclesAbsorberMidWidthLargeHeightLarge() {
        final List<Circle> circles = ABSORBER_2.getCircles();
        assertEquals("Expected 4 circles at (10,10), (10,15), (15,10), (15,15)",new HashSet<Circle>(Arrays.asList(TEN_C,TEN_FIFTEEN_C,FIFTEEN_TEN_C,FIFTEEN_C)),new HashSet<Circle>(circles));
    }
    
    //covers test of getCircles of an absorber whose origin is (19,19) and width is 1 and height is 1
    @Test
    public void testGetCirclesAbsorber1Width1Height1() {
        final List<Circle> circles = ABSORBER_3.getCircles();
        assertEquals("Expected 4 circles at (19,19), (19,20), (20,19), (20,20)",new HashSet<Circle>(Arrays.asList(NINETEEN_C,NINETEEN_TWENTY_C,TWENTY_NINETEEN_C,END_C)),new HashSet<Circle>(circles));
    }
    
    //covers test of getPosition of a gadget whose origin is (0,0)
    @Test
    public void getPositionZero() {
        assertEquals("Expected position of (0,0)",ZERO,SQUARE_BUMPER_1.getPosition());
    }
    
    //covers test of getPosition of a gadget whose origin is between 0 and 19
    @Test
    public void getPositionMid() {
        assertEquals("Expected position of (10,10)",TEN,ABSORBER_2.getPosition());
    }
    
    //covers test of getPosition of a gadget whose origin is (19,19)
    @Test
    public void getPositionEnd() {
        assertEquals("Expected position of (19,19)",NINETEEN,TRIANGLE_BUMPER_3.getPosition());
    }
    
    //covers test of trigger testing no gadgets to be triggered
    @Test
    public void triggerNoTrigger() {
        final Gadget example = new CircleBumper("CircleBumper1",0,0);
        example.trigger();
        assertTrue("Nothing should have changed when there are no triggers in the gadget",CIRCLE_BUMPER_1.sameValue(example));
    }
    
    //covers test of addTrigger of adding a trigger to a gadget that has no initial trigger and trigger testing initial gadget 
    //with at least one gadget that performs an action
    @Test
    public void addTriggerToEmptyTriggeringOne() {
        final Absorber absorber = new Absorber("Absorber",12,12,1,1);
        final Ball ball = new Ball("Ball",12,11.9,0,5);
        absorber.resolveCollision(ball);
        final Gadget example = new CircleBumper("Example",11,12);
        example.addTrigger(absorber);
        example.trigger();
        assertEquals("Expected ball to be moving at velocity <0, -50>", SHOOTOUT_VELOCITY, ball.getVelocity());
    }
    
    //covers test of addTrigger of adding a trigger to a gadget that has at least one trigger already and trigger testing initial gadget 
    //with at least one gadget that performs an action
    @Test
    public void addTriggerToOneTriggeringOne() {
        final Absorber absorber1 = new Absorber("Absorber1",12,12,1,1);
        final Ball ball1 = new Ball("Ball1",12,11.9,0,5);
        absorber1.resolveCollision(ball1);
        final Absorber absorber2 = new Absorber("Absorber2",17,17,1,1);
        final Ball ball2 = new Ball("Ball2",17,16.9,0,5);
        absorber2.resolveCollision(ball2);
        final Gadget example = new SquareBumper("Example",11,12);
        example.addTrigger(absorber1);
        example.addTrigger(absorber2);
        example.trigger();
        assertEquals("Expected ball1 to be moving at velocity <0, -50>", SHOOTOUT_VELOCITY, ball1.getVelocity());
        assertEquals("Expected ball2 to be moving at velocity <0, -50>", SHOOTOUT_VELOCITY, ball2.getVelocity());
    }
    
    //covers test of sameValue of bumpers that have same name, position, and reflection coefficient
    @Test
    public void testEqualsSameNamePositionReflectionCoeff() {
        final Gadget obtained = new CircleBumper ("CircleBumper3",10,10,1.7);
        assertTrue("Expected to have the same value",CIRCLE_BUMPER_3.sameValue(obtained));
    }
    
    //covers test of sameValue of bumpers that have same name, position, different reflection coefficient
    @Test
    public void testEqualsSameNamePositionDiffReflectionCoeff() {
        final Gadget obtained = new CircleBumper ("CircleBumper3",10,10,1.9);
        assertFalse("Expected to not have the same value",CIRCLE_BUMPER_3.sameValue(obtained));
    }
    
    //covers test of sameValue of bumpers that have same name, reflection coefficient, different position
    @Test
    public void testEqualsSameNameDiffPositionSameReflectionCoeff() {
        final Gadget obtained = new CircleBumper ("CircleBumper3",11,10,1.7);
        assertFalse("Expected to not have the same value",CIRCLE_BUMPER_3.sameValue(obtained));
    }
    
    //covers test of sameValue of bumpers that have different name, same reflection coefficient, same position
    @Test
    public void testEqualsDiffNameSamePositionSameReflectionCoeff() {
        final Gadget obtained = new CircleBumper ("CircleBumper",10,10,1.7);
        assertFalse("Expected to not have the same value",CIRCLE_BUMPER_3.sameValue(obtained));
    }
    
    //covers test of sameValue of bumpers that have different name, reflection coefficient, position
    @Test
    public void testEqualsDiffNamePositionReflectionCoeff() {
        assertFalse("Expected to not have the same value",CIRCLE_BUMPER_3.sameValue(CIRCLE_BUMPER_2));
    }
    
    //covers test of sameValue of absorbers that have same name, position, and width and height
    @Test
    public void testEqualsSameNamePositionSize() {
        final Gadget obtained = new Absorber("Absorber1",0,0,1,1);
        assertTrue("Expected to have the same value",ABSORBER_1.sameValue(obtained));
    }
    
    //covers test of sameValue of absorbers that have same name, position, different width and height
    @Test
    public void testEqualsSameNamePositionDiffSize() {
        final Gadget obtained = new Absorber("Absorber1",0,0,5,5);
        assertFalse("Expected to not have the same value",ABSORBER_1.sameValue(obtained));
    }
    
    //covers test of sameValue of absorbers that have same name, width and height, different position
    @Test
    public void testEqualsSameNameDiffPositionSamePosition() {
        final Gadget obtained = new Absorber("Absorber1",1,10,1,1);
        assertFalse("Expected to not have the same value",ABSORBER_1.sameValue(obtained));
    }
    
    //covers test of sameValue of absorbers that have different name, same position and same width and height
    @Test
    public void testEqualsDiffNameSamePositionSameSize() {
        final Gadget obtained = new Absorber("Absorber",0,0,1,1);
        assertFalse("Expected to not have the same value",ABSORBER_1.sameValue(obtained));
    }
    
    //covers test of sameValue of bumpers that have different name, reflection coefficient, position
    @Test
    public void testEqualsDiffNamePositionSize() {
        assertFalse("Expected to not have the same value",ABSORBER_1.sameValue(ABSORBER_2));
    }
       
}
