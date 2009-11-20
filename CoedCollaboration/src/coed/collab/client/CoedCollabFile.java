/**
 * 
 */
package coed.collab.client;

import java.util.LinkedList;

import coed.base.data.ICoedObject;
import coed.base.data.ICollabObject;
import coed.base.data.IFileChangeListener;
import coed.base.data.TextModification;
import coed.base.data.TextPortion;
import coed.base.data.exceptions.NotConnectedToServerException;
import coed.base.util.CoedFuture;
import coed.base.util.IFuture;
import coed.base.util.IFutureListener;
import coed.collab.connection.ICoedConnection;
import coed.collab.protocol.*;

/**
 * @author Neobi008
 *
 */
public class CoedCollabFile implements ICollabObject {
	
	private CollaboratorClient coll;
	private ICoedObject obj;
	private boolean isWorkingOnline;
	private LinkedList<IFileChangeListener> fileObservers;
	
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
				if(result instanceof GetChangesReplyMsg)
					ret.set(((GetChangesReplyMsg)result).getMods());
			}
		}
		
		FListener fl = new FListener();
		coll.getConn().sendF(new GetChangesMsg(getParent().getPath())).add(fl);
		return new FListener().ret;
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
	public void addChangeListener(IFileChangeListener listener) {
		fileObservers.add(listener);
	}

	@Override
	public void removeChangeListener(IFileChangeListener listener) {
		fileObservers.remove(listener);
	}

	@Override
	public void goOffline() {
		isWorkingOnline = false;
		coll.decNrOnline();
	}

	
	@Override
	public IFuture<String> goOnline(String contents) {
		
		class FListener implements IFutureListener<CoedMessage> {
			public CoedFuture<String> ret = new CoedFuture<String>();
			public String contents;
			@Override
			public void got(CoedMessage result) {
				if(result instanceof GoOnlineResultMsg) {
					if(((GoOnlineResultMsg)result).isAlreadyOnline()) {
						ret.chain(getCurrentContent());
					} else {
						coll.getConn().reply(result, new SendContentsMsg(contents));
						ret.set(null);
					}
				}
			}
		}
		
		isWorkingOnline = true;
		coll.incNrOnline();
		FListener fl = new FListener();
		fl.contents = contents;
		coll.getConn().sendF(new GoOnlineMsg(getParent().getPath())).add(fl);
		return new FListener().ret;
	}
	
	public void notifyChangeListeners(ICoedObject obj) {
		for(IFileChangeListener obs : fileObservers)
			obs.hasChanges(obj);
	}

	@Override
	public IFuture<String> getCurrentContent() {
		
		class FListener implements IFutureListener<CoedMessage> {
			public CoedFuture<String> ret = new CoedFuture<String>();
			@Override
			public void got(CoedMessage result) {
				if(result instanceof SendContentsMsg) {
					ret.set(((SendContentsMsg)result).getContents());
				}
			}
			
		}
		
		FListener fl = new FListener();
		coll.getConn().sendF(new GetContentsMsg(getParent().getPath())).add(fl);
		return fl.ret;
	}
}
