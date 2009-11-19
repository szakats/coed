package coed.collab.server;

import coed.collab.connection.ICoedConnection;
import coed.collab.connection.ICoedConnectionListener;
import coed.collab.protocol.CoedMessage;
import coed.collab.protocol.GetChangesMsg;
import coed.collab.protocol.GetProjectMsg;
import coed.collab.protocol.GetProjectResponseMsg;
import coed.collab.protocol.SendChangesMsg;

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
    	else if(msg instanceof GetProjectMsg) 
    		handleMessage((GetProjectMsg)msg);
    	else
    		System.out.println("unknown message received");
	}
    
    public void handleMessage(GetChangesMsg msg) {
    	System.out.println("get changes");
    }
    
    public void handleMessage(SendChangesMsg msg) {
    	System.out.println("send changes");
    }
    
    public void handleMessage(GetProjectMsg msg) {
    	System.out.println("get project");
    	boolean found_project = true;
    	conn.reply(msg, new GetProjectResponseMsg(found_project));
    }
}
