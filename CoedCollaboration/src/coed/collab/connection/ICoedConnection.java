package coed.collab.connection;

import coed.base.util.IFuture;
import coed.collab.protocol.CoedMessage;

public interface ICoedConnection {
	
    public void addListener(ICoedConnectionListener listener);
    
    public void removeListener(ICoedConnectionListener listener);
    
    public IFuture<Void> send(CoedMessage msg);
    
    public IFuture<CoedMessage> sendSeq(CoedMessage msg);
    
    public IFuture<Void> reply(CoedMessage to, CoedMessage with);
    
    public IFuture<CoedMessage> replySeq(CoedMessage to, CoedMessage with);
    
	public boolean isConnected();
	
	public void disconnect();
}
