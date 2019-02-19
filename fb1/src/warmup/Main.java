package warmup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import physics.Circle;
import physics.LineSegment;
import physics.Vect;

public class Main {

    private static final int GAMEBOARD_SIZE = 20;
    private static final int PIXELS_PER_L = 20;
    private static final int DRAWING_AREA_SIZE_IN_PIXELS = GAMEBOARD_SIZE * PIXELS_PER_L;

    private static final int TIMER_INTERVAL_MILLISECONDS = 50;

    private static Wall walls;
    private static Ball ball;

    public static void main(String[] args) {
        animationApproach();

    }


    private static void animationApproach() {
        final JFrame window = new JFrame("Ball1");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel drawingArea = new JPanel();
        drawingArea.setPreferredSize(new Dimension(DRAWING_AREA_SIZE_IN_PIXELS, DRAWING_AREA_SIZE_IN_PIXELS));
        window.add(drawingArea);
        window.pack();
        window.setVisible(true);

        int count = 0;

        try {
            initializeBall();
            while(count < 10000) {
                Graphics g = drawingArea.getGraphics();
                drawBall(g);
                count++;

                System.out.println(ball.toString() +"+++" + ball.getVelocity().toString());

                ball.move(walls);
                Thread.sleep(TIMER_INTERVAL_MILLISECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private static void initializeBall() {
        Circle startBall = new Circle(new Vect(10, 10), PIXELS_PER_L);
        Vect startBallVelocity = new Vect(PIXELS_PER_L/2, PIXELS_PER_L/5);
        // hold onto velocity separately
        ball = new Ball(startBall, startBallVelocity);
        // pass in velocity to reflectWall

        // Walled line segments
        LineSegment topWall = new LineSegment(0.0, 0.0, 20*PIXELS_PER_L, 0.0);
        LineSegment bottomWall = new LineSegment(0.0, 20*PIXELS_PER_L, 20*PIXELS_PER_L, 20*PIXELS_PER_L);
        LineSegment leftWall = new LineSegment(0.0, 0.0, 0.0, 20*PIXELS_PER_L);
        LineSegment rightWall = new LineSegment(20*PIXELS_PER_L, 0.0, 20*PIXELS_PER_L, 20*PIXELS_PER_L);
        List<LineSegment> w = new ArrayList<LineSegment>(
                Arrays.asList(topWall, bottomWall, leftWall, rightWall));
        walls = new Wall(w);

    }


    private static void drawBall(final Graphics g) {
        Graphics2D g2 = (Graphics2D) g;  // every Graphics object is also a Graphics2D, which is a stronger spec

        // fill the background to erase everything
        g2.setColor(Color.black);
        g2.fill(new Rectangle2D.Double(0, 0, DRAWING_AREA_SIZE_IN_PIXELS, DRAWING_AREA_SIZE_IN_PIXELS));

        // fill the ball with yellow
        g2.setColor(Color.yellow);
        g2.fill(new Ellipse2D.Double(ball.getLocation().x()-ball.getRadius(), ball.getLocation().y()-ball.getRadius(), 2*ball.getRadius(), 2*ball.getRadius()));

    }




}
