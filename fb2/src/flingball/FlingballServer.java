package flingball;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


/**
 * Mutable class
 * 
 * Server to host a network of Flingball games
 * 
 *  Accepts requests of the following two forms:
 *   "ballThroughWall" " newBoardName="NAME " wallOrientation="('Left'|'Right'|'Top'|'Bottom') " name="BALLNAME " x="(FLOAT|INTEGER) " y="(FLOAT|INTEGER)
    			" xVelocity="(FLOAT|INTEGER) " yVelocity="(FLOAT|INTEGER)
 *   "portalExiting" " newBoardName="NAME " newPortalName="NAME " ball="NAME " xVelocity="(FLOAT|INTEGER) " yVelocity="(FLOAT|INTEGER)
 *   
 *   where NAME ::= [A-Za-z_][A-Za-z_0-9]*;
 *         INTEGER ::= [0-9]+;
 *         FLOAT ::= '-'?([0-9]+.[0-9]*|.?[0-9]+);
 *         
 *  And for each request that warrants a reply, returns a reply of one of the following five forms (possibly to a different board):
 *   "ballEntering" " name="BALLNAME " x="(FLOAT|INTEGER) " y="(FLOAT|INTEGER) " xVelocity="(FLOAT|INTEGER) " yVelocity="(FLOAT|INTEGER)
 *   "enteringPortal" " portalName="NEWPORTALNAME " ballName="BALLNAME " xVelocity="(FLOAT|INTEGER) " yVelocity="(FLOAT|INTEGER)
 *   'removeWall ' 'Add='BOARDNAME ' toWall='('Right'|'Left'|'Top'|'Bottom');
 *   'addWall ' 'side='('Right'|'Left'|'Top'|'Bottom');
 *   "success. remove ball name="BALLNAME;
 * 
 *   
 *   
 */
public class FlingballServer {
    
    private static final int DEFAULT_PORT = 10987;
    
    private ServerSocket serverSocket;
    private Map<String, Socket> boards = new HashMap<String, Socket>();
    private Map<String, List<String>> portals = new HashMap<String, List<String>>();
    
    /*
     *  Abstraction Function:
     *   	AF(serverSocket, boards, portals): a server(cloud) which keeps tracks of all game connected to the server and sends messages
	 *   			between connected games to that games can interact with each other. 
	 *   				--serverSocket Flingball Server listens for messages from boards via socket--like FlingballServer's telephone. 
	 *   				--Boards are the boards that are currently connected to the FlingballServer
	 *   				--portals all the portals on each board currently connected to the server
	 *   
	 *   RepInvariant:
	 *   	serverSocket != null
	 *   	boards != null
	 *   	portals != null
	 *   
	 *   Safety From Rep Exposure
	 *   	-All fields are private
	 *   	-All other classes cannot mutate FlingballServer, they only send messages. FlingballServer mutates itself from within
	 *   
	 *   Thread Safety Argument:
	 *   	Class is threadsafe because no other class can mutate this class. Thus we do not have to worry about threads
	 *   	from other classes mutating FlingballServer.
	 *   	There are many threads running on this class which may mutate the class so FlingballServer is not completely threadsafe.
	 *   	This especially occurs as one client tries to disconnect and close its socket while another thread is simultaneously trying
	 *   		read from the socket if the wall is currently permeable.
	 *   		TODO fix the bug to make this class threadSafe
     */
    
    /**
     * Make a FlingballServer that listens for connections on port.
     * @param port port number, requires 0 <= port <= 65535
     * @throws IOException if there is an error listening on port
     */
    public FlingballServer(int port) throws IOException {
    	//System.out.println("creating new flingball server");
        serverSocket = new ServerSocket(port);
        checkRep();
    }    
    
    private void checkRep() {
    	assert serverSocket != null;
    	assert boards != null;
    	assert portals != null;
    }
    
    /**
     * Runs the server, listening for connections and handling them.
     * @throws IOException if the main server socket is broken
     */
    public void serve() throws IOException {
        while (true) {
            // block until a client connects
            final Socket socket = serverSocket.accept();
            System.out.println("accepted a socket " + socket);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            String name = in.readLine();
            boards.put(name, socket);
            
            //System.out.println("boards " + boards);
            
            String portalNames = in.readLine();
            if (!portalNames.equals("noPortals")) {
	        	String[] portalNames2 = portalNames.substring(15).split(" ");
	        	List<String> portalsToAdd = new ArrayList<>();
	        	for (String portal : portalNames2) {portalsToAdd.add(portal);}
	        	portals.put(name, portalsToAdd);
            }
        	//System.out.println("portals server " + portals);
        	
            
            
            // create a new thread to handle that client
            Thread handler = new Thread(new Runnable() {
                public void run() {
                	
                	//System.out.println("entered the thread and running");
                	
                	//create new board
                    try {
                        try {
                        	//System.out.println("Now trying to handle socket");
                            handle(socket);
                        } finally {
                        	System.out.println("socket is closing");
                        	//remove portals
                        	portals.remove(name);
                        	boards.get(name).close();
                        	boards.remove(name);
                        	
                        	System.out.println(boards + "  " + portals);
                        	
                            socket.close();
                        }
                    } catch (IOException ioe) {
                        // this exception wouldn't terminate serve(),
                        // since we're now on a different thread, but
                        // we still need to handle it
                    	//System.out.println("tried to read but socket is already closed");
                        //ioe.printStackTrace();
                    }
                }
            });
            // start the thread
            handler.start();
        }
    }
    
