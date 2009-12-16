/**
 * 
 */
package coed.collab.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import coed.base.data.TextModification;

/**
 * Class for representing a file on a server. It contains the locks, listeners,
 * and a ChangeQueue with all recent changes on this file. Also contains the file itself,
 * the current live version.
 * 
 * !!!   IMPORTANT NOTE   !!!
 * When creating files phisically on the server, we replace the '\' character in the
 * filepath with the '.' character.
 * @author Neobi
 *
 */
public class ServerFile {

	private String path;
	private StringBuffer contents = new StringBuffer();
	
	/**
	 * HashMap containing the session registered for this serverfile
	 * and the corresponding pointers (index into ChangeQueue) for
	 * the change that was last sent to this session. (0 pointer value
	 * means an up-to-date file).
	 */
	
	private HashMap<Session,Integer> sessions; //TODO: possible overhead when accessing
	private ChangeQueue queue;
	//TODO: listeners and locks come here
	
	public ServerFile(String path){
		this.path = path;
		this.queue = new ChangeQueue(this);
		sessions = new HashMap<Session,Integer>();
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
			sessions.put(s,new Integer(0));
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
	
	public void addChange(TextModification change){
		//TODO insert the change into StringBuffer
	}
	
}
