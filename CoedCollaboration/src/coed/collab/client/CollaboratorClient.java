package coed.collab.client;

import coed.base.common.ICoedCollaborator;
import coed.base.common.ICollabStateObserver;
import coed.base.data.CoedFile;
import coed.base.data.CoedFileLine;
import coed.base.data.CoedFileLock;
import coed.base.data.IFileObserver;
import coed.base.data.exceptions.NotConnectedToServerException;
import coed.collab.client.config.ICoedConfig;
import coed.collab.protocol.SendChangesMsg;

public class CollaboratorClient implements ICoedCollaborator {
	
	private ServerConnection conn;
	private boolean isWorkingOnline;
	
	String host;
	int port;

	public CollaboratorClient(ICoedConfig conf) {
		goOffline();
		
		//host = conf.getString("server.host");
		//port = conf.getInt("server.port");
	}
	
	public void ensureConnected() throws NotConnectedToServerException {
		if(getState() != STATUS_CONNECTED)
			throw new NotConnectedToServerException();
	}

	@Override
	public String[] getActiveUsers(CoedFile file) throws NotConnectedToServerException {
		ensureConnected();

		return null;
	}

	@Override
	public CoedFileLine[] getChanges(CoedFile file) throws NotConnectedToServerException {
		ensureConnected();
		
		return null;
	}

	@Override
	public String getState() {
		if(!isWorkingOnline) return STATUS_OFFLINE;
		return conn.isConnected() ? STATUS_CONNECTED : STATUS_ERROR;
	}

	@Override
	public boolean releaseLock(CoedFileLock lock) throws NotConnectedToServerException {
		ensureConnected();
		
		return false;
	}

	@Override
	public boolean requestLock(CoedFileLock lock) throws NotConnectedToServerException {
		ensureConnected();
		
		return false;
	}

	@Override
	public boolean sendChanges(CoedFile file, CoedFileLine line) throws NotConnectedToServerException {
		ensureConnected();
		new SendChangesMsg(file, line); // TODO: send it
		return false;
	}

	@Override
	public void addChangeListener(IFileObserver fileObserver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addFileChangeListener(CoedFile file,
			IFileObserver fileObserver) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeChangeListener(IFileObserver fileObserver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addStateListener(ICollabStateObserver stateObserver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeStateListener(ICollabStateObserver stateObserver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goOffline() {
		isWorkingOnline = false;

		if(conn != null) {
			conn.shutdown();
			conn = null;
		}
	}

	@Override
	public void goOnline() {
		isWorkingOnline = true;
		conn = new ServerConnection("localhost", 1234);
		
		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		
		conn.send(new SendChangesMsg(null, null));*/
	}

}
