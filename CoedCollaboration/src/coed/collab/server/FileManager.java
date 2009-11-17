/**
 * 
 */
package coed.collab.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**Class for managing files on the server. 
 * @author Neobi
 *
 */
public class FileManager {
	
	private Map<String,ServerFile> files;
	
	public FileManager(){
		HashMap hashMap = new HashMap<String,ServerFile>();
		files = Collections.synchronizedMap(hashMap);	
	}
	
	public void addFile(ServerFile file){
		files.put(file.getPath(), file);
	}
	
	public void removeFile(String path){
		files.remove(path);
	}
	
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
