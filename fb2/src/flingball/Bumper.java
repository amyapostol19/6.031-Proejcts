package flingball;

/**
 * A mutable data type representing a Flingball bumper gadget.
 */
public interface Bumper extends Gadget {
    
    public static final double DEFAULT_REFLECTION_COEFF = 1.0;

    /** @return reflection coefficient of this bumper */
    public double getReflectionCoeff();
    
    @Override
    /** Bumpers do not do anything when responding to a trigger. */
    public void respondToTrigger();
   
}
