/**
 * 
 */
package coed.collab.server;

import java.util.LinkedList;



/**
 * Class simulating  a queue of CoedFileChange objects, i.e. objects that
 * contain information about changes occured in a given file.
 * @author Neobi
 *
 */
public class ChangeQueue{
	
	private LinkedList<CoedFileChange> changes;
	private int top = -1; //pointer to the tail of the queue
	
	public ChangeQueue(){
		changes = new LinkedList<CoedFileChange>();
		top = -1;
	}
	
	/**
	 * Synchronized acces method for adding a new CoedFileChange object to the end of the queue
	 * @param change the change to be added
	 */
	public synchronized void enQueueChange(CoedFileChange change){
		changes.add(change);
		top++;
	}
	
	/**
	 * Synchronized acces method for deleting a CoedFileChange object from the head of the queue
	 * @return - The CoedFileChange object, or null if empty queue
	 */
	public synchronized CoedFileChange deQueueChange(){
		if (top >= 0){
		    top--;
		    return changes.removeFirst();
		}
		else return null;
	}
	
	/**
	 * Searches for a change given by it's userName field
	 * @param userName the userName of the user who made the change
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
