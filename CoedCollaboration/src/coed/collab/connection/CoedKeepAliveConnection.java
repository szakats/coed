package coed.collab.connection;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import coed.base.util.IFuture;
import coed.collab.protocol.*;

public class CoedKeepAliveConnection implements ICoedConnection {
	private String host;
	private int port;
	private boolean shuttingDown;
	private CoedConnection conn;
	private ConnectionWorker connWorker;
	
	LinkedList<ICoedConnectionListener> listeners = new LinkedList<ICoedConnectionListener>();

	public synchronized boolean isConnected() {
		return conn != null && conn.isConnected();
	}
	
	private synchronized void setConnection(CoedConnection conn) {
		this.conn = conn;
		if(conn == null)
			connWorker.interrupt();
		else
			conn.addListener(new DisconnectListener());
	}
	
	class DisconnectListener implements ICoedConnectionListener {
		@Override
		public void connected() {
		}

		@Override
		public void disconnected() {
			setConnection(null);
		}

		@Override
		public void received(CoedMessage msg) {
		}
	}
	
	class ConnectionWorker extends Thread {
		public synchronized void run() {
	        while(!shuttingDown) {
	        	if(isConnected()) {
					try {
						// wait until shutting down or disconnected
						wait();
					} catch (InterruptedException e) {
						continue;
					}
	        	}
	        	
	        	IFuture<CoedConnection> future = CoedConnection.connect(host, port);
	        	CoedConnection conn = null;
				try {
					conn = future.get();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
	        	if (conn == null) {
	        		System.out.println("failed to connect");
	        		try {
	        			// wait 1 second before trying a new connection, or until shutdown
						wait(1000);
					} catch (InterruptedException e) {
						continue;
					}
	        	} else {
	        		System.out.println("connected");
	        		setConnection(conn);
	        	}
	        }
	        System.out.println("conn thread shutting down");
		}
	}
	
	public CoedKeepAliveConnection(String host, int port) {
		this.host = host;
		this.port = port;
		shuttingDown = false;
		connWorker = new ConnectionWorker();
		connWorker.start();
	}
    
    public void shutdown() {
    	shuttingDown = true;
    	connWorker.interrupt();
    }

	@Override
	public void addListener(ICoedConnectionListener listener) {
		listeners.add(listener);
		if(conn != null)
			conn.addListener(listener);
	}

	@Override
	public void removeListener(ICoedConnectionListener listener) {
		listeners.remove(listener);
		if(conn != null)
			conn.removeListener(listener);
	}

	@Override
	public void reply(CoedMessage to, CoedMessage with) {
		assert conn != null;
		conn.reply(to, with);
	}

	@Override
	public IFuture<CoedMessage> replyF(CoedMessage to, CoedMessage with) {
		assert conn != null;
		return conn.replyF(to, with);
	}

	@Override
	public void send(CoedMessage msg) {
		assert conn != null;
		conn.send(msg);
	}

	@Override
	public IFuture<CoedMessage> sendF(CoedMessage msg) {
		assert conn != null;
		return conn.sendF(msg);
	}
}
