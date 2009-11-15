package coed.collab.client;

import java.util.ArrayList;
import java.util.HashMap;

import coed.base.common.ICoedCollaborator;
import coed.base.common.ICoedObject;
import coed.base.common.ICollabObject;
import coed.base.common.ICollabStateObserver;
import coed.base.data.exceptions.NotConnectedToServerException;
import coed.collab.client.config.ICoedConfig;
import coed.collab.protocol.CoedMessage;
import coed.collab.protocol.SendChangesMsg;

public class CollaboratorClient implements ICoedCollaborator {
	
	private ServerConnection conn;
	private int nrOnlineFiles;
	private ArrayList<ICollabStateObserver> stateListeners;
	/// cache for storing path-CoedObject pairs
	private HashMap<String,ICollabObject> cache;
	private String state;
	private ConnectionListener connListener = new ConnectionListener();
	
	private String basePath;
	
	String host;
	int port;

	public CollaboratorClient(ICoedConfig conf, String basePath) {
		nrOnlineFiles = 0;
		stateListeners = new ArrayList<ICollabStateObserver>();
		cache = new HashMap<String,ICollabObject>();
		this.basePath = basePath;
		setState(STATUS_OFFLINE);
		//goOffline();
		
		//host = conf.getString("server.host");
		//port = conf.getInt("server.port");
	}
	
	public void ensureConnected() throws NotConnectedToServerException {
		if(getState() != STATUS_CONNECTED)
			throw new NotConnectedToServerException();
	}

	@Override
	public synchronized String getState() {
		return state;
	}
	
	public void setState(String state) {
		synchronized(this) {
			this.state = state;
		}
		// must not synchronize the rest (observers are allowed to call getState)
		for(ICollabStateObserver stateObs : stateListeners)
			stateObs.update();
	}
	
	@Override
	public void addStateListener(ICollabStateObserver stateObserver) {
		stateListeners.add(stateObserver);
	}

	@Override
	public void removeStateListener(ICollabStateObserver stateObserver) {
		stateListeners.remove(stateObserver);
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
			setState(STATUS_ERROR);
			conn = new ServerConnection("localhost", 1234);
			conn.addListener(connListener);
		}
		
		nrOnlineFiles++;
	}
	
	public void decNrOnline() {
		nrOnlineFiles--;
		
		if(nrOnlineFiles == 0) {
			if(conn != null) {
				conn.shutdown();
				conn = null;
				setState(STATUS_OFFLINE);
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
	
	class ConnectionListener implements ServerConnection.Listener {
		@Override
		public void connected() {
			assert(nrOnlineFiles > 0);
			setState(STATUS_CONNECTED);
		}

		@Override
		public void disconnected() {
			if(nrOnlineFiles > 0) setState(STATUS_ERROR);
			else setState(STATUS_OFFLINE);
		}

		@Override
		public void received(CoedMessage msg) {
			if(msg instanceof SendChangesMsg) {
				
			}
		}
	}
}
