/**
 * 
 */
package coed.collab.server;

import java.io.File;

/**
 * Class for representing a file on a server. It contains the locks, listeners,
 * and a ChangeStack with all recent changes on this file.
 * @author Neobi
 *
 */
public class ServerFile {

	private String path;
	private File file;
	private ChangeStack stack;
	//TODO: listeners and locks come here
	
	public ServerFile(String path){
		this.path = path;
		this.stack = new ChangeStack(ChangeStack.MAX_CAPACITY);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}