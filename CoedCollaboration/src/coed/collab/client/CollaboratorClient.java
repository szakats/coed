package coed.collab.client;

import java.util.ArrayList;
import java.util.HashMap;

import coed.base.comm.ICoedCollaborator;
import coed.base.comm.ICollabStateObserver;
import coed.base.config.ICoedConfig;
import coed.base.data.ICoedObject;
import coed.base.data.ICollabObject;
import coed.base.data.exceptions.NotConnectedToServerException;
import coed.base.util.IFuture;
import coed.collab.connection.CoedKeepAliveConnection;
import coed.collab.connection.ICoedConnection;
import coed.collab.connection.ICoedConnectionListener;
import coed.collab.protocol.CoedMessage;
import coed.collab.protocol.SendChangesMsg;

/**
 * The CollaboratorClient is the Client side of the collaborative editing system.
 * It manages the connection to the collaborative server, keeps track of all the files
 * that a client edits in online mode. It has also a state that specifies if the 
 * Collaborative editing is on, or off.
 * @author Neobi
 *
 */
public class CollaboratorClient implements ICoedCollaborator {
	
	private CoedKeepAliveConnection conn;
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
			stateObs.collabStateChanged(state);
	}
	
	@Override
	public void addStateListener(ICollabStateObserver stateObserver) {
		stateListeners.add(stateObserver);
	}

	@Override
	public void removeStateListener(ICollabStateObserver stateObserver) {
		stateListeners.remove(stateObserver);
	}
	
	/**
	 * Creates a CollabObject from a CoedObject, if called for first time.
	 * If the collabObject is already present in the system, then a reference to
	 * it is returned. Else, the object is created, stored in the cache memory 
	 * of the CollaboratorClient, and the reference is returned. Polymorphically 
	 * creates CollebFile or CollabFolder, depending or wether or not the argument 
	 * given was a file or folder.
	 */
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
	
	/**
	 * Increments the numer of online files. If no file was online until the
	 * method is called, it also initiates a connection to the Collaborative server. 
	 */
	public void incNrOnline() {
		if(nrOnlineFiles == 0) {
			setState(STATUS_ERROR);
			conn = new CoedKeepAliveConnection("localhost", 1234);
			conn.addListener(connListener);
		}
		
		nrOnlineFiles++;
	}
	
	/**
	 * Decrements number of online files. If no file remained online, the method
	 * closes the connection to the Collaborative server as a last action.
	 */
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
	
	public ICoedConnection getConn() {
		return conn;
	}
	
	public String getBasePath(){
		return this.basePath;
	}
	
	public void setBasePath(String basePath){
		this.basePath = basePath;
	}
	
	class ConnectionListener implements ICoedConnectionListener {
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
			if(msg instanceof SendChangesMsg)
				handleMessage((SendChangesMsg)msg);
		}
		
		public void handleMessage(SendChangesMsg msg) {
			ICollabObject obj = cache.get(msg.getFile());
			assert obj instanceof CoedCollabFile;
			CoedCollabFile file = (CoedCollabFile)obj;
			file.notifyChangeListeners(file.getParent());
		}
	}

	@Override
	public IFuture<ICollabObject[]> getAllOnlineFiles() {
		// TODO Auto-generated method stub
		return null;
	}
}
