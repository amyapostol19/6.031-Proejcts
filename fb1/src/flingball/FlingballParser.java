package flingball;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.eecs.parserlib.ParseTree;
import edu.mit.eecs.parserlib.Parser;
import edu.mit.eecs.parserlib.UnableToParseException;
import physics.Angle;
import physics.Circle;
import physics.Vect;

public class FlingballParser {


    //    private static final int GAMEBOARD_SIZE = 20;
          private static final int PIXELS_PER_L = 20;
    //    private static final int DRAWING_AREA_SIZE_IN_PIXELS = GAMEBOARD_SIZE * PIXELS_PER_L;

    // the nonterminals of the grammar
    private enum FlingballGrammar {
        BOARD, BOARDOBJECT, BALL, SQUAREBUMPER, CIRCLEBUMPER, TRIANGLEBUMPER, ABSORBER, FIRE, 
        XLOCATION, YLOCATION, XVELOCITY, YVELOCITY, ORIENTATION, WIDTH, HEIGHT, FRICTION1, FRICTION2,
        ACTION, TRIGGER, NUMBER, NAME, WHITESPACE, COMMENT, GRAVITY, TOSKIP
    }

    private static Parser<FlingballGrammar> parser = makeParser();

    private static Parser<FlingballGrammar> makeParser() {
        try {
            // read the grammar as a file, relative to the project root.
            final File grammarFile = new File("src/flingball/Flingball.g");
            return Parser.compile(grammarFile, FlingballGrammar.BOARD);
        } catch (IOException e) {
            throw new RuntimeException("can't read the grammar file", e);
        } catch (UnableToParseException e) {
            e.printStackTrace();
            throw new RuntimeException("the grammar has a syntax error", e);
        }
    }

