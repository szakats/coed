/**
 * 
 */
package coed.base.data;

import coed.base.common.ICoedObject;
import coed.base.common.ICollabObject;
import coed.base.common.IVersionedObject;
import coed.base.data.exceptions.NotConnectedToServerException;

/**
 * @author Neobi
 *
 */
public class CoedObject implements ICoedObject {
	
	private IVersionedObject vo;
	private ICollabObject co;
	private String path;
	
	public CoedObject(String path, IVersionedObject vo, ICollabObject co){
		this.path = path;
		this.co = co;
		this.vo = vo;
	}

	@Override
	public boolean checkoutFile() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean commitFile() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addChangeListener(IFileObserver fileObserver) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return null;
	}

}
