/**
 * 
 */
package coed.collab.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import coed.base.data.TextModification;
import coed.base.data.TextPortion;

/**
 * Class for representing a file on a server. It contains the locks, listeners,
 * and a ChangeQueue with all recent changes on this file. Also contains the file itself,
 * the current live version.
 * 
 * @author Neobi
 *
 */
public class ServerFile {

	private String path;
	/**
	 * StringBuffer containing the contents of the file.
	 */
	private StringBuffer contents = new StringBuffer();
	
	/**
	 * HashMap containing the session registered for this serverfile
	 * and the corresponding pointers (index into ChangeQueue) for
	 * the change that was last sent to this session. (pointer value equal with
	 * the top attribute of changeQueue means an up-to-date file).
	 */
	
	private HashMap<Session,Integer> sessions; 
	
	private ChangeQueue queue;
	
	/**
	 * Vector containing all the locks on this file
	 */
	private Vector<ServerLock> locks;
	
	public ServerFile(String path){
		this.path = path;
		this.queue = new ChangeQueue(this);
		sessions = new HashMap<Session,Integer>();
		locks = new Vector<ServerLock>();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public synchronized void changeContents(String contents) throws IOException{
	   
	}
	
	/**
	 * Adds a new session to the sessions registered for this ServerFile. 
	 * Also, sets the change pointer pointer to 0. (i.e. the session has the 
	 * up-to-date version).
	 * @param s Session
	 */
	public void addSession(Session s){
		if (! sessions.containsKey(s))
			sessions.put(s,new Integer(queue.getTopIndex()));
	}
	
	public void removeSession(Session s){
		if (sessions.containsKey(s))
			sessions.remove(s);
	}
	
	public int getNrOfSessions(){
		return sessions.size();
	}
	
	public void setChangePointer(Session s, int p){
		sessions.put(s,new Integer(p));
	}
	
	public int getChangePointer(Session s){
		return ((Integer)sessions.get(s)).intValue();
	}
	
	public void addChange(TextModification change, Session s){
		//put the change into the ChangeQueue
		//TODO : algorithm for updating the offset!!!
		queue.enQueueChange(new CoedFileChange(change,new Date()));
		//insert the change into the file
		contents.insert(change.getOffset(),change.getText());
		//TODO update all the offsets after this change!!!
	}
	
	public String getCurrentContents(){
		return this.contents.substring(0);
	}
	
	public boolean RequestLock(TextPortion portion, Session s){
		//TODO: algorithm to compute the correct offset
		return true;
	}
	
	public void ReleaseLock(TextPortion portion){
		//TODO: algorithm to compute the correct offset
		
	}
	
}
