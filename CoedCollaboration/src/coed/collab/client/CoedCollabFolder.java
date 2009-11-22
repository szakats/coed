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
import coed.base.util.CoedFuture;
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
	public IFuture<Void> addChangeListener(IFileChangeListener listener) {
	    recursiveAddChangeListener(listener, obj.getPath());
	    return new CoedFuture<Void>((Void)null);
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
	public IFuture<String[]> getActiveUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFuture<TextModification[]> getChanges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFuture<Void> goOffline() {
		return new CoedFuture<Void>((Void)null);
	}

	@Override
	public IFuture<String> goOnline(String contents) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFuture<Boolean> releaseLock(TextPortion lock) {
		// TODO Auto-generated method stub
		return new CoedFuture<Boolean>(new Boolean(false));
	}

	@Override
	public IFuture<Void> removeChangeListener(IFileChangeListener listener) {
		return new CoedFuture<Void>((Void)null);
	}

	/**
	 * REVIEW!!!! Needs serious rethinking
	 */
	@Override
	public IFuture<Boolean> requestLock(TextPortion lock) {
		// TODO Auto-generated method stub
		return new CoedFuture<Boolean>(new Boolean(false));
	}

	@Override
	public IFuture<Boolean> sendChanges(TextModification line) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICoedObject getParent() {
		return obj;
	}

	@Override
	public IFuture<String> getRemoteContents() {
		// TODO Auto-generated method stub
		return null;
	}
}
