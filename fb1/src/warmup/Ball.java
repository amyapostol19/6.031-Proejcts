package warmup;

import physics.Circle;
import physics.LineSegment;
import physics.Physics;
import physics.Vect;

// Mutable ball class
public class Ball {

     private Circle ball;
     private Vect velocity;
     
     //AF()
     //RI location cannot be negative
     
     
     public Ball(Circle ball, Vect velocity) {
         this.ball = ball;
         this.velocity = velocity;
     }
     
     public double getRadius() {
         return this.ball.getRadius();
     }
     
     public Circle getBall() {
         return this.ball;
     }
     
     public Vect getLocation() {
         return this.ball.getCenter();
     }
     public Vect getVelocity(){
         return this.velocity;
     }
     
     private void changeLocation(Circle newBall) {
         this.ball = newBall;
     }
     
     private void changeVelocity(Vect newVelocity) {
         this.velocity = newVelocity;
     }
     
     public void move(Wall walls) {
         
         boolean collision = false;
         Vect newVec;
         Vect newCenter;
         double startLocationX = getLocation().x();
         double startLocationY = getLocation().y();
         
         for(LineSegment w : walls.getWalls()) {
             if (Physics.timeUntilWallCollision(w, this.ball, getVelocity()) < 1){
                 
                 newVec = Physics.reflectWall(w, getVelocity());
                 newCenter = new Vect(startLocationX + newVec.x(), startLocationY + newVec.y());
                 changeLocation(new Circle(newCenter, getRadius()));
                 changeVelocity(newVec);
                 
                 collision = true;
                 break;
             }
         }
         if(!collision) {
             newCenter = new Vect(startLocationX + getVelocity().x(), startLocationY + getVelocity().y());
             changeLocation(new Circle(newCenter, getRadius()));
         }
                  
     }
     
     public String toString() {
         return this.ball.toString();
     }
     
     public int hashCode() {
         return this.ball.hashCode() + this.velocity.hashCode();
     }
}
