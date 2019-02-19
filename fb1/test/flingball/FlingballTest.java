package flingball;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.mit.eecs.parserlib.UnableToParseException;
import physics.Angle;
import physics.Circle;
import physics.Vect;

public class FlingballTest {

    private static final double PIXELS_PER_L = 20.0;
    
    /**
     * Testing Strategy
     * 
     * Test the GUI with 
     *      Gadgets
     *      Balls
     *      Gravity
     *          Set and default
     *      friction
     *          Set and default
     *          
     * Note: There are no assertion statements for the GUI
     *       At the bottom of every test is a commented 
     *       line that will run the GUI when uncommented.
     *       Only have one uncommented at a time or the 
     *       first one chronologically will be the only one
     *       to execute.
     */
    
    @Test
    public void testGUIOnlyOneBall() {

        Circle startBall = new Circle(new Vect(20, 20), PIXELS_PER_L);

        Vect startBallVelocity = new Vect(-PIXELS_PER_L/3, PIXELS_PER_L/2);

        Ball ball = new Ball(startBall, startBallVelocity, "ballA");
        
        List<Ball> ballList = new ArrayList<Ball>(Arrays.asList(ball));
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Map<Gadget, Gadget> triggers = new HashMap<Gadget, Gadget>();

        Board board = new Board("bd", ballList, gadgets, 25.0, triggers);
      
        
//        Flingball.animationApproach(board);
        }
    
    @Test
    public void testGUIOnlyTwoBalls() {
        Circle startBall1 = new Circle(new Vect(3*PIXELS_PER_L, 3*PIXELS_PER_L), PIXELS_PER_L);
        Vect startBallVelocity1 = new Vect(PIXELS_PER_L, PIXELS_PER_L/2);
        Circle startBall2 = new Circle(new Vect(10*PIXELS_PER_L, 10*PIXELS_PER_L), PIXELS_PER_L);
        Vect startBallVelocity2 = new Vect(-PIXELS_PER_L, PIXELS_PER_L/2);
        Ball ball1 = new Ball(startBall1, startBallVelocity1, "ballA");
        Ball ball2 = new Ball(startBall2, startBallVelocity2, "ballB");
        
        List<Ball> ballList = new ArrayList<Ball>(Arrays.asList(ball2, ball1));
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Map<Gadget, Gadget> triggers = new HashMap<Gadget, Gadget>();

        Board board = new Board("bd", ballList, gadgets, 25.0, triggers);
        
//        Flingball.animationApproach(board);
    }
    
    @Test
    public void testGUIOneBallWithSquareBumper() {
        Circle startBall1 = new Circle(new Vect(3*PIXELS_PER_L, 3*PIXELS_PER_L), PIXELS_PER_L);
        Vect startBallVelocity1 = new Vect(PIXELS_PER_L/3, PIXELS_PER_L/2);
        Ball ball1 = new Ball(startBall1, startBallVelocity1, "ballA");
        
        List<Ball> ballList = new ArrayList<Ball>(Arrays.asList(ball1));
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Map<Gadget, Gadget> triggers = new HashMap<Gadget, Gadget>();
        

        Board board = new Board("bd", ballList, gadgets, 25.0, triggers);
        Vect location = new Vect(200, 200);
        
        SquareBumper sb1 = new SquareBumper(board, 100, location, new Angle(0), "SB1");
        board.addGadget(sb1);
       
        
//        Flingball.animationApproach(board);
    }
    
    @Test
    public void testGUIOneBallWithCircleBumper() {
        Circle startBall1 = new Circle(new Vect(3*PIXELS_PER_L, 3*PIXELS_PER_L), PIXELS_PER_L);
        Vect startBallVelocity1 = new Vect(PIXELS_PER_L/10, PIXELS_PER_L/10);
        Ball ball1 = new Ball(startBall1, startBallVelocity1, "ballA");
        
        List<Ball> ballList = new ArrayList<Ball>(Arrays.asList(ball1));
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Map<Gadget, Gadget> triggers = new HashMap<Gadget, Gadget>();
        
        

        Board board = new Board("bd", ballList, gadgets, 25.0, triggers);
        CircleBumper cb1 = new CircleBumper(board,new Circle(new Vect(200, 250), PIXELS_PER_L), "");
        
        board.addGadget(cb1);
        
//        Flingball.animationApproach(board);
    }
    
    @Test
    public void testGUIOneBallWithTriangleBumper() {
        Circle startBall1 = new Circle(new Vect(3*PIXELS_PER_L, 3*PIXELS_PER_L), PIXELS_PER_L);
        Vect startBallVelocity1 = new Vect(PIXELS_PER_L/2, PIXELS_PER_L/3);
        Ball ball1 = new Ball(startBall1, startBallVelocity1, "ballA");
        
        List<Ball> ballList = new ArrayList<Ball>(Arrays.asList(ball1));
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Map<Gadget, Gadget> triggers = new HashMap<Gadget, Gadget>();

        Board board = new Board("bd", ballList, gadgets, 25.0, triggers);
        
        Vect location = new Vect(140, 140);        
        TriangleBumper tb1 = new TriangleBumper(board, 100, location, new Angle(180), "");

        board.addGadget(tb1);
       
        
//        Flingball.animationApproach(board);
    }
    
