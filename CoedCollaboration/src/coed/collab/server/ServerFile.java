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
	 * the first change that was not sent to this session. (pointer value equal with
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
	   this.contents.delete(0, this.contents.capacity());
	   this.contents.append(contents);
	}
	
	/**
	 * Adds a new session to the sessions registered for this ServerFile. 
	 * Also, sets the change pointer pointer to topIndex. (i.e. the session has the 
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
		//update the change offset, and the offsets of all other changes
		//that will be affected by this change
		updateChangeOffset(change,s);
		//put the change into the ChangeQueue
		queue.enQueueChange(new CoedFileChange(change,new Date()));
		//insert the change into the file
		contents.insert(change.getOffset(),change.getText());
	}
	
	public String getCurrentContents(){
		return this.contents.substring(0);
	}
	
	public boolean RequestLock(TextPortion text, Session s){
		int startIndex = getChangePointer(s);
		TextModification chg;
		
		//calculate global offset
		for (int i=startIndex; i<queue.getTopIndex(); i++){
			chg = queue.getChangeAt(i);
			if (chg.getOffset() < text.getOffset()){
				if ( ! (s.getUserName().equals(chg.getMetaInfo()) ))
					text.setOffset(text.getOffset()+chg.getLength());
			}
		}
		
		// now we have the correct offset in the TextModification, see if we can place lock
		boolean canLock = true;
		ServerLock myLock = new ServerLock(text,new Date(),s);
		ServerLock lock;
		int i = 0;
		while ((i<locks.size()) && (canLock)){
			lock = locks.get(i);
			if (lock.overlaps(myLock)){
				//overlapping locks
				if ( ! lock.getSession().equals(s))
					//and not from the same user
					canLock = false;
			}
			i++;
		}
		
		if (! canLock)
			return false;
		else{
			//we can put the lock
			locks.add(myLock);
			return true;
		}
	}
	
	public void ReleaseLock(TextPortion portion, Session s){
		int startIndex = getChangePointer(s);
		TextModification chg;
		
		//calculate global offset
		for (int i=startIndex; i<queue.getTopIndex(); i++){
			chg = queue.getChangeAt(i);
			if (chg.getOffset() < portion.getOffset()){
				if ( ! (s.getUserName().equals(chg.getMetaInfo()) ))
					portion.setOffset(portion.getOffset()+chg.getLength());
			}
		}
		
		//search for this lock, and delete id
		boolean found = false;
		int i = 0;
		ServerLock lock;
		while ((i<locks.size()) && (! found)){
			lock = locks.get(i);
			if ((lock.getSession().equals(s)) && (lock.getOffset() == portion.getOffset())
					&& (lock.getLength() == portion.getLength())){
				locks.remove(i);
				found = true;
			}
			i++;
		}
		
	}
	
	
	/**
	 * This private method will be used to convert the offset 
	 * of the textmodification given as parameter to the corresponding
	 * global offset, with respect to the version on the server.
	 */
	private void updateChangeOffset(TextModification text, Session s){
		
		int startIndex = getChangePointer(s);
		TextModification chg;
		/**
		 * for all the changes that were not received by this session
		 * verify if it modifies its offset (i.e. some other change was written
		 * to an offset less then this change. In this case, we should adjust 
		 * our offset)
		 */
		for (int i=startIndex; i<queue.getTopIndex(); i++){
			chg = queue.getChangeAt(i);
			if (chg.getOffset() < text.getOffset()){
				if ( ! (text.getMetaInfo().equals(chg.getMetaInfo()) ))
					text.setOffset(text.getOffset()+chg.getLength());
			}
		}
		/**
		 * Now the offset is relative to the version on the server. 
		 * We should update now all offsets of those modifications 
		 * in the ChangeQueue, that were issued on an offset after this
		 * current offset. (i.e. all the modifications that are after 
		 * the current modification with respect to the offsets should be 
		 * shifted with the length of this current modification)
		 */
		//System.out.println("updated offset is:"+text.getOffset().toString());
		for (int i=0; i<queue.getTopIndex(); i++){
			chg = queue.getChangeAt(i);
			//System.out.println("chg is here "+chg.getOffset().toString()+chg.getText());
			if (chg.getOffset() >= text.getOffset()){
				//probably the same if with the users?
					chg.setOffset(chg.getOffset() + text.getLength());
				
				//System.out.println("chg is hererrrrr "+chg.getOffset().toString());
			}
			
		}
		
		/**
		 * now we should update in the same manner those locks' offsets
		 * that are after this textmodification.
		 */
		ServerLock lock;
		for ( int i=0; i<locks.size(); i++){
			lock = locks.get(i);
			if (lock.getOffset() > text.getOffset())
				lock.setOffset(lock.getOffset()+text.getLength());
			//! note that we do not verify here if the same user issued the lock
			//because even in this case, the offsets should be shifted on the SERVER!!!
			System.out.println("lock at:"+lock.getOffset()+" wiht length: "+lock.getLength());
		}
		
		
		//for (int i=0; i<queue.getTopIndex(); i++)
		//	System.out.println("queue["+i+"] = "+queue.getChangeAt(i).getOffset().toString());
	}
	
}
