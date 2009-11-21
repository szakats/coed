/**
 * 
 */
package coed.collab.client;

import java.util.LinkedList;

import coed.base.comm.ICoedCollaborator;
import coed.base.comm.ICollabStateListener;
import coed.base.data.ICoedObject;
import coed.base.data.ICollabObject;
import coed.base.data.IFileChangeListener;
import coed.base.data.TextModification;
import coed.base.data.TextPortion;
import coed.base.data.exceptions.NotConnectedException;
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
	public IFuture<String[]> getActiveUsers() throws NotConnectedException {

		return null;
	}

	@Override
	public IFuture<TextModification[]> getChanges() throws NotConnectedException {
		
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
	public boolean releaseLock(TextPortion lock) throws NotConnectedException {
		
		return false;
	}

	@Override
	public boolean requestLock(TextPortion lock) throws NotConnectedException {
		
		return false;
	}

	@Override
	public IFuture<Boolean> sendChanges(TextModification line) throws NotConnectedException {
		//conn.send(new SendChangesMsg(null, line));
		return null;
	}

	@Override
	public void addChangeListener(IFileChangeListener listener) throws NotConnectedException {
		fileObservers.add(listener);
	}

	@Override
	public void removeChangeListener(IFileChangeListener listener) throws NotConnectedException {
		fileObservers.remove(listener);
	}

	@Override
	public void goOffline() {
		isWorkingOnline = false;
		coll.decNrOnline();
	}

	
	@Override
	public IFuture<String> goOnline(String contents) {
		
		class GoOnline implements ICollabStateListener {
			public CoedFuture<String> ret = new CoedFuture<String>();
			public String localContents;
			private GoOnlineResultMsg onlineResult;
			
			GoOnline(String localContents) {
				this.localContents = localContents;
				if(coll.getState() == ICoedCollaborator.STATUS_CONNECTED) {
					if(onlineResult == null)
						sendGoOnline();
					else
						processOnlineResult();
				} else
					coll.addStateListener(this);
			}
			
			@Override
			public void collabStateChanged(String to) {
				if(to == ICoedCollaborator.STATUS_CONNECTED) {
					coll.removeStateListener(this);
					sendGoOnline();
				}
			}
			
			void sendGoOnline() {
				class FListener implements IFutureListener<CoedMessage> {
					@Override
					public void got(CoedMessage result) {
						if(result instanceof GoOnlineResultMsg) {
							onlineResult = ((GoOnlineResultMsg)result);
							processOnlineResult();
						}
					}
				}
				
				try {
					coll.getConn().sendF(new GoOnlineMsg(getParent().getPath())).add(new FListener());
				} catch (NotConnectedException e) {
					coll.addStateListener(this);
				}
			}
			
			void processOnlineResult() {
				try {
					if(onlineResult.isAlreadyOnline()) {
						ret.chain(getRemoteContents());
					} else {
						coll.getConn().reply(onlineResult, new SendContentsMsg(localContents));
						ret.set(null);
					}
				} catch (NotConnectedException e) {
					coll.addStateListener(this);
				}
			}
		}
		
		isWorkingOnline = true;
		coll.incNrOnline();
		return new GoOnline(contents).ret;
	}
	
	public void notifyChangeListeners(ICoedObject obj) {
		for(IFileChangeListener obs : fileObservers)
			obs.hasChanges(obj);
	}

	@Override
	public IFuture<String> getRemoteContents() throws NotConnectedException {
		
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