    @Test
    public void testGUIOneBallWithMultipleBumper() {
        Circle startBall1 = new Circle(new Vect(3*PIXELS_PER_L, 3*PIXELS_PER_L), PIXELS_PER_L);
        Vect startBallVelocity1 = new Vect(PIXELS_PER_L/2, PIXELS_PER_L/3);
        Ball ball1 = new Ball(startBall1, startBallVelocity1, "ballA");
        
        List<Ball> ballList = new ArrayList<Ball>(Arrays.asList(ball1));
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Map<Gadget, Gadget> triggers = new HashMap<Gadget, Gadget>();

        Board board = new Board("bd", ballList, gadgets, 25.0, triggers);
        
        Vect locationtb1 = new Vect(100, 100);        
        TriangleBumper tb1 = new TriangleBumper(board, 40, locationtb1, new Angle(180), "");
        
        CircleBumper cb1 = new CircleBumper(board,new Circle(new Vect(300, 250), PIXELS_PER_L), "");
        
        Vect locationsb1 = new Vect(200, 200);
        SquareBumper sb1 = new SquareBumper(board, 50, locationsb1, new Angle(0), "SB1");
        
        board.addGadget(tb1);
        board.addGadget(sb1);
        board.addGadget(cb1);
   
        
//        Flingball.animationApproach(board);
    }
    
    @Test
    public void testGUIMultipleBallWithMultipleBumper() {
        Circle startBall1 = new Circle(new Vect(3*PIXELS_PER_L, 3*PIXELS_PER_L), PIXELS_PER_L/4);
        Vect startBallVelocity1 = new Vect(PIXELS_PER_L/3, PIXELS_PER_L/3);
        Ball ball1 = new Ball(startBall1, startBallVelocity1, "ballA");
        
        Circle startBall2 = new Circle(new Vect(3*PIXELS_PER_L, PIXELS_PER_L), PIXELS_PER_L/4);
        Vect startBallVelocity2 = new Vect(PIXELS_PER_L, PIXELS_PER_L/3);
        Ball ball2 = new Ball(startBall2, startBallVelocity2, "ballB");
        
        Circle startBall3 = new Circle(new Vect(7*PIXELS_PER_L, 3*PIXELS_PER_L), PIXELS_PER_L/4);
        Vect startBallVelocity3 = new Vect(PIXELS_PER_L/2, PIXELS_PER_L/3);
        Ball ball3 = new Ball(startBall3, startBallVelocity3, "ballC");
        
        List<Ball> ballList = new ArrayList<Ball>(Arrays.asList(ball1, ball2, ball3));
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Map<Gadget, Gadget> triggers = new HashMap<Gadget, Gadget>();

        Board board = new Board("bd", ballList, gadgets, 50.0, triggers);
        
        Vect locationtb1 = new Vect(100, 100);        
        TriangleBumper tb1 = new TriangleBumper(board, 40, locationtb1, new Angle(0), "");
        
        CircleBumper cb1 = new CircleBumper(board,new Circle(new Vect(300, 250), PIXELS_PER_L), "");
        
        Vect locationsb1 = new Vect(200, 200);
        SquareBumper sb1 = new SquareBumper(board, 50, locationsb1, new Angle(0), "SB1");
        
        board.addGadget(tb1);
        board.addGadget(sb1);
        board.addGadget(cb1);
  
        
//        Flingball.animationApproach(board);
    }
    
    @Test
    public void testParserDefault() throws UnableToParseException, IOException, Exception {
        Board board = FlingballParser.parse("boards/default.fb");
//        Flingball.animationApproach(board);

    }
    
    @Test
    public void testParserAbsorber() throws UnableToParseException, IOException, Exception {
        Board board = FlingballParser.parse("boards/absorber.fb");
//        Flingball.animationApproach(board);

    }
    
    @Test
    public void testParserAbsorber2() throws UnableToParseException, IOException, Exception {
        Board board = FlingballParser.parse("boards/absorber2.fb");
//        Flingball.animationApproach(board);

    }
    
    @Test
    public void testParserAbsorber3() throws UnableToParseException, IOException, Exception {
        Board board = FlingballParser.parse("boards/absorber3.fb");
//        Flingball.animationApproach(board);

    }
    
    @Test
    public void testParserFriction() throws UnableToParseException, IOException, Exception {
        Board board = FlingballParser.parse("boards/friction.fb");
//        Flingball.animationApproach(board);

    }
    
    

}
