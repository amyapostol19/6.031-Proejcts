package flingball;

import java.io.IOException;

public class FlingballServerTest {

	/*
	 * Testing Strategy: 
	 * 		(MANUAL TESTS) Console input: input is valid/invalid
	 * 			--valid: 
	 * 				starts with h and has 3 tokens
	 * 				starts with v and has 3 tokens
	 * 				has 3 tokens and any amount of whitespace
	 * 			--invalid:
	 * 				input doesn't start with h or v
	 * 				first token has multiple letters
	 * 				more than 3 tokens
	 * 				names in tokens are not current boards
	 * 
	 * 		(MANUAL TEST): test Flingball runs locally if no host is given
	 * 
	 * 		(MANUAL TEST): test FlingballServer no port is given
	 * 
	 * 		(MANUAL TEST):
	 * 		Test Balls travel through permeable walls to other boards using the FlingballServer
	 * 			vertical walls join
	 * 			horizontal walls join
	 * 
	 * 		(MANUAL TEST):
	 * 		Test Balls travel through interconnected portals using the FlingballServer
	 * 			Number of portals: 2, >2
	 * 
	 */
	
	/**
	 * Runs all the test cases as a Java Application
	 * 		Tests not currently being looked at by user are commented out
	 * @param args (empty array)
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		//List of test cases
		
		//testLeftRightServer();
		//testTopBottomServer();
		testPortaltoPortalServer();
		//testBoardConnectsToItself();
		//test if walls are re-added when a board disconnects -- WORKS CORRECTLY
		//testWallNewJoin();
		//testThreeLinkedPortals();
		//testFlingballNoHost();
		//testFlingballServerNoPort();
	}
	
	//covers locally run flingball and default board
	public static void testFlingballNoHost() throws IOException {
		String [] flingArgs = {};
		Flingball.main(flingArgs);
	}
	
	//covers test FlingballServer with no given port
	public static void testFlingballServerNoPort() throws IOException {
		String[] serverArgs = {};
		FlingballServer.main(serverArgs);
	}
	
	//covers test balls traveling horizontal through permeable walls
	public static void testLeftRightServer() throws IOException {
		String[] serverArgs = {"FlingballServer", "10987"};
		FlingballServer.main(serverArgs);
		
		String[] rightArgs = {"Flingball", "localhost", "10987", "boards/rightBoard.fb"};
		Flingball.main(rightArgs);
		
		String[] leftArgs = {"Flingball", "localhost", "10987", "boards/leftBoard.fb"};
		Flingball.main(leftArgs);
	}
	
	//covers test balls traveling vertically through permeable walls
	public static void testTopBottomServer() throws IOException {
		String[] serverArgs = {"FlingballServer", "10987"};
		FlingballServer.main(serverArgs);
		
		String[] topArgs = {"Flingball", "localhost", "10987", "boards/top.fb"};
		Flingball.main(topArgs);
		
		String[] bottomArgs = {"Flingball", "localhost", "10987", "boards/bottom.fb"};
		Flingball.main(bottomArgs);
	}
	
	//test balls traveling through two interconnected portals over FlingballServer
	public static void testPortaltoPortalServer() throws IOException {
		String[] serverArgs = {"FlingballServer", "10987"};
		FlingballServer.main(serverArgs);
		
		String[] bArgs = {"Flingball", "localhost", "10987", "boards/boardB.fb"};
		Flingball.main(bArgs);
		
		String[] aArgs = {"Flingball", "localhost", "10987", "boards/boardA.fb"};
		Flingball.main(aArgs);
	}
	
	//test balls traveling through three interconnected portals over FlingballServer
	public static void testThreeLinkedPortals() throws IOException {
		String[] serverArgs = {"FlingballServer", "10987"};
		FlingballServer.main(serverArgs);
		
		String[] twoArgs = {"Flingball", "localhost", "10987", "boards/threeLinks2.fb"};
		Flingball.main(twoArgs);
		
		String[] threeArgs = {"Flingball", "localhost", "10987", "boards/threeLinks3.fb"};
		Flingball.main(threeArgs);
		
		String[] oneArgs = {"Flingball", "localhost", "10987", "boards/threeLinks1.fb"};
		Flingball.main(oneArgs);
	}
	
	//covers Board joining the walls of itself so ball travels in a "torus" shape
	public static void testBoardConnectsToItself() throws IOException {
		String[] serverArgs = {"FlingballServer", "10987"};
		FlingballServer.main(serverArgs);
		
		String[] leftArgs = {"Flingball", "localhost", "10987", "boards/leftBoard.fb"};
		Flingball.main(leftArgs);
	}
	
	//tests if two walls are already joined and a third wall tries to replace one of them
	// expecting correct (specified) behavior
	public static void testWallNewJoin() throws IOException {
		/*
		 * Tests if you have two join walls left : right and try to replace
		 * right as a joined wall so you have left : third, then left and third
		 * pass all their balls to one another and right keeps all of its balls
		 * (the left wall of right closes)
		 */
		
		String[] serverArgs = {"FlingballServer", "10987"};
		FlingballServer.main(serverArgs);
		
		String[] rightArgs = {"Flingball", "localhost", "10987", "boards/rightBoard.fb"};
		Flingball.main(rightArgs);
		
		String[] leftArgs = {"Flingball", "localhost", "10987", "boards/leftBoard.fb"};
		Flingball.main(leftArgs);
		
		String[] squareArgs = {"Flingball", "localhost", "10987", "boards/third.fb"};
		Flingball.main(squareArgs);
	}

}
