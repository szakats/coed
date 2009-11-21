/**
 * 
 */
package coed.base.data;

import coed.base.data.exceptions.NotConnectedException;
import coed.base.util.IFuture;

/**
 * @author Neobi
 *
 */
public class CoedObject implements ICoedObject {
	
	private IVersionedObject vo;
	private ICollabObject co;
	private String path;
	private boolean isFile;
	
	public CoedObject(String path, boolean isFile) {
		this.path = path;
		this.isFile = isFile;
	}
	
	public void init(IVersionedObject vo, ICollabObject co) {
		this.co = co;
		this.vo = vo;
	}

	@Override
	public boolean checkout() {
		return vo.checkout();
	}

	@Override
	public boolean commit() {
		return vo.commit();
	}

	@Override
	public void addChangeListener(IFileChangeListener listener) throws NotConnectedException {
		co.addChangeListener(listener);
		
	}

	@Override
	public IFuture<String[]> getActiveUsers() throws NotConnectedException {
		return co.getActiveUsers();
	}

	@Override
	public IFuture<TextModification[]> getChanges() throws NotConnectedException {
		return co.getChanges();
	}

	@Override
	public boolean releaseLock(TextPortion lock)
			throws NotConnectedException {
		return co.releaseLock(lock);
	}

	@Override
	public void removeChangeListener(IFileChangeListener listener) throws NotConnectedException {
		co.removeChangeListener(listener);
	}

	@Override
	public boolean requestLock(TextPortion lock)
			throws NotConnectedException {
		return co.requestLock(lock);
	}

	@Override
	public IFuture<Boolean> sendChanges(TextModification line)
			throws NotConnectedException {
		return co.sendChanges(line);
	}

	@Override
	public void goOffline() {
		co.goOffline();
	}

	@Override
	public IFuture<String> goOnline(String contents) {
		return co.goOnline(contents);
	}

	@Override
	public String getPath() {
		return this.path;
	}
	
	@Override
	public boolean isFile(){
		return this.isFile;
	}

	@Override
	public IFuture<String> getRemoteContents() throws NotConnectedException {
		return co.getRemoteContents();
	}
}
