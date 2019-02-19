package flingball;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.NoSuchElementException;

import physics.Vect;

/** make sure grammars match in boardClient and Flingball server
 * Mutable class
 * Client for each board that sends requests to the FlingballServer 
 * 		and interprets its replies
 * A new client is open until it is called closed
 * 
 * Sends requests of the following two forms:
 *   "ballThroughWall" " newBoardName="NAME " wallOrientation="('Left'|'Right'|'Top'|'Bottom') " name="BALLNAME " x="(FLOAT|INTEGER) " y="(FLOAT|INTEGER)
    			" xVelocity="(FLOAT|INTEGER) " yVelocity="(FLOAT|INTEGER)
 *   "portalExiting" " newBoardName="NAME " newPortalName="NAME " ball="NAME " xVelocity="(FLOAT|INTEGER) " yVelocity="(FLOAT|INTEGER)
 *   
 *   where NAME ::= [A-Za-z_][A-Za-z_0-9]*;
 *         INTEGER ::= [0-9]+;
 *         FLOAT ::= '-'?([0-9]+.[0-9]*|.?[0-9]+);
 *         
 * Accepts a reply of one of the following five forms:
 *   "ballEntering" " name="BALLNAME " x="(FLOAT|INTEGER) " y="(FLOAT|INTEGER) " xVelocity="(FLOAT|INTEGER) " yVelocity="(FLOAT|INTEGER)
 *   "enteringPortal" " portalName="NEWPORTALNAME " ballName="BALLNAME " xVelocity="(FLOAT|INTEGER) " yVelocity="(FLOAT|INTEGER)
 *   'removeWall ' 'Add='BOARDNAME ' toWall='('Right'|'Left'|'Top'|'Bottom');
 *   'addWall ' 'side='('Right'|'Left'|'Top'|'Bottom');
 *   "success. remove ball name="BALLNAME;
 */
public class BoardClient {
	
	//fields
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private Board board;
	
	/*
	 * Abstraction Function:
	 * 		AF(socket, in, out, board): Connects to a board and passes messages of the board state to the server so that the board
	 * 				can interact with other boards connected to the server.
	 * 			socket: method of connection to the server (how we connect)
	 * 			in: reads messages from FlingballServer
	 * 			out: sends messages to FlingballServer
	 * 			board: the board BoardClient reads from and sends messages to FlingballServer based on the state of the board.
	 * 
	 * Rep Invariant:
	 * 		socket != null
	 * 		in != null
	 * 		out != null
	 * 		board != null
	 * 
	 * Safety from RepExposure
	 * 		All fields are private
	 * 		socket, in, out are not mutated from outside the class
	 * 
	 * Thread Safety Argument
	 * 		The board class is not threadsafe so this class is not completely threadsafe
	 * 		Other than the board field, all other fields cannot be accessed outside this
	 * 			class and because multiple threads don't run in this class (confinement)
	 * 			most of BoardClient is threadsafe.
	 */
	
    /**
     * Make a SquareClient and connect it to a server running on
     * hostname at the specified port.
     * @throws IOException if can't connect
     */
    public BoardClient(String hostname, int port, Board board) throws IOException {
        socket = new Socket(hostname, port); //hostname examples: localhost, google.com, etc
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        
        //write board to input stream
        out.println(board.getName());
        out.flush();
        
        //write portals to input stream
        List<String> portals = board.getPortalNames();
        if (!portals.isEmpty()) {
	    	String sendString = "";
	    	for (String portal : portals) {
	    		sendString += portal + " ";
	    	}
	    	out.println("sendingPortals " + sendString.substring(0, sendString.length()-1));
	    	out.flush();
        } else {
        	out.println("noPortals");
        	out.flush();
        }
        
        this.board = board;
        checkRep();
    }
    
    private void checkRep() {
    	assert socket != null;
    	assert in != null;
    	assert out != null;
    	assert board != null;
    }
    
    /**
     * 
     * @return the socket in the socket field
     */
    public Socket getSocket() {
    	return socket;
    }
    
    /**
     * Send a request to the server to add a ball.
     * Normally occurs when ball is moving to a different board because there are no walls 
     * Requires this is "open".
     * @param ball that is moving to a different board
     * @param wall : the wall the ball is moving through to get to the next board
     * @throws IOException if network or server failure
     */
    public void sendRequest(Ball b, Wall w) {
    	System.err.println("Sending request to server; ball moving");
    	
    	out.println("ballThroughWall newBoardName="+w.getNeighboringBoard() + " wallOrientation=" + w.getOrientation() + 
    			" name=" + b.getName() + " x=" + b.getPosition().x() + " y=" +b.getPosition().y() + 
    			" xVelocity=" + b.getVelocity().x() + " yVelocity=" + b.getVelocity().y());
    	
    	out.flush();
    	
    }
    
