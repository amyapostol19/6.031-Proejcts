
//changed board so now ball can be anywhere in the file and there also can be multiple balls
Board ::= (boardObject | ball | squareBumper | circleBumper | triangleBumper | absorber | fire | comment)*;

@skip whitespace{
    boardObject ::= 'board name''='name ('gravity''='gravity)? ('friction1''='friction1 'friction2''='friction2)?;
    ball ::= 'ball name' '='name 'x' '='xLocation 'y' '='yLocation 'xVelocity' '='xVelocity 'yVelocity' '='yVelocity;
    squareBumper ::= 'squareBumper name''='name 'x''='xLocation 'y''='yLocation;
    circleBumper ::= 'circleBumper name''='name 'x''='xLocation 'y''='yLocation;
    triangleBumper ::= 'triangleBumper name''='name 'x''='xLocation 'y''='yLocation 'orientation''='orientation;
    absorber ::= 'absorber name''='name 'x''='xLocation 'y''='yLocation 'width''='width 'height''='height;
    
    fire ::= 'fire trigger''='trigger 'action''='action;
    
    
}

// add in different values for number
xLocation ::= number;
yLocation ::= number;
xVelocity ::= number;
yVelocity ::= number;
orientation ::= number;
width ::= number;
height ::= number;
gravity ::= number;
friction1 ::= number;
friction2 ::= number;


trigger ::= name;
action ::= name;

number ::= '-'?[0-9]+('.'[0-9]+)?;
name ::= [A-Za-z0-9]+;
whitespace ::= [ \t\r\n]+;
toSkip ::= [whitespace | comment]+;

comment ::= '#'[^\n]*;