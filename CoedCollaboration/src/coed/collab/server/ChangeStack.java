/**
 * 
 */
package coed.collab.server;

import java.util.Vector;


/**
 * Class simulating  a stack of CoedFileChange objects, i.e. objects that
 * contain information about changes occured in a given file.
 * @author Neobi
 *
 */
public class ChangeStack {
	
	public static final int MAX_CAPACITY = 300;
	private Vector<CoedFileChange> changes;
	private int top = -1; //pointer to the top of the stack
	
	public ChangeStack(int initCapacity){
		changes = new Vector<CoedFileChange>(initCapacity);
		top = -1;
	}
	
	public void pushChange(CoedFileChange change){
		changes.add(change);
		top++;
	}
	
	public CoedFileChange popChange(){
		if (top >= 0){
		    top--;
		    return changes.remove(top+1);
		}
		else return null;
	}
	
	/**
	 * Searches for a change given by it's SessionID
	 * @param ID SessionID of the change
	 * @return position if found, -1 if not found
	 */
	
	public int getPosition(int ID){
		int pos = top;
		while ((changes.get(pos).getSessionID() != ID) && (pos > 0))
			pos --;
		if (changes.get(pos).getSessionID() == ID) return pos;
			else return -1;
	}
	
}
