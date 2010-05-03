package coed.collab.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import coed.base.comm.IAllSessionsListener;
import coed.base.comm.ICoedCollaborator;
import coed.base.comm.ICoedCollaboratorPart;
import coed.base.comm.ICollabStateListener;
import coed.base.config.ICoedConfig;
import coed.base.data.CoedFile;
import coed.base.data.ICoedFile;
import coed.base.data.ICollabFile;
import coed.base.data.ICollabFilePart;
import coed.base.data.exceptions.NotConnectedException;
import coed.base.util.CoedFuture;
import coed.base.util.CoedFuture2;
import coed.base.util.IFuture;
import coed.base.util.IFuture2;
import coed.base.util.IFutureListener;
import coed.collab.connection.CoedKeepAliveConnection;
import coed.collab.connection.ICoedConnection;
import coed.collab.connection.ICoedConnectionListener;
import coed.collab.protocol.AuthenticationReplyMsg;
import coed.collab.protocol.AuthentificationMsg;
import coed.collab.protocol.CoedMessage;
import coed.collab.protocol.FileChangedMsg;
import coed.collab.protocol.CreateSessionMsg;
import coed.collab.protocol.CreateSessionResultMsg;
import coed.collab.protocol.GetCollabSessionsMsg;
import coed.collab.protocol.GetCollabSessionsReplyMsg;
import coed.collab.protocol.JoinReplyMsg;
import coed.collab.protocol.JoinSessionMsg;
import coed.collab.protocol.NewCollabSessionOnServerMsg;
import coed.collab.protocol.RemoveCollabSessionMsg;


/**
 * The CollaboratorClient is the Client side of the collaborative editing system.
 * It manages the connection to the collaborative server, keeps track of all the files
 * that a client edits in online mode. It has also a state that specifies if the 
 * Collaborative editing is on, or off.
 * @author Neobi
 *
 */
public class CollaboratorClient implements ICoedCollaboratorPart {
	
	private CoedKeepAliveConnection conn;
	private int nrOnlineFiles;
	private LinkedList<ICollabStateListener> stateListeners;
	/// cache for storing path-CoedObject pairs
	private String state;
	private ConnectionListener connListener = new ConnectionListener();
	private Map<Integer, ICollabFilePart> cache; 
//	private NewSessionListener newSessionListener;
	private String basePath;
	private LinkedList<IAllSessionsListener> sessionListeners;
	
	private String host;
	private int port;
	
	private ICoedConfig conf;