    /**
     * Parse a string into an board.
     * @param input : file to parse (inputed as a string)
     * @return Board parsed from the string
     * @throws UnableToParseException if the string doesn't match the Flingball grammar
     */
    public static Board parse(final String input) throws UnableToParseException, FileNotFoundException, IOException,
    EmptyFileException {

        List<Ball> balls = new ArrayList<>();
        Map<Gadget, Gadget> triggers = new HashMap<Gadget, Gadget>();  
        double gravity = 25.0;
        ArrayList<Double> frictionList = new ArrayList<Double>();
        frictionList.add(0.025);
        frictionList.add(0.025);

        Board board = null;

        BufferedReader reader = new BufferedReader(new FileReader(new File(input)));

        // parse the example into a parse tree
        ParseTree<FlingballGrammar> parseTree;
        try {
            String string = reader.readLine();

            if (string == null) {
                reader.close();
                throw new EmptyFileException("The file has no contents");
            }

            while (string != null) {
                //System.out.println("string " + string);
                parseTree = parser.parse(string);
                string = reader.readLine();


                for (ParseTree<FlingballGrammar> child : parseTree.children()) {
                    switch(child.name()) {
                    case BOARDOBJECT: {
                        final String name = child.childrenByName(FlingballGrammar.NAME).get(0).text();
                                              
                        final List<ParseTree<FlingballGrammar>> g = child.childrenByName(FlingballGrammar.GRAVITY);
                        if(g.size() != 0) {
                            gravity = Double.parseDouble(child.childrenByName(FlingballGrammar.GRAVITY).get(0).text());
                        } 
                        
                        
                        final List<ParseTree<FlingballGrammar>> f1 = child.childrenByName(FlingballGrammar.FRICTION1);
                        if(f1.size() != 0) {
                            final double friction1 = Double.parseDouble(child.childrenByName(FlingballGrammar.FRICTION1).get(0).text());
                            final double friction2 = Double.parseDouble(child.childrenByName(FlingballGrammar.FRICTION2).get(0).text());
                            frictionList = new ArrayList<Double>(Arrays.asList(friction1, friction2));
                        } 
                       
                        board = new Board(name, balls, new ArrayList<>(), gravity, frictionList, triggers);
                        break;
                    }
                    
                    case BALL:{
                        // make a Ball from the parse tree
                        final String name = child.childrenByName(FlingballGrammar.NAME).get(0).text();
                        final double xLocation = Double.parseDouble(child.childrenByName(FlingballGrammar.XLOCATION).get(0).text());
                        final double yLocation = Double.parseDouble(child.childrenByName(FlingballGrammar.YLOCATION).get(0).text());
                        final double xVelocity = Double.parseDouble(child.childrenByName(FlingballGrammar.XVELOCITY).get(0).text());
                        final double yVelocity = Double.parseDouble(child.childrenByName(FlingballGrammar.YVELOCITY).get(0).text());
                        final Circle ballCircle = new Circle(new Vect(xLocation*PIXELS_PER_L, yLocation*PIXELS_PER_L), 0.25*PIXELS_PER_L);
                        final Vect velocity = new Vect(xVelocity*PIXELS_PER_L, yVelocity*PIXELS_PER_L);
                        final Ball ball = new Ball(ballCircle, velocity, name);

                        board.addBall(ball);
                        break;
                    }

                    case SQUAREBUMPER:{
                        final String name = child.childrenByName(FlingballGrammar.NAME).get(0).text();
                        final double xLocation = Double.parseDouble(child.childrenByName(FlingballGrammar.XLOCATION).get(0).text());
                        final double yLocation = Double.parseDouble(child.childrenByName(FlingballGrammar.YLOCATION).get(0).text());
                        final Vect location = new Vect(xLocation*PIXELS_PER_L, yLocation*PIXELS_PER_L);
                        final SquareBumper sBumper = new SquareBumper(board, 1*PIXELS_PER_L, location, new Angle(0), name);

                        board.addGadget(sBumper);
                        break;
                    }

                    case CIRCLEBUMPER:{
                        final String name = child.childrenByName(FlingballGrammar.NAME).get(0).text();
                        final double xLocation = Double.parseDouble(child.childrenByName(FlingballGrammar.XLOCATION).get(0).text());
                        final double yLocation = Double.parseDouble(child.childrenByName(FlingballGrammar.YLOCATION).get(0).text());
                        final Vect center = new Vect(xLocation*PIXELS_PER_L +.5*PIXELS_PER_L, yLocation*PIXELS_PER_L + .5*PIXELS_PER_L); //location is upper left corner of bounding box, not center of circle
                        final Circle circle = new Circle(center, 0.5*PIXELS_PER_L);
                        final CircleBumper cBumper = new CircleBumper(board, circle, name);

                        board.addGadget(cBumper);
                        break;  
                    }

                    case TRIANGLEBUMPER:{
                        final String name = child.childrenByName(FlingballGrammar.NAME).get(0).text();
                        final double xLocation = Double.parseDouble(child.childrenByName(FlingballGrammar.XLOCATION).get(0).text());
                        final double yLocation = Double.parseDouble(child.childrenByName(FlingballGrammar.YLOCATION).get(0).text());
                        final Vect location = new Vect(xLocation*PIXELS_PER_L, yLocation*PIXELS_PER_L);

                        final double degrees = Double.parseDouble(child.childrenByName(FlingballGrammar.ORIENTATION).get(0).text());
                        //                            final double radians = 2*Math.PI*(degrees/360.0);
                        final Angle orientation = new Angle(degrees);

                        final TriangleBumper tBumper = new TriangleBumper(board, 1*PIXELS_PER_L, location, orientation, name);

                        board.addGadget(tBumper);
                        break;
                    }

                    case ABSORBER:{
                        final String name = child.childrenByName(FlingballGrammar.NAME).get(0).text();
                        final double xLocation = Double.parseDouble(child.childrenByName(FlingballGrammar.XLOCATION).get(0).text());
                        final double yLocation = Double.parseDouble(child.childrenByName(FlingballGrammar.YLOCATION).get(0).text());
                        final int width = Integer.parseInt(child.childrenByName(FlingballGrammar.WIDTH).get(0).text());
                        final int height = Integer.parseInt(child.childrenByName(FlingballGrammar.HEIGHT).get(0).text());

                        final Vect location = new Vect(xLocation*PIXELS_PER_L, yLocation*PIXELS_PER_L);

                        final Absorber absorber = new Absorber(board, width*PIXELS_PER_L, height*PIXELS_PER_L, location, new Angle(0), name);

                        board.addGadget(absorber);
                        break;
                    }

                    case FIRE:{

                        final String source = child.childrenByName(FlingballGrammar.TRIGGER).get(0).text();
                        final String target = child.childrenByName(FlingballGrammar.ACTION).get(0).text();

                        //next four lines are hacky as shit need a better way to do this
                        Gadget sourceGadget = null;
                        Gadget targetGadget = null;

                        for (Gadget g:board.getGadgets()) {
                            if (g.getName() != null && g.getName().equals(source)) {
                                sourceGadget = g;
                            }
                            if (g.getName()!= null && g.getName().equals(target)) {
                                targetGadget = g;
                            }
                        }

                        if(sourceGadget != null && targetGadget != null) {
                            board.addTrigger(sourceGadget, targetGadget);
                        }
                    }

                    case COMMENT:{
                        continue;
                    }


                    default:
                        throw new AssertionError("should never get here in parse tree");
                    }
                }
            }

        } catch (UnableToParseException e) {
            e.printStackTrace();
            reader.close();
            throw new IllegalArgumentException("the input syntax is incorrect", e);
            
        }

        reader.close();

        return board;
    }

}
