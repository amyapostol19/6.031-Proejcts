package flingball;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import edu.mit.eecs.parserlib.ParseTree;
import edu.mit.eecs.parserlib.Parser;
import edu.mit.eecs.parserlib.UnableToParseException;
import edu.mit.eecs.parserlib.Visualizer;

/**
 * An immutable parser to parse Flingball board files.
 */
public class BoardParser {
 // the nonterminals of the grammar
    private enum BoardGrammar {
        BOARDFILE, OTHER, BOARD, GRAVITY, FRICTION1,FRICTION2, BALL, SQUAREBUMPER, CIRCLEBUMPER, TRIANGLEBUMPER,
        ABSORBER, FIRE, KEYUP, KEYDOWN, RIGHTFLIPPER, LEFTFLIPPER, PORTAL, INTEGER, FLOAT, NAME, KEY, WHITESPACE,
        BLANKLINE, COMMENT, SKIPPABLE, ORIENTATION
    }

    private static Parser<BoardGrammar> parser = makeParser();
    
    /**
     * Compile the grammar into a parser.
     * 
     * @param grammarFilename <b>Must be in this class's Java package.</b>
     * @return parser for the grammar
     * @throws RuntimeException if grammar file can't be read or has syntax errors
     */
    private static Parser<BoardGrammar> makeParser() {
        try {
            // read the grammar as a file, relative to the project root.
            // final File grammarFile = new File("src/flingball/Board.g");
            // return Parser.compile(grammarFile, BoardGrammar.BOARDFILE);
            
            // read the grammar as a "classpath resource", which would allow this code 
            // to be packed up in a jar and still be able to find its grammar file
            // (see http://www.javaworld.com/article/2077352/java-se/smartly-load-your-properties.html
            //  for a discussion of classpath resources):
             final InputStream grammarStream = BoardParser.class.getResourceAsStream("Board.g");
             return Parser.compile(grammarStream, BoardGrammar.BOARDFILE);

        // Parser.compile() throws two checked exceptions.
        // Translate these checked exceptions into unchecked RuntimeExceptions,
        // because these failures indicate internal bugs rather than client errors
        } catch (IOException e) {
            throw new RuntimeException("can't read the grammar file", e);
        } catch (UnableToParseException e) {
            throw new RuntimeException("the grammar has a syntax error", e);
        }
    }
    
    /**
     * Parse a board file represented by its file path into an board.
     * @param filePath string with path to Board file
     * @return Board parsed from the string
     * @throws UnableToParseException if the string doesn't match the BoardGrammar grammar
     * @throws IOException if the board file cannot be read
     */
    public static Board parse(final String filePath) throws UnableToParseException, IOException {
        return parse(filePath, false);
    }

    /**
     * Parse a board file represented by its file path into an board.
     * @param string string with path to Board file
     * @param debugging true if debugging mode is one, false otherwise
     * @return Board parsed from the string
     * @throws UnableToParseException if the string doesn't match the BoardGrammar grammar
     * @throws IOException if the board file cannot be read
     */
    private static Board parse(final String filePath, final boolean debugging) throws UnableToParseException, IOException {
        // parse the example into a parse tree
        File file = new File(filePath);
        List<String> lines = Files.readAllLines(file.toPath());
        String string = "";
        for (String line:lines) {
            string+=line+'\n';
        }
        string = string.substring(0, string.length()-1);
        final ParseTree<BoardGrammar> parseTree = parser.parse(string);
        
        // make an AST from the parse tree
        final Board board = makeBoard(parseTree);
        
        if (debugging) {
            System.out.println("parse tree " + parseTree);
            Visualizer.showInBrowser(parseTree); // display the parse tree in a web browser
            System.out.println("AST " + board);
        }
        
        return board;
    }
    
