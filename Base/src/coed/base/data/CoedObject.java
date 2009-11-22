/**
 * 
 */
package coed.base.data;

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
	public IFuture<Void> addChangeListener(IFileChangeListener listener) {
		return co.addChangeListener(listener);
	}

	@Override
	public IFuture<String[]> getActiveUsers() {
		return co.getActiveUsers();
	}

	@Override
	public IFuture<TextModification[]> getChanges() {
		return co.getChanges();
	}

	@Override
	public IFuture<Boolean> releaseLock(TextPortion lock) {
		return co.releaseLock(lock);
	}

	@Override
	public IFuture<Void> removeChangeListener(IFileChangeListener listener) {
		return co.removeChangeListener(listener);
	}

	@Override
	public IFuture<Boolean> requestLock(TextPortion lock) {
		return co.requestLock(lock);
	}

	@Override
	public IFuture<Boolean> sendChanges(TextModification line) {
		return co.sendChanges(line);
	}

	@Override
	public IFuture<Void> goOffline() {
		return co.goOffline();
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
	public IFuture<String> getRemoteContents() {
		return co.getRemoteContents();
	}
}
