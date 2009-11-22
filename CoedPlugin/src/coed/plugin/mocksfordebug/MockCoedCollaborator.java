package coed.plugin.mocksfordebug;

import coed.base.comm.ICoedCollaborator;
import coed.base.comm.ICoedCommunicator;
import coed.base.comm.ICollabStateListener;
import coed.base.data.ICoedObject;
import coed.base.data.ICollabObject;
import coed.base.data.IVersionedObject;
import coed.base.data.exceptions.InvalidConfigFileException;
import coed.base.data.exceptions.UnknownVersionerTypeException;
import coed.base.util.IFuture;

public class MockCoedCollaborator implements coed.base.comm.ICoedCommunicator {

	public MockCoedCollaborator() throws InvalidConfigFileException, UnknownVersionerTypeException {
		
	}
	
	@Override
	public ICoedObject addObject(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICoedObject getObject(String path) {
		System.out.println("Mock created for:"+path);
		return new MockCoedObject(path);
	}

	@Override
	public String[] getProjectList() {
		// TODO Auto-generated method stub
		String[] x= {"aaa","bbb"};
		return x;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return ICoedCommunicator.STATIC;
	}

	@Override
	public IVersionedObject makeVersionedObject(ICoedObject obj) {
		// TODO Auto-generated method stub
		return (IVersionedObject) new MockCoedObject(obj.getPath());
	}

	@Override
	public void addStateListener(ICollabStateListener stateObserver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getState() {
		// TODO Auto-generated method stub
		return ICoedCollaborator.STATUS_CONNECTED;
	}

	@Override
	public ICollabObject makeCollabObject(ICoedObject obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeStateListener(ICollabStateListener stateObserver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IFuture<ICollabObject[]> getAllOnlineFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void endCollab() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startCollab() {
		// TODO Auto-generated method stub
		
	}

}
