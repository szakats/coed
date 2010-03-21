/**
 * 
 */
package coed.collab.server;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**Class for managing files on the server. Contains a synchronized map, that 
 * contains the path of the file as key, and the corresponding ServerFile object
 * as a value. 
 * The class manages all online (live) files on the server.
 * @author Neobi
 *
 */
public class FileManager {
	
	private Map<Integer,ServerFile> files;
	private Integer maxId = 1;
	
	public FileManager(){
		HashMap hashMap = new HashMap<Integer,ServerFile>();
		files = Collections.synchronizedMap(hashMap);	
	}
	
	/**
	 * Adds a file to the Manager., if the file is not already 
	 * in the manager.
	 * @param file the ServerFile object to be added
	 */
	public ServerFile addFile(String path, String contents){
		    ServerFile result = new ServerFile(path,maxId++);
		    try {
				result.changeContents(contents);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			files.put(result.getId(),result);
			return result;
	}
	
	/**
	 * Removes a file from the Manager
	 * @param path the path of the file (key).
	 */
	public void removeFile(Integer id){
		if (files.containsKey(id))
			files.remove(id);
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
			for(Map.Entry<Integer, ServerFile> entry: files.entrySet()){
				result[i] = entry.getValue();
				i++;
			}
		}
		return result;	
	}
	
	public boolean containsFile(String path){
		return files.containsKey(path);
	}
	
	public ServerFile getFile(Integer id){
		if (files.containsKey(id))
			return (ServerFile)(files.get(id));
		else return null;
	}
	
	public void addSessionToFile(ServerFile sf, Session s){
		if (files.containsKey(sf.getPath())){
			sf.addSession(s);
		}
	}
	
	/**
	 * removes a session from the serverfile. if the removed 
	 * session was the last one registered on this serverfile,
	 * the serverfile will be deleted from the managar.
	 * if the specified file is not in the manager, no action is taken
	 * @param sf
	 * @param s
	 */
	public void removeSessionFromFile(ServerFile sf, Session s){
		if (files.containsKey(sf.getPath())){
			sf.removeSession(s);
			if ( sf.getNrOfSessions() == 0 ) 
				files.remove(sf.getPath());
		}
	}

	public Collection<ServerFile> getServerFiles() {
		return files.values();
	}
}
