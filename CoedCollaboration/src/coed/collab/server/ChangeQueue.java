/**
 * 
 */
package coed.collab.server;

import java.util.LinkedList;



/**
 * Class simulating  a stack of CoedFileChange objects, i.e. objects that
 * contain information about changes occured in a given file.
 * @author Neobi
 *
 */
public class ChangeQueue{
	
	public static final int MAX_CAPACITY = 300;
	private LinkedList<CoedFileChange> changes;
	private int top = -1; //pointer to the top of the stack
	
	public ChangeQueue(int initCapacity){
		changes = new LinkedList<CoedFileChange>();
		top = -1;
	}
	
	public synchronized void pushChange(CoedFileChange change){
		changes.add(change);
		top++;
	}
	
	public synchronized CoedFileChange popChange(){
		if (top >= 0){
		    top--;
		    return changes.removeFirst();
		}
		else return null;
	}
	
	/**
	 * Searches for a change given by it's SessionID
	 * @param ID SessionID of the change
	 * @return position if found, -1 if not found
	 */
	
	public synchronized int getPosition(String userName){
		int pos = top;
		while ((changes.get(pos).getUserName() != userName) && (pos > 0))
			pos --;
		if (changes.get(pos).getUserName() == userName) return pos;
			else return -1;
	}
	
}
