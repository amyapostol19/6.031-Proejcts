package warmup;

import java.util.ArrayList;
import java.util.List;

import physics.LineSegment;

public class Wall {

    private final List<LineSegment> walls;
    
    public Wall(List<LineSegment> walls) {
        this.walls = walls;
        
    }
    
    public List<LineSegment> getWalls() {
        List<LineSegment> wallsCopy = new ArrayList<LineSegment>(this.walls);
        return wallsCopy;
    }
    
   
}
