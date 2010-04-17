/**
 * 
 */
package coed.collab.server;

import java.util.Vector;

import coed.base.data.TextModification;



/**
 * Class simulating  a queue of CoedFileChange objects, i.e. objects that
 * contain information about changes occured in a given file.
 * @author Neobi
 *
 */
public class ChangeQueue{
	
	private ChangeContainer<CoedFileChange> changes;
	private int top = 0; //pointer to the top (head of queue)
	private ServerFile file;
	
	public ChangeQueue(ServerFile file){
		changes = new ChangeContainer<CoedFileChange>();
		top = 0;
		this.file = file;
	}
	
	/**
	 * Synchronized acces method for adding a new CoedFileChange object to the end of the queue
	 * @param change the change to be added
	 */
	public synchronized void enQueueChange(CoedFileChange change){
		changes.add(top,change);
		top++;
	}
	
	/**
	 * Synchronized acces method for deleting a CoedFileChange object from the head of the queue
	 * @return - The CoedFileChange object, or null if empty queue
	 */
	public synchronized CoedFileChange deQueueChange(){
		if (top > 0){
		    top--;
		    return changes.remove(top);
		}
		else return null;
	}
	
	public TextModification[] getChangesFor(Session s){
		assert s != null;
		
		Vector<TextModification> vect = new Vector<TextModification>();
		int  k = 0;
		//System.out.println("changepointer for session:"+ file.getChangePointer(s)+ " x "+s.getUserName()+"x");
		for (int i=file.getChangePointer(s); i<top; i++){
			TextModification chg = getChangeAt(i);
			assert chg != null && chg.getMetaInfo() != null;
			
			//System.out.println("metainfo:"+chg.toString());
			if (!( chg.getMetaInfo().equals(s.getUserName()))){
				//System.out.println("change:"+chg.toString());
				vect.add(k,chg);
				k++;
			}
		}
		
		TextModification[] ret = new TextModification[k];
		vect.toArray(ret);
		
		
		return ret;
		
	}
	
	public TextModification getChangeAt(int index){
		return changes.elementAt(index).getMod();
	}
	
	public int getTopIndex(){
		return this.top;
	}
	
	
}
