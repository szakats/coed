package coed.collab.connection;

import coed.base.util.IFuture;
import coed.collab.protocol.CoedMessage;

public interface ICoedConnection {
	
    public void addListener(ICoedConnectionListener listener);
    
    public void removeListener(ICoedConnectionListener listener);
    
    public void send(CoedMessage msg);
    
    public IFuture<CoedMessage> sendF(CoedMessage msg);
    
    public void reply(CoedMessage to, CoedMessage with);
    
    public IFuture<CoedMessage> replyF(CoedMessage to, CoedMessage with);
    
	public boolean isConnected();
}