	public CollaboratorClient(ICoedConfig conf, String basePath) {
		nrOnlineFiles = 0;
		stateListeners = new LinkedList<ICollabStateListener>();
		sessionListeners = new LinkedList<IAllSessionsListener>();
		this.basePath = basePath;
		this.conf = conf;
		cache = new HashMap<Integer, ICollabFilePart>();
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
	
	class ConnectionListener implements ICoedConnectionListener, IFutureListener<CoedMessage>{
		@Override
		public void connected() {
			assert(nrOnlineFiles > 0);
			String username = conf.getString("user.name");
			String password = conf.getString("user.password");
			conn.sendSeq(new AuthentificationMsg(username, password)).addListener(this);
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
			if (msg instanceof NewCollabSessionOnServerMsg)
				handleMessage((NewCollabSessionOnServerMsg)msg);
			if (msg instanceof RemoveCollabSessionMsg)
				handleMessage((RemoveCollabSessionMsg)msg);
		}
		
		public void handleMessage(FileChangedMsg msg) {
			System.out.println("in the collabclient, user is:"+conf.getString("user.name"));
			ICollabFilePart obj = cache.get(msg.getId());
			assert obj instanceof CoedCollabFile;
			CoedCollabFile file = (CoedCollabFile)obj;
			file.notifyChangeListeners(file.getParent());
			
		}
		
		public void handleMessage(NewCollabSessionOnServerMsg msg) {

			for (IAllSessionsListener l:sessionListeners)
				l.sessionAdded(msg.getId(), msg.getPath());
			//System.out.println("new session on server "+msg.getPath());
			
		}
		
		public void handleMessage(RemoveCollabSessionMsg msg) {

			for (IAllSessionsListener l:sessionListeners)
				l.sessionRemoved(msg.getId(), msg.getPath());
			//System.out.println("new session on server "+msg.getPath());
			
		}

		@Override
		public void got(CoedMessage result) {
			if(result instanceof AuthenticationReplyMsg) {
				if( ((AuthenticationReplyMsg)result).getResult() == true) {
					setState(STATUS_CONNECTED);
				} else {
					for(ICollabStateListener l : stateListeners)
						l.authenticationError();
					endCollab();
				}
			}
		}

		@Override
		public void caught(Throwable e) {
			// TODO Auto-generated method stub
			
		}
	}

	@Override
	public IFuture<ICollabFilePart[]> getAllOnlineFiles() {
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
	
	ICollabFilePart makeCollabFile(ICoedFile file, Integer id) {
		CoedCollabFile collab_file = new CoedCollabFile(file, this, id);
		cache.put(id, collab_file);
		return collab_file;
		
	}
	
	@Override
	public IFuture<ICollabFilePart> createCollabSession(String path, String contents, CoedFile file) {
		// listener class for future messages in the sequence
		class FListener implements IFutureListener<CoedMessage> {
			public CoedFuture<ICollabFilePart> ret = new CoedFuture<ICollabFilePart>();
			public CoedFile file;
		
			public FListener(CoedFile file) {
				this.file = file;
			}

			@Override
			public void got(CoedMessage result) {
				if(result instanceof CreateSessionResultMsg) {
					CreateSessionResultMsg msg = ((CreateSessionResultMsg)result);
					ret.set(makeCollabFile(file, msg.getId()));
				} else
					ret.throwEx(new Exception("unknown message received"));
			}

			@Override
			public void caught(Throwable e) {
				// if an error occured anywhere along the sequence
				// then pass the error along to the chained future
				ret.throwEx(e);
			}
		}

		FListener fl = new FListener(file);
		// send the path to the file and its contents
		conn.sendSeq(new CreateSessionMsg(path, contents))
			.addListener(fl); // expects a CreateSessionResultMsg
		return fl.ret;
	}

	@Override
	public IFuture2<ICollabFilePart, String> joinCollabSession(String path, Integer id, CoedFile file) {
		// listener class for future messages in the sequence
		class FListener implements IFutureListener<CoedMessage> {
			public CoedFuture2<ICollabFilePart, String> ret = new CoedFuture2<ICollabFilePart, String>();
			public CoedFile file;
			public Integer id;
		
			public FListener(Integer id, CoedFile file) {
				this.id = id;
				this.file = file;
			}

			@Override
			public void got(CoedMessage result) {
				if(result instanceof JoinReplyMsg) {
					JoinReplyMsg msg = ((JoinReplyMsg)result);
					String remoteContents = msg.getContents();
					System.out.println("got remote contents: " + remoteContents);
					ret.set(makeCollabFile(file, id), remoteContents);
				}
			}

			@Override
			public void caught(Throwable e) {
				// if an error occured anywhere along the sequence
				// then pass the error along to the chained future
				ret.throwEx(e);
			}
		}

		FListener fl = new FListener(id, file);
		// first send which file should be shared
		conn.sendSeq(new JoinSessionMsg(id))
			.addListener(fl); // expects a GoOnlineResultMsg
		return fl.ret;
	}

	@Override
	public IFuture<Map<Integer, String>> getCollabSessions() {
		class FListener implements IFutureListener<CoedMessage> {
			public CoedFuture<Map<Integer, String>> ret = new CoedFuture<Map<Integer, String>>();

			@Override
			public void got(CoedMessage result) {
				if(result instanceof GetCollabSessionsReplyMsg) {
					ret.set(((GetCollabSessionsReplyMsg)result).getSessions());
				} else
					ret.throwEx(new Exception("unknown message received"));
			}

			@Override
			public void caught(Throwable e) {
				// if an error occured anywhere along the sequence
				// then pass the error along to the chained future
				ret.throwEx(e);
			}
		}

		FListener fl = new FListener();
		// send the path to the file and its contents
		conn.sendSeq(new GetCollabSessionsMsg())
			.addListener(fl); // expects a CreateSessionResultMsg
		return fl.ret;
	}

	@Override
	public void addAllSessionsListener(IAllSessionsListener listener) {
		sessionListeners.add(listener);
		
	}

	@Override
	public void removeAllSessionsListener(IAllSessionsListener listener) {
		sessionListeners.remove(listener);
		
	}
}
