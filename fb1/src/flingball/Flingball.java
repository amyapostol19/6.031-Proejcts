package flingball;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.mit.eecs.parserlib.UnableToParseException;
import physics.Circle;
import physics.Vect;


/**
 * UI Class: creates UI for our flingball implementation
 */
public class Flingball {

    
    /**
     * TODO: describe your main function's command line arguments here
     * @throws UnableToParseException 
     */
    public static void main(String[] args) throws UnableToParseException, IOException, Exception {
        Board board = parse("boards/absorber.fb");
        animationApproach(board);

    }
    
    private static Board parse(String string) throws UnableToParseException, IOException, Exception {
       return FlingballParser.parse(string);

    }


    private static final int GAMEBOARD_SIZE = 20;
    private static final int PIXELS_PER_L = 20;
    private static final int DRAWING_AREA_SIZE_IN_PIXELS = GAMEBOARD_SIZE * PIXELS_PER_L;

    private static final int TIMER_INTERVAL_MILLISECONDS = 50;
    



    public static void animationApproach(Board board) {
        final JFrame window = new JFrame("Flingball!");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel drawingArea = new JPanel();
        drawingArea.setPreferredSize(new Dimension(DRAWING_AREA_SIZE_IN_PIXELS, DRAWING_AREA_SIZE_IN_PIXELS));
        window.add(drawingArea);
        window.pack();
        window.setVisible(true);

        int count = 0;
        
        try {
            while(count < 10000) {
                Graphics g = drawingArea.getGraphics();
                drawBall(g, board);
                count++;
                
                // Empty list of balls to be removed from the board
                List<Ball> removed = new ArrayList<Ball>();
                
                // Move all of the balls
                // ball.remove() returns true if hits absorber
                for(Ball ball : board.getBalls()) {
                  ball.move(board);
              }

                
                // Wait to acquire the desired 20 fps
                Thread.sleep(TIMER_INTERVAL_MILLISECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }




    private static void drawBall(final Graphics g, Board board) {
        Graphics2D g2 = (Graphics2D) g; 

        // fill the background to erase everything
        g2.setColor(Color.black);
        g2.fill(new Rectangle2D.Double(0, 0, DRAWING_AREA_SIZE_IN_PIXELS, DRAWING_AREA_SIZE_IN_PIXELS));

        // fill the ball with white
        g2.setColor(Color.white);
        for(Ball ball : board.getBalls()) {
            g2.fill(ball.fill());

        }
        
        // fill in the gadgets
        g2.setColor(Color.red);
        for(Gadget gadget : board.getGadgets()) {
            
            // Absorbers will be colored green
            if(gadget.getType().equals("Absorber")) {
                g2.setColor(Color.green);
                
            }
            
            // Draw the shape on the board
            g2.fill(gadget.fill());

            if(gadget.getType().equals("Absorber")) {
                Absorber gg = (Absorber) gadget;
                
                // Draw a ball in the bottom right corner of the absorber if a ball exists
                if(gg.containsBalls()) {
                    // Draw a ball in the lower right hand corner
                    Circle circle = new Circle(new Vect(gg.getLocation().x() + gg.getWidth() - .25*PIXELS_PER_L,gg.getLocation().y() + gg.getHeight() - .25*PIXELS_PER_L),.25*PIXELS_PER_L);
                    Ball smallBall = new Ball(circle, new Vect(0,0), "");
                    g2.setColor(Color.white);
                    g2.fill(smallBall.fill());
                }
            }
            // Reset color to red because all bumpers should be red
            g2.setColor(Color.red);

        }
    }






}
