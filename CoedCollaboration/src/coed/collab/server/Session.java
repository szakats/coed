package coed.collab.server;

import java.io.IOException;

import coed.base.data.exceptions.NotConnectedException;
import coed.base.util.IFutureListener;
import coed.collab.connection.ICoedConnection;
import coed.collab.connection.ICoedConnectionListener;
import coed.collab.protocol.*;

public class Session implements ICoedConnectionListener {
	private ICoedConnection conn;
	CollaboratorServer server;
	
	public Session(ICoedConnection conn, CollaboratorServer server) {
		this.conn = conn;
		this.server = server;
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
		System.out.println("session closed");
	}

	@Override
	public void received(CoedMessage msg) {
    	if(msg instanceof GetChangesMsg)
    		handleMessage((GetChangesMsg)msg);
    	else if(msg instanceof SendChangesMsg)
    		handleMessage((SendChangesMsg)msg);
    	else if(msg instanceof GoOnlineMsg)
    		handleMessage((GoOnlineMsg)msg);
    	else if(msg instanceof GetContentsMsg)
    		handleMessage((GetContentsMsg)msg);
	}
    
    public void handleMessage(GetChangesMsg msg) {
    	System.out.println("get changes");
    }
    
    public void handleMessage(SendChangesMsg msg) {
    	System.out.println("send changes");
    }
    
    public void handleMessage(GetContentsMsg msg) {
    	System.out.println("get contents");
    	
    	String contents = "foo"; // TODO: get contents
		conn.reply(msg, new SendContentsMsg(contents));
		// TODO: handle the error
    }
    
    public void handleMessage(GoOnlineMsg msg) {
    	System.out.println("go online");
    	
    	class FListener implements IFutureListener<CoedMessage> {
    		private String fileName;
    		private boolean isOnline;
    		
			public FListener(GoOnlineMsg msg) {
				this.fileName = msg.getFileName();
		
				isOnline = server.isFileOnline(fileName);	
				
				if(!isOnline)
					conn.replySeq(msg, new GoOnlineResultMsg(false)).addListener(this);
				else
					conn.reply(msg, new GoOnlineResultMsg(true)).addErrorListener(this);
			}

			@Override
			public void got(CoedMessage result) {
				if(result instanceof SendContentsMsg) {
					String contents = ((SendContentsMsg)result).getContents();
					System.out.println("got contents " + contents);
					try{
					server.addNewFile(fileName,((SendContentsMsg)result).getContents());
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
