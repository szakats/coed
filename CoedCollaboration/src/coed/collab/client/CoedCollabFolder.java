/**
 * 
 */
package coed.collab.client;

import coed.base.common.ICoedObject;
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
		// TODO Auto-generated method stub
		
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
