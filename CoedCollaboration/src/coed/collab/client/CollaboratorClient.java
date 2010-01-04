package coed.collab.client;

import java.util.HashMap;
import java.util.LinkedList;

import coed.base.comm.ICoedCollaborator;
import coed.base.comm.ICollabStateListener;
import coed.base.config.ICoedConfig;
import coed.base.data.ICoedObject;
import coed.base.data.ICollabObject;
import coed.base.data.exceptions.NotConnectedException;
import coed.base.util.IFuture;
import coed.collab.connection.CoedKeepAliveConnection;
import coed.collab.connection.ICoedConnection;
import coed.collab.connection.ICoedConnectionListener;
import coed.collab.protocol.AuthentificationMsg;
import coed.collab.protocol.CoedMessage;
import coed.collab.protocol.FileChangedMsg;
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
	private LinkedList<ICollabStateListener> stateListeners;
	/// cache for storing path-CoedObject pairs
	private HashMap<String,ICollabObject> cache;
	private String state;
	private ConnectionListener connListener = new ConnectionListener();
	
	private String basePath;
	
	private String host;
	private int port;
	
	private ICoedConfig conf;

	public CollaboratorClient(ICoedConfig conf, String basePath) {
		nrOnlineFiles = 0;
		stateListeners = new LinkedList<ICollabStateListener>();
		cache = new HashMap<String,ICollabObject>();
		this.basePath = basePath;
		this.conf = conf;
		setState(STATUS_OFFLINE);

		host = conf.getString("server.host");
		port = conf.getInt("server.port");
	}
	
	public void ensureConnected() throws NotConnectedException {
		if(getState() != STATUS_CONNECTED)
			throw new NotConnectedException();
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
		// some listeners may be removed while iterating so clone the list
		// TODO: make a container which solves this problem more efficiently
		LinkedList<ICollabStateListener> clonedList = (LinkedList<ICollabStateListener>)stateListeners.clone();
		for(ICollabStateListener stateObs : clonedList)
			stateObs.collabStateChanged(state);
	}
	
	@Override
	public void addStateListener(ICollabStateListener stateObserver) {
		stateListeners.add(stateObserver);
	}

	@Override
	public void removeStateListener(ICollabStateListener stateObserver) {
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
	
	public ICoedConnection getConn() {
		return conn;
	}
	
	public ICoedConfig getConf() {
		return conf;
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
			String username = conf.getString("user.name");
			conn.send(new AuthentificationMsg(username));
			// TODO: state should only be set to connected
			//		 if the auth was successful
			setState(STATUS_CONNECTED);
		}

		@Override
		public void disconnected() {
			if(nrOnlineFiles > 0) setState(STATUS_ERROR);
			else setState(STATUS_OFFLINE);
		}

		@Override
		public void received(CoedMessage msg) {
			if(msg instanceof FileChangedMsg)
				handleMessage((FileChangedMsg)msg);
		}
		
		public void handleMessage(FileChangedMsg msg) {
			ICollabObject obj = cache.get(msg.getFileName());
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

	@Override
	public void endCollab() {
		if(conn != null) {
			conn.shutdown();
			conn = null;
			setState(STATUS_OFFLINE);
		}
	}

	@Override
	public void startCollab() {
		if(conn == null) {
			setState(STATUS_ERROR);
			conn = new CoedKeepAliveConnection(host, port);
			conn.addListener(connListener);
		}
	}
}
