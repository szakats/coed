/**
 * 
 */
package coed.collab.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class for representing a file on a server. It contains the locks, listeners,
 * and a ChangeQueue with all recent changes on this file. Also contains the file itself,
 * the current live version.
 * @author Neobi
 *
 */
public class ServerFile {

	private String path;
	private File file = null;
	private ChangeQueue queue;
	//TODO: listeners and locks come here
	
	public ServerFile(String path){
		this.path = path;
		this.queue = new ChangeQueue();
		file = new File(path.replace('\\' , '.'));
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
	
}
