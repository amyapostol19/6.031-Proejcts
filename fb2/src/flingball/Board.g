/* Copyright (c) 2017 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */

@skip skippable {
    boardFile ::= board (other)*;
    board ::= 'board' 'name' '=' NAME ('gravity' '=' gravity)? ('friction1' '=' friction1)? ('friction2' '=' friction2)?;
    gravity ::= FLOAT;
    friction1 ::= FLOAT;
    friction2 ::= FLOAT;
    other ::= (ball | squareBumper | circleBumper | triangleBumper | absorber | fire | keyup | keydown | leftFlipper | rightFlipper | portal);
    ball ::=  'ball' 'name' '=' NAME 'x' '=' FLOAT 'y' '=' FLOAT 'xVelocity' '=' FLOAT 'yVelocity' '=' FLOAT;
    squareBumper ::= 'squareBumper' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER;
    circleBumper ::= 'circleBumper' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER;
    triangleBumper ::= 'triangleBumper' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER ('orientation' '=' ORIENTATION)?;
    absorber ::= 'absorber' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER 'width' '=' INTEGER 'height' '=' INTEGER;
    fire ::= 'fire' 'trigger' '=' NAME 'action' '=' NAME;
    keyup ::= 'keyup' 'key' '=' KEY 'action' '=' NAME;
    keydown ::= 'keydown' 'key' '=' KEY 'action' '=' NAME;
    rightFlipper::= 'rightFlipper' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER ('orientation' '=' ORIENTATION)?;
    leftFlipper::= 'leftFlipper' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER ('orientation' '=' ORIENTATION)?;
    portal::= 'portal' 'name' '=' NAME 'x' '=' INTEGER 'y' '=' INTEGER ('otherBoard' '=' NAME)? 'otherPortal' '=' NAME;
    
}
INTEGER ::= [0-9]+;
FLOAT ::= '-'?([0-9]+'.'[0-9]*|'.'?[0-9]+);
NAME ::= [A-Za-z_][A-Za-z_0-9]*;
ORIENTATION ::= '0' | '90' | '180' | '270';
KEY ::=   [a-z] 
        | [0-9]
        | "shift" | "ctrl" | "alt" | "meta"
        | "space"
        | "left" | "right" | "up" | "down"
        | "minus" | "equals" | "backspace"
        | "openbracket" | "closebracket" | "backslash"
        | "semicolon" | "quote" | "enter"
        | "comma" | "period" | "slash";
whitespace ::= [ \t\r]+;
blankLine ::=  '\n';
comment ::= '#' [^#\n]* '\n';
skippable ::= blankLine | comment | whitespace;

