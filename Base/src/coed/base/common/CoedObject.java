/**
 * 
 */
package coed.base.common;

import coed.base.data.CoedFileLine;
import coed.base.data.CoedFileLock;
import coed.base.data.IFileObserver;
import coed.base.data.exceptions.NotConnectedToServerException;

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
	public void addChangeListener(IFileObserver fileObserver) {
		co.addChangeListener(fileObserver);
		
	}

	@Override
	public String[] getActiveUsers() throws NotConnectedToServerException {
		return co.getActiveUsers();
	}

	@Override
	public CoedFileLine[] getChanges() throws NotConnectedToServerException {
		return co.getChanges();
	}

	@Override
	public boolean releaseLock(CoedFileLock lock)
			throws NotConnectedToServerException {
		return co.releaseLock(lock);
	}

	@Override
	public void removeChangeListener(IFileObserver fileObserver) {
		co.removeChangeListener(fileObserver);
	}

	@Override
	public boolean requestLock(CoedFileLock lock)
			throws NotConnectedToServerException {
		return co.requestLock(lock);
	}

	@Override
	public boolean sendChanges(CoedFileLine line)
			throws NotConnectedToServerException {
		return co.sendChanges(line);
	}

	@Override
	public void goOffline() {
		co.goOffline();
	}

	@Override
	public void goOnline() {
		co.goOnline();
	}

	@Override
	public String getPath() {
		return this.path;
	}
	
	public boolean isFile(){
		return this.isFile;
	}

}