    /**
     * Convert a parse tree into an abstract syntax tree.
     * 
     * @param parseTree constructed according to the grammar in Board.g
     * @return Board made according to the parseTree
     * @throws IOException 
     */
    private static Board makeBoard(final ParseTree<BoardGrammar> parseTree) throws IOException { 
        List<ParseTree<BoardGrammar>> children = parseTree.children();
        //Make board, we assume that the first line we encounter is the 
        //line describing the board
        List<ParseTree<BoardGrammar>> boardChildren = children.get(0).children();
        //Placeholders for Board Fields
        String boardName = "";
        double boardGravity = Board.DEFAULT_GRAVITY;
        double boardFriction1 = Board.DEFAULT_FRICTION;
        double boardFriction2 = Board.DEFAULT_FRICTION;
        for (ParseTree<BoardGrammar> elts: boardChildren) {
            //board ::= 'board' 'name' '=' NAME ('gravity' '=' gravity)? ('friction1' '=' friction1)? ('friction2' '=' friction2)?;
            if (elts.name().equals(BoardGrammar.NAME)) {
                boardName = elts.text();
            }
            else if (elts.name().equals(BoardGrammar.GRAVITY)) {
                //gravity ::= FLOAT;
                boardGravity = Double.valueOf(elts.childrenByName(BoardGrammar.FLOAT).get(0).text());
            }
            else if (elts.name().equals(BoardGrammar.FRICTION1)) {
                //friction1 ::= FLOAT;
                boardFriction1 = Double.valueOf(elts.childrenByName(BoardGrammar.FLOAT).get(0).text());
            }
            else if (elts.name().equals(BoardGrammar.FRICTION2)) {
                //friction2 ::= FLOAT;
                boardFriction2 = Double.valueOf(elts.childrenByName(BoardGrammar.FLOAT).get(0).text());
            }
            else throw new AssertionError("should never get here");
        }
        Board board =  new Board(boardName,boardGravity,boardFriction1,boardFriction2);
        
        //now add balls, gadgets and fires
        for (ParseTree<BoardGrammar> otherLine: children.subList(1, children.size())) {
            ParseTree<BoardGrammar> other = otherLine.children().get(0);
            if (other.name().equals(BoardGrammar.BALL)) {
                //ball ::=  'ball' 'name' '=' NAME 'x' '=' FLOAT 'y' '=' FLOAT 'xVelocity' '=' FLOAT 'yVelocity' '=' FLOAT;
                List<ParseTree<BoardGrammar>> ballFields = other.children();
                String ballName = ballFields.get(0).text();
                double xPos = Double.valueOf(ballFields.get(1).text());
                double yPos = Double.valueOf(ballFields.get(2).text());
                double xVel = Double.valueOf(ballFields.get(3).text());
                double yVel = Double.valueOf(ballFields.get(4).text());
                board.addBall(new Ball(ballName,xPos,yPos,xVel,yVel));
            }
            else if (other.name().equals(BoardGrammar.SQUAREBUMPER)) {
                //squareBumper ::= 'squareBumper' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER;
                List<ParseTree<BoardGrammar>> bumperFields = other.children();
                String bumperName = bumperFields.get(0).text();
                int xPos = Integer.valueOf(bumperFields.get(1).text());
                int yPos = Integer.valueOf(bumperFields.get(2).text());
                board.addGadget(new SquareBumper(bumperName,xPos,yPos));
            }
            else if (other.name().equals(BoardGrammar.CIRCLEBUMPER)) {
                //circleBumper ::= 'circleBumper' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER;
                List<ParseTree<BoardGrammar>> bumperFields = other.children();
                String bumperName = bumperFields.get(0).text();
                int xPos = Integer.valueOf(bumperFields.get(1).text());
                int yPos = Integer.valueOf(bumperFields.get(2).text());
                board.addGadget(new CircleBumper(bumperName,xPos,yPos));
                
            }
            else if (other.name().equals(BoardGrammar.TRIANGLEBUMPER)) {
                //triangleBumper ::= 'triangleBumper' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER ('orientation' '=' ORIENTATION)?;
                // ORIENTATION ::= '0' | '90' | '180' | '270';
                List<ParseTree<BoardGrammar>> bumperFields = other.children();
                String bumperName = bumperFields.get(0).text();
                int xPos = Integer.valueOf(bumperFields.get(1).text());
                int yPos = Integer.valueOf(bumperFields.get(2).text());
                //Default orientation
                int orientation = 0;
                if (bumperFields.size()==4) {
                    orientation = Integer.valueOf(bumperFields.get(3).text());
                }
                board.addGadget(new TriangleBumper(bumperName,xPos,yPos,orientation));
                
            }
            else if (other.name().equals(BoardGrammar.ABSORBER)) {
                //absorber ::= 'absorber' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER 'width' '=' INTEGER 'height' '=' INTEGER;
                List<ParseTree<BoardGrammar>> absorberFields = other.children();
                String absorberName = absorberFields.get(0).text();
                int xPos = Integer.valueOf(absorberFields.get(1).text());
                int yPos = Integer.valueOf(absorberFields.get(2).text());
                int width = Integer.valueOf(absorberFields.get(3).text());
                int height = Integer.valueOf(absorberFields.get(4).text());
                board.addGadget(new Absorber(absorberName,xPos,yPos,width,height));
            }
            else if (other.name().equals(BoardGrammar.FIRE)) {
                //fire ::= 'fire' 'trigger' '=' NAME 'action' '=' NAME;
                String triggerGadget = other.children().get(0).text();
                String actionGadget = other.children().get(1).text();
                board.addTrigger(triggerGadget, actionGadget);
            }
            else if (other.name().equals(BoardGrammar.KEYUP)) {
                //keyup ::= 'keyup' 'key' '=' KEY 'action' '=' NAME;
               String keyTrigger = other.children().get(0).text();
               String triggeredGadget = other.children().get(1).text();
               board.addKeyReleaseTrigger(keyTrigger, triggeredGadget);                
            }
            else if (other.name().equals(BoardGrammar.KEYDOWN)) {
                //keydown ::= 'keydown' 'key' '=' KEY 'action' '=' NAME;
                String keyTrigger = other.children().get(0).text();
                String triggeredGadget = other.children().get(1).text();
                board.addKeyPressTrigger(keyTrigger, triggeredGadget);   
            }
            else if (other.name().equals(BoardGrammar.RIGHTFLIPPER)) {
                // rightFlipper::= 'rightFlipper' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER ('orientation' '=' ORIENTATION)?;
                // ORIENTATION ::= '0' | '90' | '180' | '270';
                List<ParseTree<BoardGrammar>> flipperFields = other.children();
                String flipperName = flipperFields.get(0).text();
                int xPos = Integer.valueOf(flipperFields.get(1).text());
                int yPos = Integer.valueOf(flipperFields.get(2).text());
                //Default orientation
                int orientation = 0;
                if (flipperFields.size()==4) {
                    orientation = Integer.valueOf(flipperFields.get(3).text());
                }
                board.addGadget(new Flipper(flipperName,xPos,yPos,orientation, "right"));
                
            }
            else if (other.name().equals(BoardGrammar.LEFTFLIPPER)) {
                // leftFlipper::= 'leftFlipper' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER ('orientation' '=' ORIENTATION)?;
                // ORIENTATION ::= '0' | '90' | '180' | '270';
                List<ParseTree<BoardGrammar>> flipperFields = other.children();
                String flipperName = flipperFields.get(0).text();
                int xPos = Integer.valueOf(flipperFields.get(1).text());
                int yPos = Integer.valueOf(flipperFields.get(2).text());
                //Default orientation
                int orientation = 0;
                if (flipperFields.size()==4) {
                    orientation = Integer.valueOf(flipperFields.get(3).text());
                }
                board.addGadget(new Flipper(flipperName,xPos,yPos,orientation, "left"));
                
            }
            else if (other.name().equals(BoardGrammar.PORTAL)) {
                //portal::= 'portal' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER ('otherBoard' '=' NAME)? 'otherPortal' '=' NAME;
                List<ParseTree<BoardGrammar>> flipperFields = other.children();
                String flipperName = flipperFields.get(0).text();
                int xPos = Integer.valueOf(flipperFields.get(1).text());
                int yPos = Integer.valueOf(flipperFields.get(2).text());
                if (flipperFields.size()==4) {
                    board.addGadget(new Portal(flipperName,xPos,yPos,board,flipperFields.get(3).text()));
                }else {
                    board.addGadget(new Portal(flipperName,xPos,yPos,board,flipperFields.get(4).text(),flipperFields.get(3).text()));
                }
                    
            }
            else throw new AssertionError("should never get here");
        }        
        
        return board;
    }
    
}

