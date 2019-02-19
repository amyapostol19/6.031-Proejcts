package flingball;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import edu.mit.eecs.parserlib.UnableToParseException;

public class ParserTest {
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /* Testing strategy:
     * 
     * - Partition the board file according to whether it contains the following or not:
     *     - comments
     *     - blank lines
     *     - whitespace: at the start of a line, at the end of a line, between tokens of a line
     *     - parameters on the board line: gravity, friction1, friction2
     *     - balls
     *     - square bumpers
     *     - circle bumpers
     *     - triangle bumpers: with and without the orientation parameter
     *     - absorber
     *     - triggers: trigger acts on a different gadget or is self-triggering
     *     
     * - Number of gadgets on the board: 0, 1, 2, >2
     *     
     * Each part of the partition is covered by at least one test case.
     */
    
    // This test covers:
    // comments
    // blank lines
    // whitespace: at the start of a line
    // parameters on the board line: none
    // ball
    // number of gadgets = 0
    @Test
    public void testBall() throws UnableToParseException, IOException {
        final double xPos = 0.25;
        final double yPos = 0.25;
        final double xVelocity = 0;
        final double yVelocity = 0;
        final Board expected = new Board("TestBall");
        expected.addBall(new Ball("flingball_0123", xPos, yPos, xVelocity, yVelocity));
        final Board actual = BoardParser.parse("boards/testBall.fb");
        assertTrue("expected board" + expected, expected.equals(actual));
    }
    
    // This test covers:
    // whitespace: at the end of a line
    // parameters on the board line: gravity
    // square bumper
    // number of gadgets = 1
    @Test
    public void testSquareBumper() throws UnableToParseException, IOException {
        final double gravity = 17.;
        final int xPos = 3;
        final int yPos = 5;
        final Board expected = new Board("TestSquareBumper", gravity, Board.DEFAULT_FRICTION, Board.DEFAULT_FRICTION);
        expected.addGadget(new SquareBumper("square", xPos, yPos));
        final Board actual = BoardParser.parse("boards/testSquareBumper.fb");
        assertTrue("expected board" + expected, expected.equals(actual));
    }
    
    // This test covers:
    // parameters on the board line: friction1
    // circle bumper
    // number of gadgets = 1
    @Test
    public void testCircleBumper() throws UnableToParseException, IOException {
        final double friction1 = 0.03;
        final int xPos = 1;
        final int yPos = 1;
        final Board expected = new Board("TestCircleBumper", Board.DEFAULT_GRAVITY, friction1, Board.DEFAULT_FRICTION);
        expected.addGadget(new CircleBumper("circle", xPos, yPos));
        final Board actual = BoardParser.parse("boards/testCircleBumper.fb");
        assertTrue("expected board" + expected, expected.equals(actual));
    }
    
    // This test covers:
    // whitespace: between tokens of a line
    // parameters on the board line: friction2
    // triangle bumper: with the orientation parameter
    @Test
    public void testTriangleBumperWithOrientation() throws UnableToParseException, IOException {
        final double friction2 = 1.;
        final int xPos = 19;
        final int yPos = 19;
        final int orientation = 270;
        final Board expected = new Board("TestTriangleBumperWithOrientation", Board.DEFAULT_GRAVITY, Board.DEFAULT_FRICTION, friction2);
        expected.addGadget(new TriangleBumper("triangle", xPos, yPos, orientation));
        final Board actual = BoardParser.parse("boards/testTriangleBumperWithOrientation.fb");
        assertTrue("expected board" + expected, expected.equals(actual));
    }
    
    // This test covers:
    // parameters on the board line: gravity, friction1
    // triangle bumper: without the orientation parameter
    // number of gadgets = 1
    @Test
    public void testTriangleBumperWithoutOrientation() throws UnableToParseException, IOException {
        final double gravity = 17.;
        final double friction1 = 0.001;
        final int xPos = 19;
        final int yPos = 6;
        final Board expected = new Board("TestTriangleBumperWithoutOrientation", gravity, friction1, Board.DEFAULT_FRICTION);
        expected.addGadget(new TriangleBumper("triangle", xPos, yPos));
        final Board actual = BoardParser.parse("boards/testTriangleBumperWithoutOrientation.fb");
        assertTrue("expected board" + expected, expected.equals(actual));
    }
    
