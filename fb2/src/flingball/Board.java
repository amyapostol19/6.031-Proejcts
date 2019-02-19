package flingball;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import physics.LineSegment;
import physics.Physics;
import physics.Vect;

/**
 * A mutable type representing the Flingball board with balls and gadgets,
 * surrounded by four boarder walls lying just outside the playing area.
 */
public class Board {
    public final static Map<Integer,String> keyName;
    static {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(KeyEvent.VK_A, "a");
        map.put(KeyEvent.VK_B, "b");
        map.put(KeyEvent.VK_C, "c");
        map.put(KeyEvent.VK_D, "d");
        map.put(KeyEvent.VK_E, "e");
        map.put(KeyEvent.VK_F, "f");
        map.put(KeyEvent.VK_G, "g");
        map.put(KeyEvent.VK_H, "h");
        map.put(KeyEvent.VK_I, "i");
        map.put(KeyEvent.VK_J, "j");
        map.put(KeyEvent.VK_K, "k");
        map.put(KeyEvent.VK_L, "l");
        map.put(KeyEvent.VK_M, "m");
        map.put(KeyEvent.VK_N, "n");
        map.put(KeyEvent.VK_O, "o");
        map.put(KeyEvent.VK_P, "p");
        map.put(KeyEvent.VK_Q, "q");
        map.put(KeyEvent.VK_R, "r");
        map.put(KeyEvent.VK_S, "s");
        map.put(KeyEvent.VK_T, "t");
        map.put(KeyEvent.VK_U, "u");
        map.put(KeyEvent.VK_V, "v");
        map.put(KeyEvent.VK_W, "w");
        map.put(KeyEvent.VK_X, "x");
        map.put(KeyEvent.VK_Y, "y");
        map.put(KeyEvent.VK_Z, "z");
        map.put(KeyEvent.VK_0, "0");
        map.put(KeyEvent.VK_1, "1");
        map.put(KeyEvent.VK_2, "2");
        map.put(KeyEvent.VK_3, "3");
        map.put(KeyEvent.VK_4, "4");
        map.put(KeyEvent.VK_5, "5");
        map.put(KeyEvent.VK_6, "6");
        map.put(KeyEvent.VK_7, "7");
        map.put(KeyEvent.VK_8, "8");
        map.put(KeyEvent.VK_9, "9");
        map.put(KeyEvent.VK_SHIFT, "shift");
        map.put(KeyEvent.VK_CONTROL, "ctrl");
        map.put(KeyEvent.VK_ALT, "alt");
        map.put(KeyEvent.VK_META, "meta");
        map.put(KeyEvent.VK_SPACE, "space");
        map.put(KeyEvent.VK_LEFT, "left");
        map.put(KeyEvent.VK_RIGHT, "right");
        map.put(KeyEvent.VK_UP, "up");
        map.put(KeyEvent.VK_DOWN, "down");
        map.put(KeyEvent.VK_MINUS, "minus");
        map.put(KeyEvent.VK_EQUALS, "equals");
        map.put(KeyEvent.VK_BACK_SPACE, "backspace");
        map.put(KeyEvent.VK_OPEN_BRACKET, "openbracket");
        map.put(KeyEvent.VK_CLOSE_BRACKET, "closebracket");
        map.put(KeyEvent.VK_BACK_SLASH, "backslash");
        map.put(KeyEvent.VK_SEMICOLON, "semicolon");
        map.put(KeyEvent.VK_QUOTE, "quote");
        map.put(KeyEvent.VK_ENTER, "enter");
        map.put(KeyEvent.VK_COMMA, "comma");
        map.put(KeyEvent.VK_PERIOD, "period");
        map.put(KeyEvent.VK_SLASH, "slash");
        keyName = Collections.unmodifiableMap(map);
    }
    
    public static final double DEFAULT_GRAVITY = 25.0;
    public static final double DEFAULT_FRICTION = 0.025;
    private static final double SIZE = 20;
    
