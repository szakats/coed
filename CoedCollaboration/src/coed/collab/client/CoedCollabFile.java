/**
 * 
 */
package coed.collab.client;

import java.util.LinkedList;

import coed.base.data.ICoedObject;
import coed.base.data.ICollabObject;
import coed.base.data.IFileObserver;
import coed.base.data.TextModification;
import coed.base.data.TextPortion;
import coed.base.data.exceptions.NotConnectedToServerException;
import coed.base.util.CoedFuture;
import coed.base.util.IFuture;
import coed.base.util.IFutureListener;
import coed.collab.protocol.*;

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
	public IFuture<TextModification[]> getChanges() throws NotConnectedToServerException {
		coll.ensureConnected();
		
		class FListener implements IFutureListener<CoedMessage> {
			public CoedFuture<TextModification[]> ret = new CoedFuture<TextModification[]>();
			@Override
			public void got(CoedMessage result) {
				if(result instanceof GetChangesResultMsg)
					ret.set(((GetChangesResultMsg)result).getMods());
			}
		}
		
		FListener fl = new FListener();
		coll.getConn().sendF(new GetChangesMsg()).add(fl);
		return fl.ret;
	}
	
	@Override
	public boolean releaseLock(TextPortion lock) throws NotConnectedToServerException {
		coll.ensureConnected();
		
		return false;
	}

	@Override
	public boolean requestLock(TextPortion lock) throws NotConnectedToServerException {
		coll.ensureConnected();
		
		return false;
	}

	@Override
	public IFuture<Boolean> sendChanges(TextModification line) throws NotConnectedToServerException {
		coll.ensureConnected();
		//conn.send(new SendChangesMsg(null, line));
		return null;
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
	public IFuture<String> goOnline(String contents) {
		isWorkingOnline = true;
		coll.incNrOnline();
		return null;
	}
	
	public void notifyChangeListeners(ICoedObject obj) {
		for(IFileObserver obs : fileObservers)
			obs.update(obj);
	}

	@Override
	public IFuture<String> getCurrentContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFuture<ICoedObject[]> goOnline() {
		// TODO Auto-generated method stub
		assert(false); // until implemented
		return null;
	}
}
