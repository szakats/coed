package coed.collab.connection;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
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

    }

	@Override
    public void messageReceived(IoSession session, Object message) {
    	assert(session != null && message != null);
    	CoedMessage msg = (CoedMessage)message;
    	for(ICoedConnectionListener l : allListeners)
    		l.received(msg);
    	long id = msg.getSequenceID();
    	
    	ICoedConnectionListener l;
    	synchronized(this) {
	    	l = seqListeners.remove(new Long(id));
    	}
    	
	    if(l != null)
	    	l.received(msg);
    }
    
	@Override
    public void sessionClosed(IoSession session) {
    	System.out.println("session closed.");
		for(ICoedConnectionListener listener : allListeners)
			listener.disconnected();
    }
	
    private synchronized void send(CoedMessage msg, long sequenceID) throws NotConnectedException {
    	if(!isConnected()) throw new NotConnectedException();
    	msg.setSequenceID(sequenceID);
    	io.write(msg);
    }
    
    private synchronized IFuture<CoedMessage> sendF(CoedMessage msg, long sequenceID) throws NotConnectedException {
    	send(msg, sequenceID);
    	
    	CoedFuture<CoedMessage> future = new CoedFuture<CoedMessage>();
    	
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
				// TODO Auto-generated method stub
				
			}
    	}
    	
    	synchronized(this) {
    		seqListeners.put(new Long(sequenceID), new ReplyListener(future));
    	}
    	
    	return future;
    }
    
    public synchronized void send(CoedMessage msg) throws NotConnectedException {
    	send(msg, curSequenceID);
    	curSequenceID++;
    }
    
    public synchronized void addListener(ICoedConnectionListener listener) {
    	allListeners.add(listener);
    	listener.connected();
    }
    
    public synchronized void removeListener(ICoedConnectionListener listener) {
    	allListeners.remove(listener);
    }
    
    public synchronized IFuture<CoedMessage> sendF(CoedMessage msg) throws NotConnectedException {
    	IFuture<CoedMessage> ret = sendF(msg, curSequenceID);
    	curSequenceID++;
    	return ret;
    }
    
    public void reply(CoedMessage to, CoedMessage with) throws NotConnectedException {
    	send(with, to.getSequenceID());
    }
    
    public IFuture<CoedMessage> replyF(CoedMessage to, CoedMessage with) throws NotConnectedException {
    	return sendF(with, to.getSequenceID());
    }
    
	public boolean isConnected() {
		return io != null && io.isConnected();
	}
}
