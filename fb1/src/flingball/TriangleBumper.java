package flingball;

import java.awt.Polygon;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import physics.Angle;
import physics.LineSegment;
import physics.Vect;

public class TriangleBumper implements Gadget {

    private final double minBoardDimensions = 0.0;
    private final double maxBoardDimensions = 400.0;

    private final Board board;
    private final int width;
    private final int height;
    private final Vect location;
    private final Angle orientation;
    private final String name;
    
    private final List<Angle> validAngles  = new ArrayList<>(Arrays.asList(new Angle(0), new Angle(90), new Angle(180), new Angle(270)));


    /*
     * AF(legLength, location, orientation, name) = represents a triangle bumper object in flingball that has a leg/hypotenuse length,
     *      a location, and orientation.  Bumper has a unique name to distinguish it form other bumpers
     *      
     * RI:
     *      Width and height must be positive
     *      Reflection coefficient is one
     *      Entirety of the bumper is within the board dimensions
     *      name is non null
     *      
     * Safety from rep exposure:
     *      All fields are private
     *      Do not return mutable objects
     */
    
    
    /**
     * isoceles right triangle
     * @param board Board that the object is apart of
     * @param legLength Integer length of the legs of the TriangleBmper
     * @param location Point location of the top left corner of the TriangleBumper
     * @param orientation Angle orientation of Absorber compared to center (0 degrees)
     * @param name String name that is unique to the object
     */
    public TriangleBumper(Board board, int legLength, Vect location, Angle orientation, String name) {
        this.width = legLength;
        this.height = legLength; 
        this.location = location;
        this.orientation = orientation;
        this.name = name;
        this.board = board;
        
        checkRep();
        

        
    }
    
    /**
     * Guarantee that the rep invariants are not broken
     */
    private void checkRep() {
        
        assert (width >= minBoardDimensions);
        assert (width==height);
        assert (validAngles.contains(orientation));
        assert (location.x() >= minBoardDimensions);
        assert (location.y() >= minBoardDimensions);
        assert (location.x() + this.width <= maxBoardDimensions);
        assert (location.y() + this.height<= maxBoardDimensions);
        assert this.name != null;
  
        
    }
    
    public String getName() {
        return this.name;
    }
    
    public Vect getLocation() {
        return this.location;
    }

    public double getReflectionCoefficient() {
        return 1.0;
    }

    public Angle getOrientation() {
        return this.orientation;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }
    
    public Shape fill() {
        List<LineSegment> ls = getLineSegments();
        
        int[] x = new int[] {(int) Math.round(ls.get(0).p1().x()), (int) Math.round(ls.get(1).p1().x()), (int) Math.round(ls.get(2).p1().x())};
        int[] y = new int[] {(int) Math.round(ls.get(0).p1().y()), (int) Math.round(ls.get(1).p1().y()), (int) Math.round(ls.get(2).p1().y())};
                
        return (Shape) new Polygon(x, y, 3);
   }
    
    public List<LineSegment> getLineSegments() {
        List<LineSegment> lineList = new ArrayList<LineSegment>();
        Vect v1 = new Vect(getLocation().x(), getLocation().y());  //Top left
        Vect v2 = new Vect(getLocation().x() + this.width, getLocation().y()); //Top right
        Vect v3 = new Vect(getLocation().x(), getLocation().y()+this.height);  //Bottom left
        Vect v4 = new Vect(getLocation().x() + this.width, getLocation().y()+this.height); //Bottom right

        // Or rotate around the middle of the hypotenuse
        if(getOrientation().equals(new Angle(0))) {

            lineList.add(new LineSegment(v1, v2));
            lineList.add(new LineSegment(v2, v3));
            lineList.add(new LineSegment(v3, v1));            
        }
        
        else if(getOrientation().equals(new Angle(90))) {
            lineList.add(new LineSegment(v1, v3));
            lineList.add(new LineSegment(v4, v1));
            lineList.add(new LineSegment(v3, v4));            

        }
        
        else if(getOrientation().equals(new Angle(180))) {
            lineList.add(new LineSegment(v3, v4));
            lineList.add(new LineSegment(v4, v2));
            lineList.add(new LineSegment(v2, v3));            
        }
        
        else if(getOrientation().equals(new Angle(270))) {
            lineList.add(new LineSegment(v1, v2));
            lineList.add(new LineSegment(v4, v1));
            lineList.add(new LineSegment(v2, v4));            
        }
        

        return lineList;
    }


    public String getType() {
        return "TriangleBumper";
    }
    

    public void trigger() {
        board.activateTriggers(this);
        
    }

    public void action() {
        // no action
        
    }

    public Board getBoard() {
        return this.board;
    }
    
    
    @Override public int hashCode() {
        return getName().hashCode();
    }

    @Override 
    public boolean equals(Object obj) {
        if(!(obj instanceof TriangleBumper)) {
            return false;
        }
        TriangleBumper that = (TriangleBumper) obj;
        return (getWidth() == that.getWidth() && 
                getHeight() == that.getHeight() &&
                getLocation().equals(that.getLocation()) &&
                getOrientation().equals(that.getOrientation())
                );
    }

    @Override public String toString() {
        return "Triangle Bumper at " + this.getLocation() + " with orientation " + this.getOrientation() + " and leg length " +  this.getWidth();
    }

}
