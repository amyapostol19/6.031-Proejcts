package warmup;

import java.util.List;

import physics.LineSegment;
import physics.Physics;
import physics.Vect;

public class Main {

    public static void main(String[] args) {
        final double posX = 10.0;
        final double posY = 10.0;
        final double speed = 5.0;
        final double angle = 2*Math.PI*Math.random();
        final Ball ball = new Ball(posX, posY, speed, angle);
        final Board board = new Board();
        
        long mintime = -1;
        LineSegment nextCollidingWall = null;
        long currentTime = System.currentTimeMillis();
        long lastPrintedTime = -1;
       
        while (true) {
            if (currentTime % 100 == 0 && currentTime != lastPrintedTime) {
                System.out.println(currentTime);
                System.out.println("Position of the ball is:"+ball.getPosition());
                lastPrintedTime = currentTime;
            }
            
            if (mintime <= 1) { // at this point, the ball and the wall it is about to hit are exactly adjacent to one another
                if (mintime != -1) {
                    // call reflectWall() to calculate the change in the ball's velocity
                    Vect newVelocity = Physics.reflectWall(nextCollidingWall, ball.getVelocity(), board.getReflectionCoeff());
                    ball.setVelocity(newVelocity); // updates the ball's velocity
                }
                /*
                 * Call the timeUntilCollision() methods to calculate the times at which the ball will 
                 * collide with each of walls. The minimum of all these times (call it "mintime") 
                 * is the time of the next collision.
                 */
                List<LineSegment> walls = board.getSides();
                mintime = (long) (Physics.timeUntilWallCollision(walls.get(0), ball.getCircle(), ball.getVelocity()) * 1000);
                nextCollidingWall = walls.get(0);
                for (int i = 1; i < walls.size(); i++) {
                    final long collisionTime = (long) (Physics.timeUntilWallCollision(walls.get(i), ball.getCircle(), ball.getVelocity())*1000);
                    if (collisionTime < mintime) {
                        mintime = collisionTime;
                        nextCollidingWall = walls.get(i);
                    }
                }
            }
            else {
                final long newTime = System.currentTimeMillis();
                final long elapsedTime = newTime - currentTime;
                currentTime = newTime;
                ball.move(elapsedTime); // update the position of the ball to account for mintime passing
                mintime -= elapsedTime;
            }
        }
    }
    
}
