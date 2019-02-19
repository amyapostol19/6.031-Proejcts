package flingball;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import edu.mit.eecs.parserlib.UnableToParseException;

/**
 * Console interface to the flingball system
 *
 */
public class Main{
    
    public static void main(String[] args) throws IOException, UnableToParseException, Exception {
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Optional<String> currentBall = Optional.empty();
        List<Optional<String>> currentBumpers = new ArrayList<Optional<String>>();
        List<Optional<String>> currentAbsorbers = new ArrayList<Optional<String>>();
        List<Optional<String>> currentFires = new ArrayList<Optional<String>>();
        
        while(true) {
            System.out.println("> ");
            final String input = in.readLine();
            
            if (input.isEmpty()) {
                System.exit(0);
            }
            
            // maybe eventually we have commands that generate our flingboard. But for now all commands will just be adding to flingboard
            try {
                // name all parameters needed for board
                final String output;
                
                    if (input.startsWith("ball")) {
                        output = FlingballParser.parse(input).toString();
                        currentBall = Optional.of(output);
                        System.out.println(output);
                    } else if (input.contains("Bumper")) {
                        output = FlingballParser.parse(input).toString();
                        currentBumpers.add(Optional.of(output));
                        System.out.println(output);
                    } else if (input.startsWith("absorber")) {
                        output = FlingballParser.parse(input).toString();
                        currentAbsorbers.add(Optional.of(output));
                    } else if (input.startsWith("fire")) {
                        output = FlingballParser.parse(input).toString();
                        currentFires.add(Optional.of(output));
                    }
                
            } catch (NoSuchElementException nse) {
                //current is empty
                System.out.println("must enter input");
            } catch (RuntimeException re) {
                System.out.println(re.getMessage());
            }
        }
    }
    
    //list different commands
    
    //Interface of board
}