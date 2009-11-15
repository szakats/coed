package coed.collab.client;

import java.util.ArrayList;

import coed.base.common.ICoedCollaborator;
import coed.base.common.ICoedObject;
import coed.base.common.ICollabObject;
import coed.base.common.ICollabStateObserver;
import coed.base.data.exceptions.NotConnectedToServerException;
import coed.collab.client.config.ICoedConfig;

public class CollaboratorClient implements ICoedCollaborator {
	
	private ServerConnection conn;
	private int nrOnlineFiles;
	private ArrayList<ICollabStateObserver> stateListeners;
	
	String host;
	int port;

	public CollaboratorClient(ICoedConfig conf) {
		nrOnlineFiles = 0;
		stateListeners = new ArrayList<ICollabStateObserver>();
		//goOffline();
		
		//host = conf.getString("server.host");
		//port = conf.getInt("server.port");
	}
	
	public void ensureConnected() throws NotConnectedToServerException {
		if(getState() != STATUS_CONNECTED)
			throw new NotConnectedToServerException();
	}

	@Override
	public String getState() {
		if(nrOnlineFiles == 0) return STATUS_OFFLINE;
		return conn.isConnected() ? STATUS_CONNECTED : STATUS_ERROR;
	}
	
	@Override
	public void addStateListener(ICollabStateObserver stateObserver) {
		stateListeners.add(stateObserver);
		
	}

	@Override
	public void removeStateListener(ICollabStateObserver stateObserver) {
		stateListeners.remove(stateObserver);
	}
	
	public void notifyObservers(){
		
	}

	@Override
	public ICollabObject makeCollabObject(ICoedObject obj) {
		
		if(obj.isFile()) {
			return new CoedCollabFile(obj, this);
		} else
			return new CoedCollabFolder(obj, this);
	}
	
	public void incNrOnline() {
		if(nrOnlineFiles == 0) {
			conn = new ServerConnection("localhost", 1234);
		}
		
		nrOnlineFiles++;
	}
	
	public void decNrOnline() {
		nrOnlineFiles--;
		if(nrOnlineFiles == 0) {
			if(conn != null) {
				conn.shutdown();
				conn = null;
			}
		}
	}
}
