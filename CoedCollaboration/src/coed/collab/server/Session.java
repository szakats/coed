package coed.collab.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import coed.base.data.TextModification;
import coed.base.util.IFutureListener;
import coed.collab.connection.ICoedConnection;
import coed.collab.connection.ICoedConnectionListener;
import coed.collab.protocol.*;

public class Session implements ICoedConnectionListener {
	
	private ICoedConnection conn;
	CollaboratorServer server;
	private HashSet<ServerFile> onlineFiles = new HashSet<ServerFile>();
	private String userName;
	
	/** FileChangeListeners mapped with the fileName as the key**/
	private HashMap<String,FileChangedListener> listeners;
	
	public Session(ICoedConnection conn, CollaboratorServer server) {
		this.conn = conn;
		this.server = server;
		this.listeners = new HashMap<String,FileChangedListener>();
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
			file.removeSession(this);
			if(file.getNrOfSessions() == 0)
				server.removeServerFile(file.getPath());
			it.remove();
		}	
	}
	
	private int x = 0;

	@Override
	public void received(CoedMessage msg) {
		if(x == 1) {
			System.out.println("concurrent");
		}
		x = 1;
    	if(msg instanceof GetChangesMsg)
    		handleMessage((GetChangesMsg)msg);
    	else if(msg instanceof SendChangesMsg)
    		handleMessage((SendChangesMsg)msg);
    	else if(msg instanceof GoOnlineMsg)
    		handleMessage((GoOnlineMsg)msg);
    	else if(msg instanceof GetContentsMsg)
    		handleMessage((GetContentsMsg)msg);
    	else if (msg instanceof AddChangedListenerMsg)
    		handleMessage((AddChangedListenerMsg) msg);
    	else if (msg instanceof AuthentificationMsg)
    		handleMessage((AuthentificationMsg) msg);
    	else if (msg instanceof RequestLockMsg)
    		handleMessage((RequestLockMsg) msg);
    	else if (msg instanceof ReleaseLockMsg)
    		handleMessage((ReleaseLockMsg) msg);
    	else if (msg instanceof GetUserListMsg)
    		handleMessage((GetUserListMsg) msg);
    	else if( msg instanceof GoOfflineMsg)
    		handleMessage((GoOfflineMsg)msg);
    	x = 0;
	}
	
    public void handleMessage(GoOfflineMsg msg) {
    	System.out.println("going offline with file " + msg.getFileName());
    	ServerFile file =  server.getServerFile(msg.getFileName());
		file.removeSession(this);
		if(file.getNrOfSessions() == 0)
			server.removeServerFile(file.getPath());
    }
    
    public void handleMessage(GetChangesMsg msg) {
    	System.out.println("get changes request for session "+getUserName());
    	GetChangesReplyMsg reply = new GetChangesReplyMsg(server.getServerFile(msg.getFileName()).getChangesFor(this));
    	for (TextModification t : reply.getMods())
    		System.out.println(t.toString());
    	conn.reply(msg, reply);
    }
    
    public void handleMessage(AuthentificationMsg msg) {
    	System.out.println("authentification");
    	setUserName(msg.getUserName());
    }
    
    public void handleMessage(SendChangesMsg msg) {
    	System.out.println("send changes");
    	ServerFile sf = server.getServerFile(msg.getFile());
    	for (int i=0; i<msg.getMods().length; i++){
    		sf.addChange(msg.getMods()[i], this);
    	}
    	//System.out.println(sf.getCurrentContents());
    }
    
    public void handleMessage(RequestLockMsg msg) {
    	System.out.println("requesting lock");
    	RequestLockReplyMsg reply = new RequestLockReplyMsg(msg.getFile(),server.getServerFile(msg.getFile()).RequestLock(msg.getPortion(), this));
    	conn.reply(msg, reply);
    }
    
    public void handleMessage(ReleaseLockMsg msg) {
    	System.out.println("releasing lock");
    	server.getServerFile(msg.getFile()).ReleaseLock(msg.getPortion(), this);
    }
    
    public void handleMessage(GetUserListMsg msg) {
    	System.out.println("requesting user list lock");
    	GetUserListReplyMsg reply = new GetUserListReplyMsg(msg.getFile(),server.getServerFile(msg.getFile()).getActiveUsers());
    	conn.reply(msg, reply);
    }
    
    public void handleMessage(GetContentsMsg msg) {
    	System.out.println("get contents");
    	
    	String contents = server.getServerFile(msg.getFileName()).getCurrentContents();
		conn.reply(msg, new SendContentsMsg(contents));
		// TODO: handle the error
    }
    
    public void handleMessage(AddChangedListenerMsg msg){
    	FileChangedListener listener = new FileChangedListener(msg.getFile(),this);
    	listeners.put(msg.getFile(), listener);
    	server.getServerFile(msg.getFile()).addChangeListener(this, listener);
    }
    
    public void handleMessage(GoOnlineMsg msg) {
    	System.out.println("go online");
    	
    	class FListener implements IFutureListener<CoedMessage> {
    		private String fileName;
    		private boolean isOnline;
    		
			public FListener(GoOnlineMsg msg) {
				this.fileName = msg.getFileName();
		
				isOnline = server.isFileOnline(fileName);	
				
				if(!isOnline){
					conn.replySeq(msg, new GoOnlineResultMsg(false)).addListener(this);
				}
				else
				{
					ServerFile sf = server.getServerFile(fileName);
					onlineFiles.add(sf);
					sf.addSession(Session.this);
					conn.reply(msg, new GoOnlineResultMsg(true)).addErrorListener(this);
				}
			}

			@Override
			public void got(CoedMessage result) {
				if(result instanceof SendContentsMsg) {
					String contents = ((SendContentsMsg)result).getContents();
					//System.out.println("got contents " + contents);
					try{
						server.addNewFile(fileName,((SendContentsMsg)result).getContents());
						ServerFile sf = server.getServerFile(fileName);
						onlineFiles.add(sf);
						sf.addSession(Session.this);
					}
					catch(IOException ex) {}
				}
			}

			@Override
			public void caught(Throwable e) {
				// TODO: handle the error
			}
    	}
    	
    	new FListener(msg);
    }
}
