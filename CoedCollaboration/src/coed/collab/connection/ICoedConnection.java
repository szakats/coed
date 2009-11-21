package coed.collab.connection;

import coed.base.data.exceptions.NotConnectedException;
import coed.base.util.IFuture;
import coed.collab.protocol.CoedMessage;

public interface ICoedConnection {
	
    public void addListener(ICoedConnectionListener listener);
    
    public void removeListener(ICoedConnectionListener listener);
    
    public void send(CoedMessage msg) 
    	throws NotConnectedException;
    
    public IFuture<CoedMessage> sendF(CoedMessage msg) 
    	throws NotConnectedException;
    
    public void reply(CoedMessage to, CoedMessage with)
    	throws NotConnectedException;
    
    public IFuture<CoedMessage> replyF(CoedMessage to, CoedMessage with)
    	throws NotConnectedException;
    
	public boolean isConnected();
}
