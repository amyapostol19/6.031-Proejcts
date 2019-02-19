package flingball;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author amya
 * A mutable datatype representing the keypresses on the board	
 */
public class KeyPress {
	
	//fields
	private final Integer keyEvent;
	private final String keyName;
	private final String action; //keyUp or keyDown
	private final List<Gadget> triggering;
	
	/*
	 * Abstraction Function:
	 * 		AF(keyEvent, keyName, action, triggering) TODO
	 * 
	 * Rep Invariant:
	 * 		TODO
	 * 
	 * Safety From Rep Exposure
	 * 		TODO
	 */
	
	/**
	 * Creator method for KeyPress
	 */
	public KeyPress() {
		//TODO creator method; below fields may need to be replaced or initialized in above fields
		keyEvent = 0;
		keyName = "";
		action = "";
		triggering = new ArrayList<>();
	}
	
	/**
	 * CheckRep Method
	 */
	public void checkRep() {
		assert keyEvent != null;
		assert keyName != null;
		assert action != null;
		assert triggering != null;
		
		assert triggering.size() > 0;
		//TODO finish checkRep
	}
	
	/**
	 * Trigger all of the gadgets in triggering
	 */
	public void trigger() {
		//TODO
	}
	
}
