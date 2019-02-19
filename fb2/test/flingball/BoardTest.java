package flingball;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

public class BoardTest {
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /*Testing Strategy
     * 	Creator Method
     * 		Partitions: 0 added objects (gadgets, balls), >0 added Objects(gadgets, balls)
     * 	render
     * 	simulate
     * 	resolve Collision
     * 	
    	addGadget: Partition into a board initially with no gadgets, a board with some gadgets on it initially
    	addBall: Partition into a board initially with no balls, a board with at least one ball initially
    	removeGadget:
    	removeBall:
    	addTrigger
    	getName: Check if correct board name
    	getSides: Check if correct sides
    	getGravity: Check if correct gravity
    	getFriction1: Check if correct friction 1
    	getFriction2: Check if correct friction 2
    	getReflectionCoeff: check if correct reflection coefficient
    	getGadgetsNames: partition into a board with no gadgets, a board with one gadget, a board with more than one gadget
    	getBallsNames: partition into a ball with no balls, a board with one ball, a board with more than one ball
    	sameValue
    	toString
    */
    
    private static Board createBoard(String name, double gravity, double friction1, double friction2) {
    	try {
			return new Board(name, gravity, friction1, friction2);
		} catch (IOException e) {
			e.printStackTrace();
			throw new Error(e);
		}
    }
    
    private static final Board BOARD1 = createBoard("Board1", 10, 1, 2); 
    private static final Gadget ABSORBER = new Absorber("Absorber",1,1,1,1);
    private static final Gadget CIRCLEBUMPER = new CircleBumper("CircleBumper",5,5);
    private static final Gadget SQUAREBUMPER = new SquareBumper("SquareBumper",2,2);
    private static final Ball BALL1 = new Ball("Ball1",9,4,10,100);
    private static final Ball BALL2 = new Ball("Ball2",5,3,20,10);
    
    //covers that Board creates an actual board object with given parameters
    @Test
    public void testCreateBoard() {
    	assertTrue(BOARD1 instanceof Board);
    	assertEquals("Board <Board1> with gravity=10.000000, friction1=1.000000, friction2=2.000000,"
    			+ " and reflectionCoeff=1.000000\nWith 0 gadgets\nWith 0 balls", BOARD1.toString());
    }
    
    //covers test of addGadget of a board initially with no gadgets, also covers test of getGadgets of a board with one gadget
    @Test
    public void testAddGadgetEmptyBoard() throws IOException {
        final Board example = new Board("Board",10,1,1);
        example.addGadget(ABSORBER);
        assertEquals("Expected correct gadgets",new HashSet<String>(Arrays.asList("Absorber")),example.getGadgetsNames());      

    }
    
    //covers test of addGadget of a board initially with some gadgets, also covers test of getGadgets of a board with more than one gadget
    @Test
    public void testAddGadgetNonEmptyBoard() throws IOException {
        final Board example = new Board("Board",10,1,1);
        example.addGadget(ABSORBER);
        example.addGadget(CIRCLEBUMPER);
        example.addGadget(SQUAREBUMPER);
        assertEquals("Expected correct gadgets",new HashSet<String>(Arrays.asList("Absorber","CircleBumper","SquareBumper")),example.getGadgetsNames());      
    }
    
    //covers test of addBall of a board initially with no balls, also covers test of getBalls of a board with one ball
    @Test
    public void testAddBallEmptyBoard() throws IOException {
        final Board example = new Board("Board",10,1,1);
        example.addBall(BALL1);
        assertEquals("Expected correct balls",new HashSet<String>(Arrays.asList("Ball1")),example.getBallsNames());      
    }
    
    //covers test of addBall of a board initially with some balls, also covers test of getBalls of a board with more than one ball
    @Test
    public void testAddBallNonEmptyBoard() throws IOException {
        final Board example = new Board("Board",10,1,1);
        example.addBall(BALL1);
        example.addBall(BALL2);
        assertEquals("Expected correct balls",new HashSet<String>(Arrays.asList("Ball1","Ball2")),example.getBallsNames());      
    }
    
    //covers test of getName
    @Test
    public void testGetName() {
        assertEquals("Expected correct board name","Board1",BOARD1.getName());
    }
    
    //covers test of getGravity
    @Test
    public void testGetGravity() {
        assertEquals("Expeceted correct gravity",10.0,BOARD1.getGravity(),0.0001);
    }
    
    //covers test of getFriction1
    @Test
    public void testGetFriction1() {
        assertEquals("Expectec correct friction 1",1.0,BOARD1.getFriction1(),0.0001);
    }
    
    //covers test of getFriction2
    @Test
    public void testGetFriction2() {
        assertEquals("Expectec correct friction 2",2.0,BOARD1.getFriction2(),0.0001);
    }
    
    //covers test of getReflectionCoeff
    @Test
    public void testGetReflectionCoefficient() {
        assertEquals("Expected default reflection coefficient of 1.0",1.0,BOARD1.getReflectionCoeff(),0.0001);
    }
    
}
