/**
 * 
 */
package coed.collab.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

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
	private File file = null;
	private LinkedList<Session> sessions;
	private ChangeQueue queue;
	//TODO: listeners and locks come here
	
	public ServerFile(String path){
		this.path = path;
		this.queue = new ChangeQueue();
		file = new File(path.replace('\\' , '.'));
		sessions = new LinkedList<Session>();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public synchronized void changeContents(String contents) throws IOException{
		file.createNewFile();
	    BufferedWriter out = new BufferedWriter(new FileWriter(path.replace('\\' , '.')));
	    out.write(contents);
	    out.close(); 
	}
	
	public void addSession(Session s){
		if (! sessions.contains(s))
			sessions.add(s);
	}
	
	public void removeSession(Session s){
		if (sessions.contains(s))
			sessions.remove(s);
		if (sessions.size() == 0 )
			file.delete();
	}
	
	public int getNrOfSessions(){
		return sessions.size();
	}
	
}
