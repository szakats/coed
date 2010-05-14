/**
 * 
 */
package coed.base.data;

import coed.base.comm.IUserChangeListener;
import coed.base.util.IFuture;

/**
 * @author Neobi
 *
 */
public class CoedFile implements ICoedFile {
	
	private IVersionedFilePart vo;
	private ICollabFilePart co;
	private String path;
	
	public CoedFile(String path) {
		this.path = path;
	}
	
	public void init(IVersionedFilePart vo, ICollabFilePart co) {
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
	public IFuture<Void> endSession() {
		return co.endSession();
	}

	@Override
	public String getPath() {
		return this.path;
	}

	@Override
	public IFuture<String> getRemoteContents() {
		return co.getRemoteContents();
	}

	@Override
	public Integer getId() {
		return co.getId();
	}

	@Override
	public IFuture<Void> goOffline() {
		return co.goOffline();
	}

	@Override
	public void addUserChangeListener(IUserChangeListener listener) {
		co.addUserChangeListener(listener);
	}

	@Override
	public void removeUserChangeListener(IUserChangeListener listener) {
		co.removeUserChangeListener(listener);
	}
}
