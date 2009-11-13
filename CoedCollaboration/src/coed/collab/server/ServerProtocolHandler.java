package coed.collab.server;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.util.SessionLog;

import coed.collab.protocol.CoedMessage;
import coed.collab.protocol.GetChangesMsg;
import coed.collab.protocol.SendChangesMsg;

public class ServerProtocolHandler extends IoHandlerAdapter {

    public void exceptionCaught(IoSession session, Throwable cause) {

    }

    public void messageReceived(IoSession session, Object message) {
    	CoedMessage msg = (CoedMessage)message;
    	if(message instanceof GetChangesMsg)
    		handleMessage((GetChangesMsg)msg);
    	else if(message instanceof SendChangesMsg)
    		handleMessage((SendChangesMsg)msg);
    	else
    		System.out.println("unknown message received");
    }
    
    public void handleMessage(GetChangesMsg msg) {
    	System.out.println("get changes");
    }
    
    public void handleMessage(SendChangesMsg msg) {
    	System.out.println("send changes");
    }
    
    public void sessionClosed(IoSession session) throws Exception {
    	
    }
}