    private final String name;
    private final Map<String, Wall> walls;
    private final Map<String, Gadget> gadgets = new HashMap<>();
    private final Set<Flipper> flippers = new HashSet<>();
    private final Map<String, Portal> portals = new HashMap<>();
    private final Map<String, Ball> balls = new HashMap<>();
    private final Map<String, List<Gadget>> keyPressGadgets = new HashMap<>();
    private final Map<String, List<Gadget>> keyReleaseGadgets = new HashMap<>();
    private final double gravity;
    private final double friction1;
    private final double friction2;
    private final double reflectionCoeff = 1.0; // default
    private Optional<BoardClient> client = Optional.empty();
    
    /* 
     * Abstraction function:
     *     AF(name, walls, gadgets, balls, gravity, friction1, friction2, reflectionCoeff): 
     *         a Flingball board, whose name is ``name``, containing Flingball gadget(s) in the list ``gadgets``
     *         and ball(s) in the list ``balls``; its four border walls are the line segments 
     *         in the list ``walls`` and have reflection coefficient ``reflectionCoeff``.
     *         The board has global gravity ``gravity`` as well as friction values 
     *         mu equal to ``friction1`` and mu2 equal to ``friction2``.
     *         
     * Rep Invariant:
     *   - walls has size 4 and contains the four line segments representing the four border walls:
     *      - one horizontal wall just above the y=0L coordinate
     *      - one horizontal wall just below the y=20L coordinate
     *      - one vertical wall just to the left of the x=0L coordinate
     *      - one vertical wall just to the right of the x=20L coordinate
     *   - gravity >= 0
     *   - friction1 >= 0
     *   - friction2 >= 0
     *   - reflectionCoeff = 1
     *     
     * Safety from rep exposure:
     *   - all fields except ``gadgets`` and ``balls`` are private, immutable, and final
     *   - ``gadgets`` and ``balls`` are private and final, and their references are never shared with clients
     *   
     * Thread Safety Argument
     * 	 - Board is not currently threadsafe because other threads may remove a ball from the board
     * 		while it is still iterating through the list of balls and checking each ball for collisions
     * 		TODO figure out a way to fix this issue and make Board threadsafe
     *     
     */
    
    /**
     * Creates a new board with default gravity (0.25) and friction values (mu1 = mu2 = 0.025),
     * whose name is specified by the argument of the same name.
     * @param name name of the board
     * @throws IOException 
     */
    public Board(final String name) throws IOException {
        this(name, DEFAULT_GRAVITY, DEFAULT_FRICTION, DEFAULT_FRICTION);
    }
    
    /**
     * Creates a new board with the specified gravity and friction values,
     * whose name is specified by the argument of the same name.
     * @param name name of the board
     * @param gravity gravity of the Flingball board
     * @param friction1 global friction constant mu1
     * @param friction2 global friction constant mu2
     * @throws IOException 
     */
    public Board(final String name, final double gravity, final double friction1, final double friction2) throws IOException {
        this.name = name;
        this.walls = new HashMap<>(); 
        		walls.put("Left", new Wall(new LineSegment(0, 0, 0, SIZE), "Left")); 
                walls.put("Top", new Wall(new LineSegment(0, 0, SIZE, 0), "Top")); 
                walls.put("Bottom", new Wall(new LineSegment(0, SIZE, SIZE, SIZE), "Bottom")); 
                walls.put("Right", new Wall(new LineSegment(SIZE, 0, SIZE, SIZE), "Right"));
        this.gravity = gravity;
        this.friction1 = friction1;
        this.friction2 = friction2;
        
        final int default_port = 10987;
        
        //this.client = new BoardClient("", default_port, this);
        //this.client = Optional.of(new BoardClient("", default_port, this));
        checkRep();
    }
    
    // checkRep
    private void checkRep() {
        assert name != null;
        assert walls != null;
        final Vect upperLeft = new Vect(0, 0);
        final Vect upperRight = new Vect(SIZE, 0);
        final Vect lowerLeft = new Vect(0, SIZE);
        final Vect lowerRight = new Vect(SIZE, SIZE);
        final Set<Wall> expectedWalls = new HashSet<>(Arrays.asList(new Wall(new LineSegment(upperLeft, upperRight), "Top"), 
                                                                new Wall(new LineSegment(upperLeft, lowerLeft), "Left"), 
                                                                new Wall(new LineSegment(lowerLeft, lowerRight), "Bottom"), 
                                                                new Wall(new LineSegment(upperRight, lowerRight), "Right")));
        //assert expectedWalls.equals(new HashSet<>(walls));
        assert gadgets != null;
        assert balls != null;
        assert gravity >= 0;
        assert friction1 >= 0;
        assert friction2 >= 0;
    }
    