    /**
     * Handle one client connection. Returns when client disconnects.
     * @param socket socket where client is connected
     * @throws IOException if connection encounters an error
     */
    private void handle(Socket socket) throws IOException {
    	System.err.println("client connected");
    	
    	// get the socket's input stream, and wrap converters around it
        // that convert it from a byte stream to a character stream,
        // and that buffer it so that we can read a line at a time
    	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    	
    	//wait for request: like a ball hit a portal or removed wall or something
    	try {
            // each request is a single line containing a number
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                //System.err.println("request: " + line);
                try {
                    if (line.contains("ballThroughWall")) {
                    	
                    	//System.out.println("working on ball exiting");
                    	
                    	String[] information = line.split(" ");
                    	String newBoard = information[1].substring(13);
                    	String oldOrientation = information[2].substring(16);
                    	String ballName = information[3].substring(5);
                    	String xPosition = information[4].substring(2);
                    	String yPosition = information[5].substring(2);
                    	String xVelocity = information[6].substring(10);
                    	String yVelocity = information[7].substring(10);
                    	
                    	System.out.println("oldOrientation " + oldOrientation);
                    	
                    	if (oldOrientation.equals("Top")) {
                    		yPosition = "19.75";
                    	} else if (oldOrientation.equals("Bottom")) {
                    		yPosition = "0.25"; 
                    	} else if (oldOrientation.equals("Right")) {
                    		xPosition = "0.25";
                    	} else if (oldOrientation.equals("Left")) {
                    		xPosition = "19.75";
                    	}
                    	
                    	System.out.println("should try to remove ball from current board now");
                    	
                    	if (boards.containsKey(newBoard)) {
	                    	out.println("success. remove ball name=" + ballName);
	                    	
	                    	Socket newSocket = boards.get(newBoard);
	                    	PrintWriter outNewSocket = new PrintWriter(new OutputStreamWriter(newSocket.getOutputStream()), true);
	                    	
	                    	outNewSocket.println("ballEntering name=" + ballName + " x=" + xPosition + " y=" + yPosition + 
	                    			" xVelocity=" + xVelocity + " yVelocity=" +yVelocity);
	                    	
	                    	outNewSocket.flush();
                    	} else {
                    		//System.out.println("Board no longer exists");
                    		out.println("addWall side=" + oldOrientation);
                    	}
                    	
                    	
                    } else if (line.startsWith("portalExiting")) {
                    	
                    	//System.out.println("working on portal exiting");
                    	
                    	String[] information = line.split(" ");
                    	
                    	String newBoard = information[1].substring(13);
                    	String newPortal = information[2].substring(14);
                    	String ballName = information[3].substring(5);
                    	String xVelocity = information[4].substring(10);
                    	String yVelocity = information[5].substring(10);
                    	
                    	if (boards.containsKey(newBoard) && portals.get(newBoard).contains(newPortal)) {
                    		System.out.println("sending information to other board");
                        	
                        	Socket newSocket = boards.get(newBoard);
                        	System.out.println("socket and board " + newBoard + " " + newSocket);
                        	PrintWriter outNewSocket = new PrintWriter(new OutputStreamWriter(newSocket.getOutputStream()), true);
                        	
                        	
                        	
                        	
                        	outNewSocket.println("enteringPortal portalName=" + newPortal + " ballName=" + ballName +
                        			" xVelocity=" + xVelocity + " yVelocity=" + yVelocity);
                        	
                        	outNewSocket.flush();
                        	
                        	out.println("success. remove ball name=" + ballName);
                    	}
                    } else if (line.startsWith("addWall")) {
                    	String[] information = line.split(" ");
                    	
                    	String board = information[1].substring(6);
                    	String side = information[2].substring(5);
                    	
                    	if (boards.containsKey(board)) {
                    		Socket newSocket = boards.get(board);
                    		PrintWriter outNewSocket = new PrintWriter(new OutputStreamWriter(newSocket.getOutputStream()), true);
                    		outNewSocket.println("addWall side=" + side);
                    		outNewSocket.flush();
                    	}
                    	
                    }
                    
                    
                } catch (NumberFormatException nfe) {
                    // complain about ill-formatted request
                    System.err.println("reply: err");
                    out.println("err");
                }
                // important! flush our buffer so the reply is sent
                out.flush();
            }
        } finally {
            out.close();
            in.close();
        }
    }
    
    /**
     * Reads commands passed in through the console and sends messages to clients
     * 		based on input
     * @throws IOException
     */
    public void readCommands() throws IOException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("> ");
            final String input = in.readLine();
            
            if (input.isEmpty()) {
                System.exit(0); // exits the program
            }

            try {
            	String[] information = input.trim().split("[ \t]+");
            	
            	System.out.println("info " + information[0]);
            	
            	if (information.length != 3) {
            		throw new Error("Invalid console input");
            	} else if (!information[0].equals("h") && !information[0].equals("v")) {
            		throw new Error("Invalid console input");
            	} else if (!boards.containsKey(information[1]) || !boards.containsKey(information[2])) {
            		throw new Error("Board does not exist");
            	}
            	
                if (information[0].equals("h")) {
                	System.out.println(input);
                    
                    String leftBoard = information[1];
                    String rightBoard = information[2];
                    
                    if (boards.containsKey(leftBoard) && boards.containsKey(rightBoard)) {
                    
                    	System.out.println("have two valid boards");
	                    PrintWriter outLeftBoard = new PrintWriter(new OutputStreamWriter(boards.get(leftBoard).getOutputStream()), true);
	                    PrintWriter outRightBoard = new PrintWriter(new OutputStreamWriter(boards.get(rightBoard).getOutputStream()), true);
	                    
	                    //Send a message that board needs to remove wall and attach new board
	                    
	                    outLeftBoard.println("removeWall" + " Add=" + rightBoard + " toWall=Right");
	                    outRightBoard.println("removeWall" + " Add=" + leftBoard + " toWall=Left");
	                    
	                    outLeftBoard.flush();
	                    outRightBoard.flush();
	                    
	                    
	                	System.out.println("join two horizontal boards");
                    }
                } else if (information[0].equals("v")) {
                    
                	String topBoard = information[1];
                	String bottomBoard = information[2];
                	
                	PrintWriter outTopBoard = new PrintWriter(new OutputStreamWriter(boards.get(topBoard).getOutputStream()), true);
                	PrintWriter outBottomBoard = new PrintWriter(new OutputStreamWriter(boards.get(bottomBoard).getOutputStream()), true);
                	
                	//Send message that board needs to remove wall and attach new board
                	//when you remove a wall always send message that old wall is added back
                	outTopBoard.println("removeWall" + " Add=" + bottomBoard + " toWall=Bottom");
                	outBottomBoard.println("removeWall" + " Add=" + topBoard + " toWall=Top");
                	
                	outTopBoard.flush();
                	outBottomBoard.flush();
                	
                	System.out.println("join two vertical boards");
                } else {
                    //do something
                	throw new Error("Invalid console input");
                }
                
            } catch (NoSuchElementException nse) {
                // currentExpression was empty
                System.out.println("must enter an expression before using this command");
            } catch (RuntimeException re) {
                System.out.println(re.getClass().getName() + ": " + re.getMessage());
            }
        }
    }

    /**
     * Start a game server: 
     * listen for connections and read inputs from the console for joining boards.
     * 
     * Command line usage: 
     *   FlingballServer [--port PORT]
     * 
     * where PORT is an integer in the range 0 to 65535 inclusive,
     * specifying the port where the server should listen for incoming connections.
     * 
     * If no port is specified, the default port is 10987.
     * 
     * Requires: args.length()%2 == 0
     * 
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        //Run this in one thread:
    	
    	
    	System.out.println("Running main method flingball server");
    	
    	FlingballServer server;
    	if (args.length == 0) {
    		server = new FlingballServer(DEFAULT_PORT);
		} else if (args.length == 2) {
        	//System.out.println("args length == 2");
            server = new FlingballServer(Integer.parseInt(args[1]));
        } else {
            throw new IllegalArgumentException("illegal input for port");
        }
    	Thread serveThread = new Thread(new Runnable() {
            public void run() {
            	
            	//System.out.println("running server thread");
            	
            	try {
					server.serve();
						
				} catch (IOException e) {
					//Auto-generated catch block
					System.out.println("tried to serve");
					e.printStackTrace();
				}  	
            }
        });
    	serveThread.start();
    	
    	Thread consoleThread = new Thread(new Runnable() {
    		public void run() {
    			//System.out.println("running console thread");
    			
    			try {
					server.readCommands();
				} catch (IOException e) {
					//Auto-generated catch block
					System.out.println("console thread exception!!");
					e.printStackTrace();
				}
    			
    		}
    	});
    	consoleThread.start();
        
    }
}
