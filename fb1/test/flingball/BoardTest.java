package flingball;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import edu.mit.eecs.parserlib.UnableToParseException;
import physics.Angle;
import physics.Circle;
import physics.Vect;

    /**
     * Testing Strategy (Partitions)
     * 
     * Create a board with the three constructors
     * 
     * Number of balls correct
     * 
     * Number of walls correct
     * 
     * Number of gadgets correct
     * Can add gadget
     * Can remove gadget
     * 
     * Correct Triggers
     * 
     * Gravity
     * 
     * Friction
     * 
     * Equals
     * 
     * HashCode
     * 
     * ToString
     *
     */


public class BoardTest {

    //covers creating a board with first constructor
    @Test
    public void testCreateBoardDefaultFriction() {
        Board board = null;
        
        Ball ball = new Ball(new Circle(new Vect(1.25, 1.25), 4), new Vect(0, 0), "BallA");
        SquareBumper bumper1 = new SquareBumper(board, 20, new Vect(0, 20*17), Angle.ZERO, "SquareA");
        
        List<Ball> ballList = new ArrayList<Ball>(Arrays.asList(ball));
        List<Gadget> gadgetList = new ArrayList<Gadget>(Arrays.asList(bumper1));
        
        
        board = new Board("DefaultFriction", ballList, gadgetList, 25.0, new HashMap<Gadget, Gadget>());
        assertTrue(board.getBalls().contains(ball));
        assertTrue(board.getGadgets().contains(bumper1));
        assertTrue(board.getGadgets().size() == 5);
        assertTrue(board.getFriction().contains(0.025));
    }
    
    
    @Test
    public void testNumBallsDefault() throws UnableToParseException, IOException, Exception {
        Board board = FlingballParser.parse("boards/default.fb");        
        assertTrue(board.getBalls().size() == 1);

    }
    
    @Test
    public void testNumBallsAbsorber() throws UnableToParseException, IOException, Exception {
        Board board = FlingballParser.parse("boards/absorber.fb");        
        assertTrue(board.getBalls().size() == 3);

    }
    
    @Test
    public void testNumGadgets() throws UnableToParseException, IOException, Exception {
        Board board = FlingballParser.parse("boards/absorber.fb");

        System.out.println("===" + board.getGadgets().size());
        
        assertTrue(board.getGadgets().size() == 14);

    }

}
