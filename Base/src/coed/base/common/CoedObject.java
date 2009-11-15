/**
 * 
 */
package coed.base.common;

import java.io.File;

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean commit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addChangeListener(IFileObserver fileObserver) {
		co.addChangeListener(fileObserver);
		
	}

	@Override
	public boolean addFileChangeListener(IFileObserver fileObserver)
			throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] getActiveUsers() throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CoedFileLine[] getChanges() throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean releaseLock(CoedFileLock lock)
			throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeChangeListener(IFileObserver fileObserver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean requestLock(CoedFileLock lock)
			throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendChanges(CoedFileLine line)
			throws NotConnectedToServerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void goOffline() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goOnline() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getPath() {
		return this.path;
	}
	
	public boolean isFile(){
		return this.isFile;
	}

}
