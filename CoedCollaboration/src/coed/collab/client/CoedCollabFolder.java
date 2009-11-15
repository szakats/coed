/**
 * 
 */
package coed.collab.client;

import java.io.File;

import coed.base.common.ICoedObject;
import coed.base.common.CoedObject;
import coed.base.common.ICollabObject;
import coed.base.data.CoedFileLine;
import coed.base.data.CoedFileLock;
import coed.base.data.IFileObserver;
import coed.base.data.exceptions.NotConnectedToServerException;

/**
 * @author Neobi008
 *
 */
public class CoedCollabFolder implements ICollabObject {
	
	private CollaboratorClient coll;
	private ICoedObject obj;
	
	public CoedCollabFolder(ICoedObject obj, CollaboratorClient coll) {
		this.obj = obj;
		this.coll = coll;
	}

	@Override
	public void addChangeListener(IFileObserver fileObserver) {
	    recursiveAddChangeListener(fileObserver, obj.getPath());
	   
	}
	
	public void recursiveAddChangeListener(IFileObserver fileObserver, String path){
		File f = new File(coll.getBasePath()+ path);
		
		File[] listOfFiles = f.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        coll.makeCollabObject(new CoedObject(path+"\\"+listOfFiles[i].getName(),true));
	      } else if (listOfFiles[i].isDirectory()) {
	    	    recursiveAddChangeListener(fileObserver, path+"\\"+listOfFiles[i].getName());
	        System.out.println("Directory " + listOfFiles[i].getName());
	      }
	    }
	}

	@Override
	public boolean addFileChangeListener(IFileObserver fileObserver)
			throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] getActiveUsers() throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CoedFileLine[] getChanges() throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void goOffline() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goOnline() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean releaseLock(CoedFileLock lock)
			throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeChangeListener(IFileObserver fileObserver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean requestLock(CoedFileLock lock)
			throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendChanges(CoedFileLine line)
			throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		return false;
	}
}