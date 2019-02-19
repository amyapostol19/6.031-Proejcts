package flingball;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import edu.mit.eecs.parserlib.UnableToParseException;

/**
 * Console interface to the Flingball game
 * 
 * Flingball is a game played on a 20x20 board. The goal of the game is to keep a ball moving around the board
 * without it falling off the bottom of the playing area. The board has various gadgets that deflect the ball,
 * and the player controls a set of flippers that can bat at the ball as it falls
 */
public class Flingball {

    public static final int GAMEBOARD_SIZE = 20;
    public static final int PIXELS_PER_L = 20;
    public static final int DRAWING_AREA_SIZE_IN_PIXELS = GAMEBOARD_SIZE * PIXELS_PER_L;

    private static final int TIMER_INTERVAL_MILLISECONDS = 50; // for ~20 frames per second

    /**
     * Displays and animates the playing board of this Flingball game.
     * @param board board to be displayed 
     */
    public static void render(final Board board) {
        final JFrame window = new JFrame("Flingball (" + board.getName() + ")");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        KeyListener listener = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                report("press", e.getKeyCode());
                board.keyPressTrigger(Board.keyName.get(e.getKeyCode()));
            }

            @Override public void keyReleased(KeyEvent e) {
                report("release", e.getKeyCode());
                board.keyReleaseTrigger(Board.keyName.get(e.getKeyCode()));
            }
            
            private void report(String whatHappened, int keyCode) {
                System.out.println(
                        whatHappened
                        + " "
                        + Board.keyName.get(keyCode) // note: may return null, which will print "null"
                        );
                
            }
        };
        /*
        if (args.length > 0 && args[0].equals("--magic")) {
            System.err.println("turning on MagicKeyListener to work around Linux problem");
            listener = new MagicKeyListener(listener);
        }*/

        window.addKeyListener(listener);
        final JPanel drawingArea = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                board.render(g);
            }
        };
        drawingArea.setPreferredSize(new Dimension(DRAWING_AREA_SIZE_IN_PIXELS, DRAWING_AREA_SIZE_IN_PIXELS));
        window.add(drawingArea);
        window.pack();
        window.setVisible(true);

        board.simulate();
        // note: the time must be javax.swing.Timer, not java.util.Timer
        new Timer(TIMER_INTERVAL_MILLISECONDS, (ActionEvent e) -> {
            drawingArea.repaint();
        }).start();
    }

    /**
     * Read command line inputs from the console and renders the board. 
     * 
     * Command line usage: 
     *   Flingball [--host HOST] [--port PORT] [FILE]
     *   
     *   HOST is an optional hostname or IP address of the server to connect to. 
     *   If no HOST is provided, then the client runs in single-machine play mode, 
     *   running a board and allowing the user to play it without any network connection to any other board.
     *   
     *   PORT is an optional integer in the range 0 to 65535 inclusive, 
     *   specifying the port where the server is listening for incoming connections. 
     *   The default port is 10987.
     *   
     *   FILE is an optional argument specifying a file pathname of the Flingball board
     *   that this client should run. 
     *   If FILE is not provided, then your Flingball client should run the default 
     *   benchmark board as described in the phase 1 specification.
     * 
     * @param args an optional command-line argument representing the optional arguments described above
     * @throws IllegalArgumentException if board file is syntactically invalid
     * @throws IOException if there is an error reading the input
     */
    public static void main(String[] args) throws IOException {
        try {
            if (args.length == 0) render(BoardParser.parse("boards/default.fb"));
            
            else if (args.length == 1) render(BoardParser.parse(args[0]));
            
            else if (args.length == 4) {
            	String host = args[1];
            	int port = Integer.parseInt(args[2]);
            	String file = args[3];
            	
            	Board currentBoard = BoardParser.parse(file);
            	//adding a client to the board automatically connects a socket to the server
            	currentBoard.addClient(new BoardClient(host, port, currentBoard)); //make sure line is uncommented if trying to connect to server
            	
            	render(currentBoard);
            }

        } catch (UnableToParseException e) {
            throw new IllegalArgumentException("input board file is syntactically invalid", e);
        } 
    }

}

