package coed.collab.server;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.util.SessionLog;

import coed.collab.protocol.*;

public class ServerProtocolHandler extends IoHandlerAdapter {

    public void exceptionCaught(IoSession session, Throwable cause) {

    }

    public void messageReceived(IoSession io, Object message) {
    	CoedMessage msg = (CoedMessage)message;
    	Session session = new Session(io);
    	if(message instanceof GetChangesMsg)
    		handleMessage((GetChangesMsg)msg, session);
    	else if(message instanceof SendChangesMsg)
    		handleMessage((SendChangesMsg)msg, session);
    	else if(message instanceof GetProjectMsg) 
    		handleMessage((GetProjectMsg)msg, session);
    	else
    		System.out.println("unknown message received");
    }
    
    public void handleMessage(GetChangesMsg msg, Session session) {
    	System.out.println("get changes");
    }
    
    public void handleMessage(SendChangesMsg msg, Session session) {
    	System.out.println("send changes");
    }
    
    public void handleMessage(GetProjectMsg msg, Session session) {
    	System.out.println("get project");
    	boolean found_project = true;
    	session.write(new GetProjectResponseMsg(found_project));
    }
    
    public void sessionClosed(IoSession session) throws Exception {
    	System.out.println("session closed");
    }
    
    public void sessionCreated(IoSession session) throws Exception {
    	System.out.println("session created");
    }
}