    /**
     * Displays this board across the window.
     * @param g graphics for the drawing buffer for the window. 
     *          Modifies this graphics by drawing the state of the board on it.
     */
    public void render(final Graphics g) {
        Graphics2D g2 = (Graphics2D) g;  // every Graphics object is also a Graphics2D, which is a stronger spec
        
        // fill the background to erase everything
        g2.setColor(Color.black);
        g2.fill(new Rectangle2D.Double(0, 0, Flingball.DRAWING_AREA_SIZE_IN_PIXELS, Flingball.DRAWING_AREA_SIZE_IN_PIXELS));
        
        for (String wall: walls.keySet()) {
            if (wall.equals("Top") && walls.get("Top").hasNeighboringBoard()) {
                final int X_COORD_STRING = 9;
                final int Y_COORD_STRING = 0;
                g2.drawString(walls.get("Top").getNeighboringBoard(),X_COORD_STRING*Flingball.PIXELS_PER_L,
                                                                     Y_COORD_STRING*Flingball.PIXELS_PER_L);
            }
        }
        for (Gadget gadget : gadgets.values()) {
            gadget.render(g2);
        }
        for (Ball ball : balls.values()) {
            ball.render(g2);
        }
    }
    
    /**
     * Starts a Flingball game on this board.
     */
    public void simulate() {
    	Thread listen = new Thread(new Runnable() {
    		public void run() {
    			System.out.println("listening");
    			while (true) {
    				if (client.isPresent()) {
    					try {
							client.get().getReply();
						} catch (IOException e) {
							// Auto-generated catch block
							e.printStackTrace();
						}
    				}
    			}
    		}
    	});
    	listen.start();
    	
        new Thread(() -> {
            long currentTime = System.currentTimeMillis();
            while (true) {
            	//check if any walls have been removed or added to board and if any games are adjacent
            	                
                // resolve all collisions
                for (final Ball ball : new ArrayList<Ball>(balls.values())) {
                    for (final Gadget gadget : gadgets.values()) {
                        gadget.resolveCollision(ball);
                    }
                    resolveCollision(ball);
                }
                // update the position of the balls to account for time passing
                final long newTime = System.currentTimeMillis();
                final long elapsedTime = newTime - currentTime;
                currentTime = newTime;
                for (Flipper flipper: flippers) flipper.move(elapsedTime, this);
                for (final Ball ball : new ArrayList<Ball>(balls.values())) ball.move(elapsedTime, this); 
                resolveBallCollisions();
                
            }
        }).start();    
    }
    
    public void resolveBallCollisions() {
        Map<Ball, Ball> collisions = new HashMap<>();
        for (Ball ball1: balls.values()) {
            for (Ball ball2: balls.values()) {
                if (ball1.equals(ball2)) continue;
                final long collisionTime = (long) (Physics.timeUntilBallBallCollision(
                        ball1.getCircle(), ball1.getVelocity(), ball2.getCircle(), ball2.getVelocity())*1000);
                if (collisionTime <= 1 && !collisions.keySet().contains(ball2) && !collisions.keySet().contains(ball1)) {
                        collisions.put(ball1,  ball2);
                }
            }
        }
        for (Ball ball1: collisions.keySet()) {
            Ball ball2 = collisions.get(ball1);
            Physics.VectPair vectPair = Physics.reflectBalls(ball1.getPosition(), 1., ball1.getVelocity(), ball2.getPosition(), 1., ball2.getVelocity());
            ball1.setVelocity(vectPair.v1);
            ball2.setVelocity(vectPair.v2);
        }
    }
    
