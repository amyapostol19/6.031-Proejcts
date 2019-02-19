package flingball;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;

import edu.mit.eecs.parserlib.UnableToParseException;
import physics.Angle;
import physics.Circle;
import physics.Vect;


public class FlingballParserTest {
    
    //FlingballParser should take in a file and read the contents of that file to create
    //a board object
    /*Partitions for Flingball ParserTest
     * Parser should be able to handle all of the following lines in file
     *  boardObject
     *  ball
     *  squareBumper
     *  circleBumper
     *  triangleBumper
     *  absorber
     *  fire
     *  
     *  comments
     * 
     */

    //Tests for parser
    
    //covers empty file --> should throw an error
    @Test(expected=EmptyFileException.class)
    public void testParserEmptyFile() throws UnableToParseException, IOException, EmptyFileException{
        Board board = FlingballParser.parse("boards/emptyFile.fb");
        assertTrue(board instanceof Board);
    }
    
    //covers only board in the file --> should create an empty board
    @Test
    public void testParserOnlyBoard() throws UnableToParseException, IOException, EmptyFileException {
        Board board = FlingballParser.parse("boards/emptyBoard.fb");
        
        Board correct = new Board("emptyBoard", new ArrayList<Ball>(), new ArrayList<Gadget>(), 15.0, 
                new ArrayList<Double>(Arrays.asList(1.0, 1.0)), new HashMap<Gadget, Gadget>());
        
        assertTrue(correct.equals(board));
    }
    
    //covers board and ball in the file (with spaces and comments)
    @Test
    public void testParserAbsorberFB() throws UnableToParseException, IOException, EmptyFileException{
        Board board = FlingballParser.parse("boards/boardAndBall.fb");
        
        Ball singleBall = new Ball(new Circle(new Vect(10.25, 15.25), 4), new Vect(0, 0), "name");
        Board correct = new Board("New", new ArrayList<Ball>(Arrays.asList(singleBall)), new ArrayList<Gadget>(), 15.0,
                new ArrayList<Double>(Arrays.asList(1.0, 1.0)), new HashMap<Gadget, Gadget>());
        
        assertTrue(correct.equals(board));
    }
    
    //covers board without inputed friction and balls and bumpers in file
    @Test
    public void testParserBoardBallBumper() throws UnableToParseException, IOException, EmptyFileException{
        Board board = FlingballParser.parse("boards/default.fb");
        
        Ball ball = new Ball(new Circle(new Vect(1.25, 1.25), 4), new Vect(0, 0), "BallA");
        
        SquareBumper bumper1 = new SquareBumper(board, 20, new Vect(0, 20*17), Angle.ZERO, "SquareA");
        SquareBumper bumper2 = new SquareBumper(board, 20, new Vect(1*20, 17*20), Angle.ZERO, "SquareB");
        SquareBumper bumper3 = new SquareBumper(board, 20, new Vect(2*20, 17*20), Angle.ZERO, "SquareC");
        
        CircleBumper cbumper1 = new CircleBumper(board, new Circle(new Vect(1*20 +.5*20, 10*20 + .5*20), 10), "CircleA");
        CircleBumper cbumper2 = new CircleBumper(board, new Circle(new Vect(7*20 +.5*20, 18*20 + .5*20), 10), "CircleB");
        CircleBumper cbumper3 = new CircleBumper(board, new Circle(new Vect(8*20 +.5*20, 18*20 + .5*20), 10), "CircleC");
        CircleBumper cbumper4 = new CircleBumper(board, new Circle(new Vect(9*20 +.5*20, 18*20 + .5*20), 10), "CircleD");
        
        TriangleBumper tbumper = new TriangleBumper(board, 20, new Vect(12*20, 15*20), new Angle(180), "Tri");
        
        Board correct = new Board("Default", new ArrayList<Ball>(Arrays.asList(ball)), new ArrayList<Gadget>(Arrays.asList(bumper1, bumper2, bumper3, cbumper1,
                cbumper2, cbumper3, cbumper4, tbumper)), 25.0, new HashMap<Gadget, Gadget>());  
        
        assertTrue(correct.equals(board));
    }
    
    //covers board absorbers and fires
    @Test
    public void testParserAsorbersAndFires() throws UnableToParseException, IOException, EmptyFileException{
        Board board = FlingballParser.parse("boards/absorber.fb");
        
        assertTrue(board.getBalls().size() == 3);
        assertTrue(board.getGadgets().size() == 14);
        assertTrue(board.getTriggers().size() == 6);
    }
    
}
