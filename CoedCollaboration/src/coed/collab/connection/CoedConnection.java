package coed.collab.connection;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.WriteFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;

import coed.base.data.exceptions.NotConnectedException;
import coed.base.util.CoedFuture;
import coed.base.util.IFuture;
import coed.collab.protocol.CoedMessage;

public class CoedConnection extends IoHandlerAdapter implements ICoedConnection {
	private IoSession io;
	private long curSequenceID;
	
	/**
	 * allListeners contains listeners that listen for all incoming
	 * messages while seqListeners contains those which wait for
	 * the continuation of a particular sequence.
	 */
	LinkedList<ICoedConnectionListener> allListeners = new LinkedList<ICoedConnectionListener>();
	HashMap<Long, ICoedConnectionListener> seqListeners = new HashMap<Long, ICoedConnectionListener>();
	
	public static IFuture<CoedConnection> connect(String host, int port) {

    	class CoedConnectFuture extends CoedFuture<CoedConnection> {
    		private CoedConnection conn = new CoedConnection();
    		private ConnectFuture future;
    		
    		public CoedConnectFuture(String host, int port) {
    			SocketConnector connector = new SocketConnector();
    			SocketConnectorConfig config = new SocketConnectorConfig();
    			
    			DefaultIoFilterChainBuilder chain = config.getFilterChain();
    	        chain.addLast("codec", new ProtocolCodecFilter(
    	                new ObjectSerializationCodecFactory()));
    	    	future = connector.connect(new InetSocketAddress(host, port), conn, config);
    	    	
    	    	new CoedConnectThread().start();
    		}
    		
    		class CoedConnectThread extends Thread {
    			public void run() {
    				future.join();
    				if (!future.isConnected()) {
    					set(null);
    				} else {
    					conn.init(future.getSession());
    					set(conn);
    				}
    			}
    		}
    	}
    	
        return new CoedConnectFuture(host, port);
	}
	
	public static CoedConnection bind(IoSession io) {
		CoedConnection ret = new CoedConnection();
		ret.init(io);
		return ret;
	}
	
	private CoedConnection() {
		curSequenceID = 0;
	}
	
	private void init(IoSession io) {
		this.io = io;
	}
	
	@Override
    public void exceptionCaught(IoSession session, Throwable cause) {
		cause.printStackTrace();
    }

	/**
	 * The messageReceived function is called by MINA when a message arrives
	 * for a particular session
	 * @param session the session which received the message
	 * @message the message that was received
	 */
	@Override
    public void messageReceived(IoSession session, Object message) {
    	assert(session != null && message != null && message instanceof CoedMessage);
    	CoedMessage msg = (CoedMessage)message;
    	
    	synchronized(this) {
    		// make sure all new messages will not use a sequence id
    		// the the other peer knows about
    		curSequenceID = Math.max(curSequenceID, msg.getSequenceID() + 1);
    	}
    	
    	// get the list of listeners for this particular sequence if there is one
    	long id = msg.getSequenceID();
    	ICoedConnectionListener l;
    	synchronized(this) {
	    	l = seqListeners.remove(new Long(id));
    	}
    	
	    if(l != null)
	    	l.received(msg);
	    else
	    	for(ICoedConnectionListener cl : allListeners)
	    		cl.received(msg);
    }
    
	@Override
    public void sessionClosed(IoSession session) {
    	System.out.println("connection closed.");
		for(ICoedConnectionListener listener : allListeners)
			listener.disconnected();
    }
	
	
	
    private synchronized boolean write(CoedMessage msg, long sequenceID) {
    	if(!isConnected())
    		return false;
    	
    	msg.setSequenceID(sequenceID);
    	io.write(msg);
    	return true;
    }
    
    private synchronized boolean send(CoedMessage msg, long sequenceID, CoedFuture<CoedMessage> future) {
    	boolean success = write(msg, sequenceID);
    	if(!success)
    		future.throwEx(new NotConnectedException());
    	return success;
    }
    
    private synchronized CoedFuture<Void> send(CoedMessage msg, long sequenceID) {
    	if(write(msg, sequenceID))
    		return new CoedFuture<Void>((Void)null);
    	else
    		return new CoedFuture<Void>(new NotConnectedException()); 
    }
    
    private synchronized IFuture<CoedMessage> sendSeq(CoedMessage msg, long sequenceID) {
    	
    	CoedFuture<CoedMessage> future = new CoedFuture<CoedMessage>();
    	
    	if(send(msg, sequenceID, future)) {	    	
    		class ReplyListener implements ICoedConnectionListener {
	    		CoedFuture<CoedMessage> future;
	    		
	    		public ReplyListener(CoedFuture<CoedMessage> future) {
	    			this.future = future;
	    		}
	
				@Override
				public void disconnected() {
	
				}
	
				@Override
				public void received(CoedMessage msg) {
					future.set(msg);
				}
	
				@Override
				public void connected() {
					future.throwEx(new NotConnectedException());
				}
	    	}
	    	
	    	synchronized(this) {
	    		seqListeners.put(new Long(sequenceID), new ReplyListener(future));
	    	}
    	}
    	
    	return future;
    }
    
    public synchronized IFuture<Void> send(CoedMessage msg) {
    	return send(msg, curSequenceID++);
    }
    
    public synchronized void addListener(ICoedConnectionListener listener) {
    	allListeners.add(listener);
    	listener.connected();
    }
    
    public synchronized void removeListener(ICoedConnectionListener listener) {
    	allListeners.remove(listener);
    }
    
    public synchronized IFuture<CoedMessage> sendSeq(CoedMessage msg) {
    	return sendSeq(msg, curSequenceID++);
    }
    
    public IFuture<Void> reply(CoedMessage to, CoedMessage with) {
    	return send(with, to.getSequenceID());
    }
    
    public IFuture<CoedMessage> replySeq(CoedMessage to, CoedMessage with) {
    	return sendSeq(with, to.getSequenceID());
    }
    
	public boolean isConnected() {
		return io != null && io.isConnected();
	}
}
