/**
 * 
 */
package coed.collab.server;


/**
 * Class simulating  a stack of CoedFileChange objects, i.e. objects that
 * contain information about changes occured in a given file.
 * @author Neobi
 *
 */
public class ChangeStack {
	
	private CoedFileChange[] changes;
	private int top = -1; //pointer to the top of the stack
	
	public ChangeStack(int initCapacity){
		changes = new CoedFileChange[initCapacity];
		top = -1;
	}
	
	public void pushChange(CoedFileChange change){
		top++;
		changes[top] = change;
	}
	
	public CoedFileChange popChange(){
		if (top >= 0){
		    top--;
		    return changes[top+1];
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
		while ((changes[pos].getSessionID() != ID) && (pos > 0))
			pos --;
		if (changes[pos].getSessionID() == ID) return pos;
			else return -1;
	}
	
}