    /**
     * Resolve collision if a ball is colliding with the outer walls of this board,
     * according to the rules of Flingball Physics.
     * @param ball ball of the Flingball game
     * @return true if the ball collides with any of the outer walls, 
     *         false otherwise
     */
    public boolean resolveCollision(Ball ball) {
        /*
         * Call timeUntilWallCollision() methods to calculate 
         * the times at which the ball will collide with each of lines. 
         * The minimum of all these times is the time of the next collision.
         */
        long mintimeLine = Long.MAX_VALUE;
        Wall nextCollidingWall = null;
        for (Wall wall : walls.values()) {
            final long collisionTime = (long) (Physics.timeUntilWallCollision(wall.getLineSegment(), ball.getCircle(), ball.getVelocity())*1000);
            if (collisionTime < mintimeLine) {
                mintimeLine = collisionTime;
                nextCollidingWall = wall;
            }
        }
        if (mintimeLine <= 1) {
        	//if the wall still exists on the board
        	if (!nextCollidingWall.isRemoved()) {
	            // call reflectWall() to calculate the change in the ball's velocity
	            final Vect newVelocity = Physics.reflectWall(nextCollidingWall.getLineSegment(), ball.getVelocity(), reflectionCoeff);
	            ball.setVelocity(newVelocity); // updates the ball's velocity
	            return true;
        	} else if (nextCollidingWall.isRemoved() && client.isPresent()) {
        		System.out.println("colliding with a removed wall");
        		client.get().sendRequest(ball, nextCollidingWall);
        	}
        }
        return false; // no collision
    }
    
    /**
     * Adds a gadget to this board.
     * @param gadget gadget to add
     */
    public void addGadget(final Gadget gadget) {
        gadgets.put(gadget.getName(), gadget);
        if (gadget instanceof Flipper) {
            Flipper flipper = (Flipper) gadget;
            flippers.add(flipper);
        }
        if (gadget instanceof Portal) {
            Portal portal = (Portal) gadget;
            portals.put(portal.getName(), portal);
        }
        checkRep();
    }

    /**
     * Adds a ball to this board.
     * @param ball ball to add
     */
    public void addBall(final Ball ball) {
        balls.put(ball.getName(), ball);
        checkRep();
    }
    
    /**
     * Adds a gadget to the list of gadgets triggered by pressing a given key
     * @param key
     * @param gadgetName
     */
    public void addKeyPressTrigger(final String key, final String gadgetName) {
        if (this.keyPressGadgets.containsKey(key)) {
            this.keyPressGadgets.get(key).add(this.gadgets.get(gadgetName));
        } else {
            this.keyPressGadgets.put(key, new ArrayList<Gadget>(Collections.singletonList(this.gadgets.get(gadgetName))));
        }
    }
    
    /**
     * Adds a gadget to the list of gadgets triggered by releasing a given key
     * @param key
     * @param gadgetName
     */
    public void addKeyReleaseTrigger(final String key, final String gadgetName) {
        if (this.keyReleaseGadgets.containsKey(key)) {
            this.keyReleaseGadgets.get(key).add(this.gadgets.get(gadgetName));
        } else {
            this.keyReleaseGadgets.put(key, new ArrayList<Gadget>(Collections.singletonList(this.gadgets.get(gadgetName))));
        }
    }
    
    public void addClient(BoardClient client) {
    	    this.client = Optional.of(client);
    }
    
    /**
     * Removes the specified gadget from this board
     * @param gadget
     */
    public void removeGadget(final Gadget gadget) {
        gadgets.remove(gadget.getName());
        checkRep();
    }
    
    /**
     * Removes the specified Ball from the board
     * (Will be useful when Ball's have to switch to a different board)
     * @param ball
     */
    public void removeBall(final String ball) {
        balls.remove(ball);
        checkRep();
    }
    
    /**
     * Links a trigger-action event between two gadgets on the board.
     * @param triggeringGadgetName name of the gadget that will trigger the action
     * @param actionGadegetName name of the gadget that will be triggered to perform the action
     */
    public void addTrigger(final String triggeringGadgetName, String actionGadegetName) {
        gadgets.get(triggeringGadgetName).addTrigger(gadgets.get(actionGadegetName));
        checkRep();
    }
    
    /**
     * 
     * @param key
     */
    public void keyPressTrigger(String key) {
        if (this.keyPressGadgets.containsKey(key)) {
            for (Gadget gadget: this.keyPressGadgets.get(key)) {
                gadget.respondToTrigger();
            }
        }   
    }
    
