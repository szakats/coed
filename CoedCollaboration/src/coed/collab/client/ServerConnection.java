package coed.collab.client;

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

import coed.base.util.CoedFuture;
import coed.base.util.IFuture;
import coed.collab.protocol.*;

public class ServerConnection extends IoHandlerAdapter {
	private String host;
	private int port;
	private IoSession io;
	
	private long curSequenceID;
	
	private boolean connected;
	ConnectionWorker connWorker;
	private boolean shuttingDown;
	
	public interface Listener {
		void received(CoedMessage msg);
		void connected();
		void disconnected();
	}
	
	/**
	 * allListeners contains listeners that listen for all incoming
	 * messages which seqListeners contains those which wait for
	 * the continuation of a particular sequence.
	 */
	LinkedList<Listener> allListeners = new LinkedList<Listener>();
	HashMap<Long, Listener> seqListeners = new HashMap<Long, Listener>();
	
	public synchronized boolean isConnected() {
		return connected;
	}
	
	private synchronized void setConnected(boolean connected) {
		this.connected = connected;
		
		for(Listener listener : allListeners)
			if(connected)
				listener.connected();
			else
				listener.disconnected();
				
		synchronized(connWorker) {
			connWorker.notify();
		}
	}
	
	class ConnectionWorker extends Thread {
		public synchronized void run() {
			SocketConnector connector = new SocketConnector();
			SocketConnectorConfig config = new SocketConnectorConfig();
			
			DefaultIoFilterChainBuilder chain = config.getFilterChain();
	        chain.addLast("codec", new ProtocolCodecFilter(
	                new ObjectSerializationCodecFactory()));
		
	        
	        while(!shuttingDown) {
	        	if(isConnected()) {
					try {
						// wait until shutting down or disconnected
						wait();
					} catch (InterruptedException e) {
						continue;
					}
	        	}

	        	ConnectFuture future = connector.connect
	        		(new InetSocketAddress(host, port), ServerConnection.this, config);
	        	future.join();
	        	if (!future.isConnected()) {
	        		System.out.println("failed to connect");
	        		try {
	        			// wait 1 second before trying a new connection, or until shutdown
						wait(1000);
					} catch (InterruptedException e) {
						continue;
					}
	        	} else {
	        		System.out.println("connected");
	        		io = future.getSession();
	        		setConnected(true);
	        	}
	        }
	        System.out.println("conn thread shutting down");
		}
	}
	
	public ServerConnection(String host, int port) {
		this.host = host;
		this.port = port;
		connected = false;
		shuttingDown = false;
		curSequenceID = 0;
		connWorker = new ConnectionWorker();
		connWorker.start();
	}
	
    public void exceptionCaught(IoSession session, Throwable cause) {

    }

    public void messageReceived(IoSession session, Object message) {
    	assert(session != null && message != null);
    	CoedMessage msg = (CoedMessage)message;
    	for(Listener l : allListeners)
    		l.received(msg);
    	long id = msg.getSequenceID();
    	
    	Listener l;
    	synchronized(this) {
	    	l = seqListeners.remove(new Long(id));
    	}
    	
	    if(l != null)
	    	l.received(msg);
    }
    
    public void sessionClosed(IoSession session) {
    	System.out.println("session closed.");
    	setConnected(false);
    }
    
    private synchronized void send(CoedMessage msg, long sequenceID) {
    	assert(io != null);
    	msg.setSequenceID(sequenceID);
    	io.write(msg);
    }
    
    public synchronized void send(CoedMessage msg) {
    	send(msg, curSequenceID);
    	curSequenceID++;
    }
    
    public void shutdown() {
    	shuttingDown = true;
    	synchronized(connWorker) {
    		connWorker.notify();
    	}
    }
    
    public void addListener(Listener listener) {
    	allListeners.add(listener);
    }
    
    public void removeListener(Listener listener) {
    	allListeners.remove(listener);
    }
    
    public IFuture<CoedMessage> sendF(CoedMessage msg) {
    	send(msg);
    	long id = msg.getSequenceID();
    	
    	CoedFuture<CoedMessage> future = new CoedFuture<CoedMessage>();
    	
    	class WaitListener implements Listener {
    		CoedFuture<CoedMessage> future;
    		
    		public WaitListener(CoedFuture<CoedMessage> future) {
    			this.future = future;
    		}

			@Override
			public void connected() {
				
			}

			@Override
			public void disconnected() {

			}

			@Override
			public void received(CoedMessage msg) {
				future.set(msg);
			}
    	}
    	
    	synchronized(this) {
    		seqListeners.put(new Long(id), new WaitListener(future));
    	}

    	
    	return future;
    }
}