    /**
     * Send a request to the server to move a ball between portals on different boards.
     * Requires this is "open".
     * @param b ball that is moving to different portal
     * @param newPortal portal that the ball is moving to
     * @throws IOException if network or server failure
     */
    public void sendRequest(Ball b, String newPortal, String newBoard) {
    	System.err.println("Sending request to server");
    	
    	out.println("portalExiting newBoardName=" + newBoard + " newPortalName=" + newPortal + " ball=" + b.getName() + 
    			" xVelocity=" + b.getVelocity().x() + " yVelocity=" + b.getVelocity().y());
    	
    	out.flush();
    }

	
    /**
     * Get a reply from the next request that was submitted.
     * Requires this is "open".
     * Responds appropriately to the reply depending on what type of reply it is.
     * @throws IOException if network or server failure
     */
    public void getReply() throws IOException {
    	/*
    	 * There are many different replies this client can get:
    	 *  -A new ball is entering from a portal
    	 *  -A new ball is entering from a removed wall
    	 *  -Add newBoard, remove a wall from oldBoard
    	 *  -Add a wall to the board
    	 *  -Success: the ball moved, remove ball
    	 */
    	System.out.println("getting reply");
    	
    	
        String reply = in.readLine();
        if (reply == null) {
            throw new IOException("connection terminated unexpectedly");
        } 
        //code to parse reply and figure out what to do with it
        
        
        System.out.println(reply);
        
        
        if (reply.startsWith("ballEntering")) {
        	String[] information = reply.split(" ");
        	String name = information[1].substring(5);
        	double xPosition = Double.parseDouble(information[2].substring(2));
        	double yPosition = Double.parseDouble(information[3].substring(2));
        	double xVelocity = Double.parseDouble(information[4].substring(10));
        	double yVelocity = Double.parseDouble(information[5].substring(10));
        	
        	Ball newBall = new Ball(name, xPosition, yPosition, xVelocity, yVelocity);
        	
        	board.addBall(newBall);
        	
        	
        } else if (reply.contains("enteringPortal")) {
        	String[] information = reply.split(" ");
        	String portalName = information[1].substring(11);
        	String ballName = information[2].substring(9);
        	double xVelocity = Double.parseDouble(information[3].substring(10));
        	double yVelocity = Double.parseDouble(information[4].substring(10));
        	Portal destinationPortal = (Portal) board.getGadgetByName(portalName);
        	Vect portalPosition = destinationPortal.getPosition();
        	
        	Ball newBall = new Ball(ballName, portalPosition.x(), portalPosition.y(),
        			xVelocity, yVelocity);
        	newBall.setExitingPortal(true);
        	destinationPortal.addBall(newBall);
        	board.addBall(newBall);
        	
        } else if (reply.startsWith("removeWall")) {
        	String[] information = reply.split(" ");
        	String newBoard = information[1].substring(4);
        	String side = information[2].substring(7);
        	
        	if (board.getWall(side).hasNeighboringBoard()) {
        		String neighbor = board.getWall(side).getNeighboringBoard();
        		String newSide = null;
        		if (side.equals("Top")) newSide = "Bottom";
        		else if (side.equals("Bottom")) newSide = "Top";
        		else if (side.equals("Left")) newSide = "Right";
        		else if (side.equals("Right")) newSide = "Left";
        		
        		out.println("addWall board=" + neighbor + " side=" + newSide);
        		out.flush();	
        	}
        	
        	//System.out.println(newBoard + "  " + side);
        	//System.out.println(board.getWalls());
        	
        	//System.out.println(board.getWall("Left"));
        	
        	board.getWall(side).addNeighboringBoard(newBoard);
        	board.getWall(side).removeWall();
        	
        } else if (reply.contains("addWall")) {
        	//System.out.println("re adding the wall");
        	
        	//when a board disconnects re-add the wall
        	String[] information = reply.split(" ");
        	String wallOrientation = information[1].substring(5);
        	
        	for (Wall wall : board.getWalls()) {
        		if (wall.getOrientation().equals(wallOrientation)) {
        			wall.addWall();
        			wall.removeNeighboringBoard();
        		}
        	}
        }
        
        else if (reply.contains("success")) {
        	String[] information = reply.split(" ");
        	String ballName = information[3].substring(5);
        	board.removeBall(ballName);
        }
        
        else if (reply.contains("portal exit fail")) {
        	System.out.println("exit failed");
        }
    }
    /**
     * 
     * @return the board attached to this client
     */
    public Board getBoard() {
        return this.board;
    }
    /**
     * Closes the client's connection to the server.
     * This client is now "closed". Requires this is "open".
     * @throws IOException if close fails
     */
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