    // This test covers:
    // parameters on the board line: gravity, friction2
    // absorber
    // number of gadgets = 1
    @Test
    public void testAbsorber() throws UnableToParseException, IOException {
        final double gravity = 17.;
        final double friction2 = 6.;
        final int xPos = 0;
        final int yPos = 0;
        final int width = 20;
        final int height = 20;
        final Board expected = new Board("TestAbsorber", gravity, Board.DEFAULT_FRICTION, friction2);
        expected.addGadget(new Absorber("rob", xPos, yPos, width, height));
        final Board actual = BoardParser.parse("boards/testAbsorber.fb");
        assertTrue("expected board" + expected, expected.equals(actual));
    }
    
    // This test covers:
    // blanklines
    // parameters on the board line: friction1, friction2
    // circle bumper
    // absorber
    // triggers: trigger acts on a different gadget
    // number of gadgets = 2
    @Test
    public void testActionTrigger() throws UnableToParseException, IOException {
        final double friction1 = 0.001;
        final double friction2 = 0.37;
        final int xPosAbs = 0;
        final int yPosAbs = 19;
        final int width = 20;
        final int height = 1;
        final int xPosCirc = 3;
        final int yPosCirc = 7;
        final Board expected = new Board("TestActionTrigger", Board.DEFAULT_GRAVITY, friction1, friction2);
        final Absorber rob = new Absorber("rob", xPosAbs, yPosAbs, width, height);
        final CircleBumper circle = new CircleBumper("circle", xPosCirc, yPosCirc);
        circle.addTrigger(rob);
        expected.addGadget(rob);
        expected.addGadget(circle);
        final Board actual = BoardParser.parse("boards/testActionTrigger.fb");
        assertTrue("expected board" + expected, expected.equals(actual));
    }
    
    // This test covers:
    // comments
    // parameters on the board line: gravity, friction1, friction2
    // triangle bumper: with the orientation parameter
    // absorber
    // triggers: trigger is self-triggering
    // number of gadgets = 2
    @Test
    public void testSelfTrigger() throws UnableToParseException, IOException {
        final double gravity = 23.;
        final double friction1 = 0.1;
        final double friction2 = 0.5;
        final int xPosAbs = 0;
        final int yPosAbs = 19;
        final int width = 20;
        final int height = 1;
        final int xPosTri = 19;
        final int yPosTri = 0;
        final int orientation = 90;
        final Board expected = new Board("TestSelfTrigger", gravity, friction1, friction2);
        final Absorber rob = new Absorber("rob", xPosAbs, yPosAbs, width, height);
        final TriangleBumper triangle = new TriangleBumper("triangle", xPosTri, yPosTri, orientation);
        rob.addTrigger(rob);
        expected.addGadget(rob);
        expected.addGadget(triangle);
        final Board actual = BoardParser.parse("boards/testSelfTrigger.fb");
        assertTrue("expected board" + expected, expected.equals(actual));
    }
    
    // This test covers:
    // parameters on the board line: gravity, friction2
    // ball
    // square bumper
    // circle bumper
    // traingle bumper: without the orientation parameter
    // absorber
    // triggers: trigger acts on a different gadget or is self-triggering
    // number of gadgets > 2
    @Test
    public void testBallGadgetsTriggers() throws UnableToParseException, IOException {
        final double gravity = 17.;
        final double friction2 = 6.;
        final double xPosBall = 1.;
        final double yPosBall = 1.;
        final double xVelocity = 25.;
        final double yVelocity = 0.;
        final int xPosSqu = 3;
        final int yPosSqu = 5;
        final int xPosCirc = 3;
        final int yPosCirc = 7;
        final int xPosTri = 5;
        final int yPosTri = 5;
        final int xPosAbs = 0;
        final int yPosAbs = 19;
        final int width = 20;
        final int height = 1;
        
        final Board expected = new Board("TestBallGadgetsTriggers", gravity, Board.DEFAULT_FRICTION, friction2);
        final Ball ball = new Ball("flingball_0123", xPosBall, yPosBall, xVelocity, yVelocity);
        final SquareBumper square = new SquareBumper("square", xPosSqu, yPosSqu);
        final CircleBumper circle = new CircleBumper("circle", xPosCirc, yPosCirc);
        final TriangleBumper triangle = new TriangleBumper("triangle", xPosTri, yPosTri);
        final Absorber rob = new Absorber("rob", xPosAbs, yPosAbs, width, height);
        
        square.addTrigger(rob);
        circle.addTrigger(rob);
        rob.addTrigger(rob);
        expected.addBall(ball);
        expected.addGadget(square);
        expected.addGadget(circle);
        expected.addGadget(triangle);
        expected.addGadget(rob);
        
        final Board actual = BoardParser.parse("boards/testBallGadgetsTriggers.fb");
        assertTrue("expected board" + expected, expected.equals(actual));
    }
    
}
