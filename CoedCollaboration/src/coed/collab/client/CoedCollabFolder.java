/**
 * 
 */
package coed.collab.client;

import java.io.File;

import coed.base.data.CoedObject;
import coed.base.data.ICoedObject;
import coed.base.data.ICollabObject;
import coed.base.data.IFileChangeListener;
import coed.base.data.TextModification;
import coed.base.data.TextPortion;
import coed.base.data.exceptions.NotConnectedException;
import coed.base.util.IFuture;

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
	public void addChangeListener(IFileChangeListener listener) throws NotConnectedException {
	    recursiveAddChangeListener(listener, obj.getPath());
	   
	}
	
	public void recursiveAddChangeListener(IFileChangeListener fileObserver, String path){
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
	public IFuture<String[]> getActiveUsers() throws NotConnectedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFuture<TextModification[]> getChanges() throws NotConnectedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void goOffline() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IFuture<String> goOnline(String contents) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean releaseLock(TextPortion lock)
			throws NotConnectedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeChangeListener(IFileChangeListener listener) throws NotConnectedException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * REVIEW!!!! Needs serious rethinking
	 */
	@Override
	public boolean requestLock(TextPortion lock)
			throws NotConnectedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IFuture<Boolean> sendChanges(TextModification line)
			throws NotConnectedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICoedObject getParent() {
		return obj;
	}

	@Override
	public IFuture<String> getRemoteContents() throws NotConnectedException {
		// TODO Auto-generated method stub
		return null;
	}
}