    /**
     * 
     * @param key
     */
    public void keyReleaseTrigger(String key) {
        if (this.keyReleaseGadgets.containsKey(key)) {
            for (Gadget gadget: this.keyReleaseGadgets.get(key)) {
                gadget.respondToTrigger();
            }
        }   
    }
    
    /** @return name of board. */
    public String getName() {
        return name;
    }
    
    /** @return list containing the outer walls of the board */
    public List<Wall> getWalls() {
        checkRep();
        return new ArrayList<Wall>(walls.values());
    }
    
    /**
     * 
     * @return the names of all the portals on the board
     */
    public List<String> getPortalNames() {
    	List<String> portalList = new ArrayList<String>();
    	for (String portal : portals.keySet()) {
    		portalList.add(portal);
    	}
    	return portalList;
    }
    
    /**
     * 
     * @param side the side that the wall we want is on
     * @return a wall on the specific side
     */
    public Wall getWall(String side) {
    	return walls.get(side);
    }
    
    /** @return gravity of the Flingball board */
    public double getGravity() {
        checkRep();
        return gravity;
    }
    
    /** @return global friction constant mu1 */
    public double getFriction1() {
        checkRep();
        return friction1;
    }
    
    /** @return global friction constant mu2 */
    public double getFriction2() {
        checkRep();
        return friction2;
    }
    
    /** @return reflection coefficient of the board's out walls */
    public double getReflectionCoeff() {
        checkRep();
        return reflectionCoeff;
    }
    
    /** @return set of the name of gadgets on this board */
    public Set<String> getGadgetsNames(){
        checkRep();
        return new HashSet<>(gadgets.keySet());
    }
    
    /**
     * 
     * @param name the name of the gadget we want
     * @return gadget
     */
    public Boolean hasGadget(String gadgetName) {
        return gadgets.containsKey(gadgetName);
    }
    
    /**
     * 
     * @param name the name of the gadget we want
     * @return gadget
     */
    public Gadget getGadgetByName(String gadgetName) {
        return gadgets.get(gadgetName);
    }
    
    public Portal getPortalByName(String name) {
    	return portals.get(name);
    }
    
    /** @return set of the name of balls that are currently on this board */
    public Set<String> getBallsNames(){
        checkRep();
        return new HashSet<>(balls.keySet());
    }

    /**
     * 
     * @return the client that connects this board to the server
     */
    public BoardClient getClient() {
    	    return client.get();
    }
    
    /**
     * Indicates whether some other object has the same value as this board.
     * @param that an object with which to compare
     * @return true if that object is observationally equal to this one,
     *         false otherwise.
     */
    public boolean sameValue(final Object that) {
        checkRep();
        if (!(that instanceof Board)) return false;
        final Board other = (Board) that;
        if (this.name.equals(other.name)
            && this.gravity == other.gravity
            && this.friction1 == other.friction1
            && this.friction2 == other.friction2
            && this.reflectionCoeff == other.reflectionCoeff
            && this.gadgets.size() ==  other.gadgets.size()
            && this.balls.size() == other.balls.size()) {
            for (String gadgetName : gadgets.keySet()) {
                if (!this.gadgets.get(gadgetName).sameValue(other.gadgets.get(gadgetName)))
                    return false;
            }
            for (String ballName : balls.keySet()) {
                if (!this.balls.get(ballName).sameValue(other.balls.get(ballName)))
                    return false;
            }
            return true;
        }
        return false;
    }
    public boolean equals(Board other) {
        return this.sameValue(other);
    }
    @Override 
    public String toString() {
        checkRep();
        final StringBuilder string = new StringBuilder();
        final Formatter formatter = new Formatter(string);
        formatter.format("Board <%s> with gravity=%f, friction1=%f, friction2=%f, and reflectionCoeff=%f\n", 
                         name, gravity, friction1, friction2, reflectionCoeff);
        formatter.format("With %d gadgets\n", gadgets.size());
        for (Gadget gadget : gadgets.values()) {
            formatter.format("  - %s\n", gadget.getName());
        }
        formatter.format("With %d balls\n", balls.size());
        for (Ball ball : balls.values()) {
            formatter.format("  - %s\n", ball.getName());
        }
        string.deleteCharAt(string.length() - 1); // delete last new line character
        return string.toString();
    }
    
}

