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
import coed.base.data.exceptions.NotOnlineException;
import coed.base.data.exceptions.NotConnectedException;
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
	private LinkedList<IFileChangeListener> fileObservers;
	
	private <T> boolean ensureOnline(CoedFuture<T> future) {
		if(!isWorkingOnline) {
			future.throwEx(new NotOnlineException());
			return false;
		} else
			return true;
	}
	
	private <T> void rethrow(Throwable t, CoedFuture<T> future) {
		if(t instanceof NotConnectedException)
			future.throwEx(new NotOnlineException());
		else if(t instanceof NotOnlineException)
			future.throwEx(t);
		else
			assert false;
	}
	
	public CoedCollabFile(ICoedObject obj, CollaboratorClient coll) {
		this.obj = obj;
		this.coll = coll;
		this.fileObservers = new LinkedList<IFileChangeListener>();
	}
	
	@Override
	public ICoedObject getParent() {
		return obj;
	}
	
	
	@Override
	public IFuture<String[]> getActiveUsers() {

		return null;
	}

	@Override
	public IFuture<TextModification[]> getChanges() {
		
		class FListener implements IFutureListener<CoedMessage> {
			public CoedFuture<TextModification[]> ret = new CoedFuture<TextModification[]>();
			@Override
			public void got(CoedMessage result) {
				if(result instanceof GetChangesReplyMsg)
					ret.set(((GetChangesReplyMsg)result).getMods());
			}
			@Override
			public void caught(Throwable e) {
				rethrow(e, ret);
			}
		}
		
		FListener fl = new FListener();
		if(ensureOnline(fl.ret))
			coll.getConn().sendSeq(new GetChangesMsg(getParent().getPath())).addListener(fl);
		return new FListener().ret;
	}
	
	@Override
	public IFuture<Boolean> releaseLock(TextPortion lock) {
		return new CoedFuture<Boolean>(new Boolean(false));
	}

	@Override
	public IFuture<Boolean> requestLock(TextPortion lock) {
		return new CoedFuture<Boolean>(new Boolean(false));
	}

	@Override
	public IFuture<Boolean> sendChanges(TextModification line) {
		TextModification[] mods = new TextModification[1];
		mods[0] = line;
		
		coll.getConn().send(new SendChangesMsg(getParent().getPath(), mods));
		return new CoedFuture<Boolean>(true);
	}

	@Override
	public IFuture<Void> addChangeListener(IFileChangeListener listener) {
		CoedFuture<Void> ret = new CoedFuture<Void>();
		if(ensureOnline(ret))
			fileObservers.add(listener);
		coll.getConn().send(new AddChangedListenerMsg(getParent().getPath()));
		return ret;
	}

	@Override
	public IFuture<Void> removeChangeListener(IFileChangeListener listener) {
		CoedFuture<Void> ret = new CoedFuture<Void>();
		if(ensureOnline(ret))
			fileObservers.remove(listener);
		return ret;
	}

	@Override
	public IFuture<Void> goOffline() {
		CoedFuture<Void> ret = new CoedFuture<Void>();
		if(ensureOnline(ret))
			isWorkingOnline = false;
		return ret;
	}

	
	@Override
	public IFuture<String> goOnline(String contents) {
		
		class FListener implements IFutureListener<CoedMessage> {
			public CoedFuture<String> ret = new CoedFuture<String>();
			public String localContents;
		
			public FListener(String localContents) {
				this.localContents = localContents;
			}
			
			void finish(String contents) {
				isWorkingOnline = true;
				ret.set(contents);
			}

			@Override
			public void got(CoedMessage result) {
				if(result instanceof GoOnlineResultMsg) {
					GoOnlineResultMsg msg = ((GoOnlineResultMsg)result);
					System.out.println("got online result: " + msg.isAlreadyOnline());
					if(msg.isAlreadyOnline()) {
						coll.getConn().sendSeq(new GetContentsMsg(getParent().getPath()))
							.addListener(this);
					} else {
						coll.getConn().reply(msg, new SendContentsMsg(localContents))
							.addErrorListener(this);
						
						// TODO: maybe only set this after the server confirmed that it
						// got the contents ?
						finish(null);
					}
				} else if(result instanceof SendContentsMsg) {
					String remoteContents = ((SendContentsMsg)result).getContents();
					System.out.println("got remote contents: " + remoteContents);
					finish(remoteContents);
				}
			}

			@Override
			public void caught(Throwable e) {
				assert e instanceof NotConnectedException;
				ret.throwEx(e);
			}
		}

		FListener fl = new FListener(contents);
		coll.getConn().sendSeq(new GoOnlineMsg(getParent().getPath())).addListener(fl);
		return fl.ret;
	}
	
	public void notifyChangeListeners(ICoedObject obj) {
		for(IFileChangeListener obs : fileObservers)
			obs.hasChanges(obj);
	}

	@Override
	public IFuture<String> getRemoteContents() {
		
		class FListener implements IFutureListener<CoedMessage> {
			public CoedFuture<String> ret = new CoedFuture<String>();
			@Override
			public void got(CoedMessage result) {
				if(result instanceof SendContentsMsg) {
					ret.set(((SendContentsMsg)result).getContents());
				}
			}
			@Override
			public void caught(Throwable e) {
				rethrow(e, ret);
			}
		}
		
		FListener fl = new FListener();
		if(ensureOnline(fl.ret))
			coll.getConn().sendSeq(new GetContentsMsg(getParent().getPath())).addListener(fl);
		return fl.ret;
	}
}
