/**
 * 
 */
package coed.collab.client;

import java.util.LinkedList;

import coed.base.data.CoedFileLine;
import coed.base.data.CoedFileLock;
import coed.base.data.ICoedObject;
import coed.base.data.ICollabObject;
import coed.base.data.IFileObserver;
import coed.base.data.exceptions.NotConnectedToServerException;
import coed.base.util.IFuture;
import coed.collab.protocol.CoedMessage;

/**
 * @author Neobi008
 *
 */
public class CoedCollabFile implements ICollabObject {
	
	private CollaboratorClient coll;
	private ICoedObject obj;
	private boolean isWorkingOnline;
	private LinkedList<IFileObserver> fileObservers;
	
	public CoedCollabFile(ICoedObject obj, CollaboratorClient coll) {
		this.obj = obj;
		this.coll = coll;
	}
	
	@Override
	public ICoedObject getParent() {
		return obj;
	}
	
	
	@Override
	public IFuture<String[]> getActiveUsers() throws NotConnectedToServerException {
		coll.ensureConnected();

		return null;
	}

	@Override
	public IFuture<CoedFileLine[]> getChanges() throws NotConnectedToServerException {
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
		fileObservers.add(fileObserver);
	}

	@Override
	public void removeChangeListener(IFileObserver fileObserver) {
		fileObservers.remove(fileObserver);
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
	
	public void notifyChangeListeners(ICoedObject obj) {
		for(IFileObserver obs : fileObservers)
			obs.update(obj);
	}
}
