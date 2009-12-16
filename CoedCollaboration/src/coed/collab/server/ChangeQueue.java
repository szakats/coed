/**
 * 
 */
package coed.collab.server;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.LinkedList;

import coed.base.data.TextModification;



/**
 * Class simulating  a queue of CoedFileChange objects, i.e. objects that
 * contain information about changes occured in a given file.
 * @author Neobi
 *
 */
public class ChangeQueue{
	
	class CoedFileChange{
		
		private Date time;
		private String userName;
		private TextModification mod;
		
		public CoedFileChange(Date time, String userName, TextModification mod) {
			this.time = time;
			this.userName = userName;
			this.mod = mod;
		}
		
		public TextModification getMod() {
			return mod;
		}
		public void setMod(TextModification mod) {
			this.mod = mod;
		}
		public Date getTime() {
			return time;
		}
		public void setTime(Date time) {
			this.time = time;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		
	}
	
	private ArrayDeque<CoedFileChange> changes;
	private int top = -1; //pointer to the tail of the queue
	private ServerFile file;
	
	public ChangeQueue(ServerFile file){
		changes = new ArrayDeque<CoedFileChange>();
		top = -1;
		this.file = file;
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
	
	public TextModification[] getChangesFor(Session s){
		TextModification[] ret = new TextModification[changes.size()-file.getChangePointer(s)];
		//TODO : this shit:P
		return ret;
		
	}
	
	
}
