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
		return fl.ret;
	}
	
	@Override
	public IFuture<Boolean> releaseLock(TextPortion lock) {
		coll.getConn().send(new ReleaseLockMsg(getParent().getPath(), lock));
		return new CoedFuture<Boolean>(true);
	}

	@Override
	public IFuture<Boolean> requestLock(TextPortion lock) {
		class FListener implements IFutureListener<CoedMessage> {
			public CoedFuture<Boolean> ret = new CoedFuture<Boolean>();
			@Override
			public void got(CoedMessage result) {
				if(result instanceof RequestLockReplyMsg)
					ret.set(((RequestLockReplyMsg)result).getResult());
			}
			@Override
			public void caught(Throwable e) {
				rethrow(e, ret);
			}
		}
		
		FListener fl = new FListener();
		if(ensureOnline(fl.ret))
			coll.getConn().sendSeq(new RequestLockMsg(getParent().getPath(), lock)).addListener(fl);
		return fl.ret;
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
		// TODO: don't do this
		//fileObservers.clear();
		if(ensureOnline(ret)) {
			if(fileObservers.contains(listener))
				System.out.println("CoedCollabFile:addChangeListener: attempt to add duplicate listener");
			else
				fileObservers.add(listener);
		}
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
		if(ensureOnline(ret)) {
			isWorkingOnline = false;
			coll.getConn().send(new GoOfflineMsg(getParent().getPath()));
		}
		return ret;
	}

	
	@Override
	public IFuture<String> goOnline(String contents) {
		
		// listener class for future messages in the sequence
		class FListener implements IFutureListener<CoedMessage> {
			public CoedFuture<String> ret = new CoedFuture<String>();
			public String localContents;
		
			public FListener(String localContents) {
				this.localContents = localContents;
			}
			
			void finish(String contents) {
				//notify the chained future of the result
				isWorkingOnline = true;
				ret.set(contents);
			}

			@Override
			public void got(CoedMessage result) {
				if(result instanceof GoOnlineResultMsg) {
					GoOnlineResultMsg msg = ((GoOnlineResultMsg)result);
					System.out.println("got online result: " + msg.isAlreadyOnline());
					if(msg.isAlreadyOnline()) {
						// if online, get the remote contents for this file
						coll.getConn().sendSeq(new GetContentsMsg(getParent().getPath()))
							.addListener(this);	// expects a SendContentsMsg
					} else {
						// otherwise send the local contents of the file
						coll.getConn().reply(msg, new SendContentsMsg(localContents))
							.addErrorListener(this);
						// no new contents was received, return null
						finish(null);
					}
				} else if(result instanceof SendContentsMsg) {
					String remoteContents = ((SendContentsMsg)result).getContents();
					System.out.println("got remote contents: " + remoteContents);
					// return the remote contents
					finish(remoteContents);
				}
			}

			@Override
			public void caught(Throwable e) {
				// if an error occured anywhere along the sequence
				// then pass the error along to the chained future
				ret.throwEx(e);
			}
		}

		FListener fl = new FListener(contents);
		// first send which file should be shared
		coll.getConn().sendSeq(new GoOnlineMsg(getParent().getPath()))
			.addListener(fl); // expects a GoOnlineResultMsg
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
