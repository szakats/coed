package coed.collab.client;

import java.util.ArrayList;
import java.util.HashMap;

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
	/// cache for storing path-CoedObject pairs
	private HashMap<String,ICollabObject> cache;
	
	private String basePath;
	
	String host;
	int port;

	public CollaboratorClient(ICoedConfig conf, String basePath) {
		nrOnlineFiles = 0;
		stateListeners = new ArrayList<ICollabStateObserver>();
		cache = new HashMap<String,ICollabObject>();
		this.basePath = basePath;
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
		if (! cache.containsKey(obj.getPath() )){
			if(obj.isFile()) {
				return new CoedCollabFile(obj, this);
			} else
				return new CoedCollabFolder(obj, this);
		}
		else return cache.get(obj.getPath());
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
	
	/**
	 * For debugging purposes only!
	 * @return
	 */
	public ServerConnection getConn() {
		return conn;
	}
	
	public String getBasePath(){
		return this.basePath;
	}
	
	public void setBasePath(String basePath){
		this.basePath = basePath;
	}
}
