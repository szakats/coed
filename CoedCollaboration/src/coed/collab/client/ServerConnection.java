package coed.collab.client;

import java.net.InetSocketAddress;
import java.util.LinkedList;

import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;

import coed.collab.protocol.*;

public class ServerConnection extends IoHandlerAdapter {
	private String host;
	private int port;
	private IoSession io;
	
	private boolean connected;
	ConnectionWorker connWorker;
	private boolean shuttingDown;
	
	public interface Listener {
		void received(CoedMessage msg);
		void connected();
		void disconnected();
	}
	
	LinkedList<Listener> listeners = new LinkedList<Listener>();
	
	public synchronized boolean isConnected() {
		return connected;
	}
	
	private synchronized void setConnected(boolean connected) {
		this.connected = connected;
		
		for(Listener listener : listeners)
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
		connWorker = new ConnectionWorker();
		connWorker.start();
	}
	
    public void exceptionCaught(IoSession session, Throwable cause) {

    }

    public void messageReceived(IoSession session, Object message) {
    	CoedMessage msg = (CoedMessage)message;
    	for(Listener listener : listeners)
    		listener.received(msg);
    }
    
    public void sessionClosed(IoSession session) {
    	System.out.println("session closed.");
    	setConnected(false);
    }
    
    public void send(CoedMessage msg) {
    	assert(io != null);
    	io.write(msg);
    }
    
    public void shutdown() {
    	shuttingDown = true;
    	synchronized(connWorker) {
    		connWorker.notify();
    	}
    }
    
    public void addListener(Listener listener) {
    	listeners.add(listener);
    }
    
    public void removeListener(Listener listener) {
    	listeners.remove(listener);
    }
    
    public CoedMessage sendAndwait(CoedMessage msg, String waitForMsgName, long timeoutMS) throws ClassNotFoundException {
    	send(msg);
    	
    	Class msgClass = Class.forName(waitForMsgName);
    	Object receiveEvent = new Object();
    	
    	class WaitListener implements Listener {
    		public CoedMessage ret;
    		public Class msgClass;
    		public Object receiveEvent;

			@Override
			public void connected() {
				
			}

			@Override
			public void disconnected() {

			}

			@Override
			public void received(CoedMessage msg) {
    			if(msgClass.isInstance(msg)) {
    				ret = msg;
    				synchronized(receiveEvent) {
    					receiveEvent.notify();
    				}
    			}
			}
    	}
    	
    	WaitListener listener = new WaitListener();
    	listener.msgClass = msgClass;
    	listener.receiveEvent = receiveEvent;
    	
    	addListener(listener);
    	synchronized(receiveEvent) {
    		try {
				receiveEvent.wait(timeoutMS);
			} catch (InterruptedException e) {
			}
    	}
    	removeListener(listener);
    	
    	return listener.ret;
    }
}
