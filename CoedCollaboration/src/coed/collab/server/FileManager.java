/**
 * 
 */
package coed.collab.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**Class for managing files on the server. Contains a synchronized map, that 
 * contains the path of the file as key, and the corresponding ServerFile object
 * as a value. 
 * The class manages all online (live) files on the server.
 * @author Neobi
 *
 */
public class FileManager {
	
	private Map<String,ServerFile> files;
	
	public FileManager(){
		HashMap hashMap = new HashMap<String,ServerFile>();
		files = Collections.synchronizedMap(hashMap);	
	}
	
	/**
	 * Adds a file to the Manager.
	 * @param file the ServerFile object to be added
	 */
	public void addFile(ServerFile file){
		files.put(file.getPath(), file);
	}
	
	/**
	 * Removes a file from the Manager
	 * @param path the path of the file (key).
	 */
	public void removeFile(String path){
		files.remove(path);
	}
	
	/**
	 * Synchronized access method that returns an array of all the ServerFiles that are in this
	 * FileManager(i.e. the online files)
	 * @return array of ServerFile objects
	 */
	public ServerFile[] getAllLiveFiles(){
		ServerFile[] result = new ServerFile[files.size()];
		int i = 0;
		synchronized(files) {
			for(Map.Entry<String, ServerFile> entry: files.entrySet()){
				result[i] = entry.getValue();
				i++;
			}
		}
		return result;
			
	}

}
