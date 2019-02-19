package flingball;

import java.io.IOException;

import org.junit.Test;

import edu.mit.eecs.parserlib.UnableToParseException;

public class SimulatorTest {
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    //To test Simulator, we observe the boards and check to see that Flingball.render() renders the board correctly
    //testing render of Flingball.java
    //  partition input board into a board with default parameters and no gadgets, board with just one absorber,
    //  board with just a ball, board with just a circle bumper, board with just a square bumper, board with just
    //  a triangle bumper without orientation, board with just a triangle bumper with orientation, board with an
    //  action trigger, a board with multiple action triggers, board with a combination of gadgets
    
    //covers test of board with default parameters
    public static void main(String[] args) throws IOException, UnableToParseException{
            //tests a board with default parameters
            Flingball.render(new Board("Default")); //check to see it should just be a black board
            
            //tests a board with just an absorber
            Flingball.render(BoardParser.parse("boards/testAbsorber.fb")); //check to see there is just one absorber
            
            //tests a board with just a ball
            Flingball.render(BoardParser.parse("boards/testBall.fb")); //check to see there is just one ball
            
            //tests a board with just a circle bumper
            Flingball.render(BoardParser.parse("boards/testCircleBumper.fb")); //check to see there is just one circle bumper
            
            //tests a board with just a square bumper 
            Flingball.render(BoardParser.parse("boards/testSquareBumper.fb")); //check to see there is just one square bumper
            
            //tests a board with just a triangle bumper without orientation
            Flingball.render(BoardParser.parse("boards/testTriangleBumperWithoutOrientation.fb")); //check to see there is just one triangle bumper
            
            //tests a board with just a triangle bumper with orientation
            Flingball.render(BoardParser.parse("boards/testTriangleBumperWithOrientation.fb"));  //check to see there is just one triangle bumper oriented
            
            //tests a board with an action trigger
            Flingball.render(BoardParser.parse("boards/testActionTrigger.fb")); //check to see there is an action trigger
            
            //tests a board with many action triggers
            Flingball.render(BoardParser.parse("boards/testBallGadgetsTriggers.fb")); //check to see there are many action triggers
            
            //tests a board with a combination of gadgets
            Flingball.render(BoardParser.parse("boards/absorber.fb")); //check to see the combination of different bumpers
    }
}
