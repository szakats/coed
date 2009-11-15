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
public class CoedCollabFile implements ICollabObject {
	
	private CollaboratorClient coll;
	private ICoedObject obj;
	private boolean isWorkingOnline;
	private IFileObserver fileObserver;
	
	public CoedCollabFile(ICoedObject obj, CollaboratorClient coll) {
		this.obj = obj;
		this.coll = coll;
	}
	
	
	@Override
	public String[] getActiveUsers() throws NotConnectedToServerException {
		coll.ensureConnected();

		return null;
	}

	@Override
	public CoedFileLine[] getChanges() throws NotConnectedToServerException {
		coll.ensureConnected();
		
		return null;
	}
	
	@Override
	public boolean releaseLock(CoedFileLock lock) throws NotConnectedToServerException {
		coll.ensureConnected();
		
		return false;
	}

	@Override
	public boolean requestLock(CoedFileLock lock) throws NotConnectedToServerException {
		coll.ensureConnected();
		
		return false;
	}

	@Override
	public boolean sendChanges(CoedFileLine line) throws NotConnectedToServerException {
		coll.ensureConnected();
		//conn.send(new SendChangesMsg(null, line));
		return false;
	}

	@Override
	public void addChangeListener(IFileObserver fileObserver) {
		this.fileObserver = fileObserver;
		
	}

	@Override
	public boolean addFileChangeListener(IFileObserver fileObserver) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeChangeListener(IFileObserver fileObserver) {
		this.fileObserver = null;
		
	}

	@Override
	public void goOffline() {
		isWorkingOnline = false;
		coll.decNrOnline();

	}

	@Override
	public void goOnline() {
		isWorkingOnline = true;
		coll.incNrOnline();
		

	}
}
