package coed.collab.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import coed.base.data.TextModification;
import coed.base.data.exceptions.NoSuchSessionException;
import coed.base.util.IFutureListener;
import coed.collab.connection.ICoedConnection;
import coed.collab.connection.ICoedConnectionListener;
import coed.collab.protocol.*;

public class Session implements ICoedConnectionListener {
	
	private ICoedConnection conn;
	CollaboratorServer server;
	private HashSet<ServerFile> onlineFiles = new HashSet<ServerFile>();
	private String userName;
	
	private boolean auth = false;
	
	/** FileChangeListeners mapped with the fileName as the key**/
	private HashMap<Integer,FileChangedListener> fileChangelisteners;
	
	public Session(ICoedConnection conn, CollaboratorServer server) {
		this.conn = conn;
		this.server = server;
		this.fileChangelisteners = new HashMap<Integer,FileChangedListener>();
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public void setUserName(String user){
		this.userName = user;
	}
	
	public ICoedConnection getConn() {
		return conn;
	}

	@Override
	public void connected() {
		System.out.println("session created");
	}

	@Override
	public void disconnected() {
		System.out.println("session ending");
		Iterator<ServerFile> it = onlineFiles.iterator();
		while (it.hasNext()){
			ServerFile file = ((ServerFile)it.next());
			removeFromFile(file);
			it.remove();
		}	
		server.unregisterSession(this);	
	}
	
	@Override
	public void received(CoedMessage msg) {
		if(auth == false) {
			if (msg instanceof AuthentificationMsg)
				handleMessage((AuthentificationMsg) msg);
		} else {
	    	if(msg instanceof GetChangesMsg)
	    		handleMessage((GetChangesMsg)msg);
	    	else if(msg instanceof SendChangesMsg)
	    		handleMessage((SendChangesMsg)msg);
	    	else if(msg instanceof CreateSessionMsg)
	    		handleMessage((CreateSessionMsg)msg);
	    	else if(msg instanceof GetContentsMsg)
	    		handleMessage((GetContentsMsg)msg);
	    	else if (msg instanceof AddChangedListenerMsg)
	    		handleMessage((AddChangedListenerMsg) msg);
	    	else if (msg instanceof RequestLockMsg)
	    		handleMessage((RequestLockMsg) msg);
	    	else if (msg instanceof ReleaseLockMsg)
	    		handleMessage((ReleaseLockMsg) msg);
	    	else if (msg instanceof GetUserListMsg)
	    		handleMessage((GetUserListMsg) msg);
	    	else if( msg instanceof GoOfflineMsg)
	    		handleMessage((GoOfflineMsg)msg);
	    	else if( msg instanceof GetCollabSessionsMsg)
	    		handleMessage((GetCollabSessionsMsg)msg);
	    	else if( msg instanceof UserSessionListenerMsg)
	    		handleMessage((UserSessionListenerMsg)msg);
	    	else if( msg instanceof JoinSessionMsg)
	    		handleMessage((JoinSessionMsg)msg);
	    	
		}
	}
	
    public void handleMessage(UserSessionListenerMsg msg) {
    	ServerFile file = server.getFileManager().getFile(msg.getSessionId());
    	if(file == null) {
    		System.out.println("no file by the given id found (" + msg.getSessionId() + ")");
    		return;
    	}
    	if(msg.getAdd()) {
    		System.out.println("adding user listener for session " + msg.getSessionId());
    		file.addUserChangeListener(this);
    	} else {
    		System.out.println("removing user listener for session " + msg.getSessionId());
    		file.removeUserChangeListener(this);
    	}
    }
	
	void removeFromFile(ServerFile file) {
		file.removeSession(this);
		// tell others that I am being removed
		for(Session s : file.getUserChangeListeners())
			s.getConn().send(new UserSessionChangeMsg(file.getId(), getUserName(), false));
			
		if(file.getNrOfSessions() == 0)
		{
			server.removeServerFile(file.getId());
			server.broadcast(new RemoveCollabSessionMsg(file.getId(), file.getPath()));
		}
	}
	
    public void handleMessage(GoOfflineMsg msg) {
    	System.out.println("going offline with file " + msg.getId());
    	ServerFile file =  server.getServerFile(msg.getId());
    	onlineFiles.remove(file);
		removeFromFile(file);
    }
    
    public void handleMessage(GetChangesMsg msg) {
    	System.out.println("get changes request for session "+getUserName());
    	GetChangesReplyMsg reply = new GetChangesReplyMsg(server.getServerFile(msg.getId()).getChangesFor(this));
    	for (TextModification t : reply.getMods())
    		System.out.println(t.toString());
    	conn.reply(msg, reply);
    }
    
    public void handleMessage(AuthentificationMsg msg) {
    	
    	System.out.println("authentification");
    	boolean result = server.validateUser(msg.getUserName(), msg.getPassword());
    	
    	conn.reply(msg, new AuthenticationReplyMsg(result));
    	if(result == true) {
    		auth = true;
    		setUserName(msg.getUserName());
    		server.registerSession(this);
    	}
    }
    
    public void handleMessage(SendChangesMsg msg) {
    	System.out.println("send changes");
    	ServerFile sf = server.getServerFile(msg.getId());
    	for (int i=0; i<msg.getMods().length; i++){
    		sf.addChange(msg.getMods()[i], this);
    	}
    	//System.out.println(sf.getCurrentContents());
    }
    
    public void handleMessage(RequestLockMsg msg) {
    	System.out.println("requesting lock");
    	RequestLockReplyMsg reply = new RequestLockReplyMsg(msg.getId(),server.getServerFile(msg.getId()).RequestLock(msg.getPortion(), this));
    	conn.reply(msg, reply);
    }
    
    public void handleMessage(ReleaseLockMsg msg) {
    	System.out.println("releasing lock");
    	server.getServerFile(msg.getId()).ReleaseLock(msg.getPortion(), this);
    }
    
    public void handleMessage(GetUserListMsg msg) {
    	System.out.println("requesting user list");
    	GetUserListReplyMsg reply = new GetUserListReplyMsg(msg.getId(),server.getServerFile(msg.getId()).getActiveUsers());
    	conn.reply(msg, reply);
    }
    
    public void handleMessage(GetContentsMsg msg) {
    	System.out.println("get contents");
    	
    	String contents = server.getServerFile(msg.getId()).getCurrentContents();
		conn.reply(msg, new SendContentsMsg(contents));
		// TODO: handle the error
    }
    
    public void handleMessage(AddChangedListenerMsg msg){
    	FileChangedListener listener = new FileChangedListener(msg.getId(),this);
    	fileChangelisteners.put(msg.getId(), listener);
    	server.getServerFile(msg.getId()).addChangeListener(this, listener);
    }
    
    public void handleMessage(CreateSessionMsg msg) {
    	Integer id = server.createSession(msg.getFileName(), msg.getContents(), this);
    	onlineFiles.add(server.getFileManager().getFile(id));
    	conn.reply(msg, new CreateSessionResultMsg(id));
    	
    	
    	//send notification to all clients that a new session is on server
    	// TODO: not good..we need all the connected users, regardless of the fact that they are registered to a file or not.
  
    	server.broadcast(new NewCollabSessionOnServerMsg(id,msg.getFileName()));
    
    	System.out.println("go online with " + msg.getFileName() + " (" + id + ")");
    }
    
    public void handleMessage(JoinSessionMsg msg) {
    	System.out.println(getUserName() + " is joining session " + msg.getId());

    	if(!server.existsSession(msg.getId())) {
    		System.out.println("Id " + msg.getId() + "does not exist");
    		conn.reply(msg, new NoSuchSessionException());
    	} else {
    		String contents = server.joinSession(msg.getId(),this);
    		conn.reply(msg, new JoinReplyMsg(contents));
    		onlineFiles.add(server.getFileManager().getFile(msg.getId()));
    	}
    }
    
    public void handleMessage(GetCollabSessionsMsg msg) {
    	System.out.println("sending list of sessions to " + getUserName());
    	GetCollabSessionsReplyMsg ret = new GetCollabSessionsReplyMsg();
    	for(ServerFile s : server.getFileManager().getServerFiles()) {
    		ret.addSession(s.getId(), s.getPath());
    	}
    	
    	conn.reply(msg, ret);
    }
    

}
