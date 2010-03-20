/**
 * 
 */
package coed.collab.client;

import java.util.LinkedList;
import coed.base.data.ICoedFile;
import coed.base.data.ICollabFilePart;
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
public class CoedCollabFile implements ICollabFilePart {
	
	private CollaboratorClient coll;
	private ICoedFile obj;
	private boolean isWorkingOnline;
	private LinkedList<IFileChangeListener> fileObservers;
	private Integer id;
	
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
	
	public CoedCollabFile(ICoedFile obj, CollaboratorClient coll, Integer id) {
		this.obj = obj;
		this.coll = coll;
		this.id = id;
		this.fileObservers = new LinkedList<IFileChangeListener>();
		this.isWorkingOnline = true;
	}
	
	@Override
	public ICoedFile getParent() {
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
			coll.getConn().sendSeq(new GetChangesMsg(getId())).addListener(fl);
		return fl.ret;
	}
	
	@Override
	public IFuture<Boolean> releaseLock(TextPortion lock) {
		coll.getConn().send(new ReleaseLockMsg(getId(), lock));
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
			coll.getConn().sendSeq(new RequestLockMsg(getId(), lock)).addListener(fl);
		return fl.ret;
	}

	@Override
	public IFuture<Boolean> sendChanges(TextModification line) {
		TextModification[] mods = new TextModification[1];
		mods[0] = line;
		
		coll.getConn().send(new SendChangesMsg(getId(), mods));
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
		coll.getConn().send(new AddChangedListenerMsg(getId()));
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
			coll.getConn().send(new GoOfflineMsg(getId()));
		}
		return ret;
	}
	
	public void notifyChangeListeners(ICoedFile obj) {
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
			coll.getConn().sendSeq(new GetContentsMsg(getId())).addListener(fl);
		return fl.ret;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public IFuture<Void> endSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getId() {
		return id;
	}
}
