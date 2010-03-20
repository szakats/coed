package coed.collab.connection;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import coed.base.data.exceptions.NotConnectedException;
import coed.base.util.CoedFuture;
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
		else {
			for(ICoedConnectionListener listener : listeners)
				conn.addListener(listener);
			conn.addListener(new DisconnectListener());
		}
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
	public IFuture<Void> reply(CoedMessage to, CoedMessage with) {
		if(conn == null)
			return new CoedFuture<Void>(new NotConnectedException());
		else
			return conn.reply(to, with);
	}

	@Override
	public IFuture<CoedMessage> replySeq(CoedMessage to, CoedMessage with) {
		if(conn == null)
			return new CoedFuture<CoedMessage>(new NotConnectedException());
		else
			return conn.replySeq(to, with);
	}

	@Override
	public IFuture<Void> send(CoedMessage msg) {
		if(conn == null)
			return new CoedFuture<Void>(new NotConnectedException());
		else
			return conn.send(msg);
	}

	@Override
	public IFuture<CoedMessage> sendSeq(CoedMessage msg) {
		if(conn == null)
			return new CoedFuture<CoedMessage>(new NotConnectedException());
		else
			return conn.sendSeq(msg);
	}
	
	public void disconnect() {
		conn.disconnect();
	}

	@Override
	public IFuture<Void> reply(CoedMessage to, Throwable exception) {
		return conn.reply(to, exception);
	}
}
