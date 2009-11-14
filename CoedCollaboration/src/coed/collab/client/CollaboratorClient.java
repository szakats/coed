package coed.collab.client;

import java.net.InetSocketAddress;

import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;

import coed.base.common.ICoedCollaborator;
import coed.base.common.ICollabStateObserver;
import coed.base.data.CoedFile;
import coed.base.data.CoedFileLine;
import coed.base.data.CoedFileLock;
import coed.base.data.IFileObserver;
import coed.base.data.exceptions.NotConnectedToServerException;
import coed.collab.client.config.ICoedConfig;
import coed.collab.protocol.SendChangesMsg;

public class CollaboratorClient implements ICoedCollaborator {
	private String host;
	private int port;
	
	public CollaboratorClient(ICoedConfig conf) {
	
		//host = conf.getString("server.host");
		//port = conf.getInt("server.port");
		
		SocketConnector connector = new SocketConnector();
		SocketConnectorConfig config = new SocketConnectorConfig();
		ClientProtocolHandler handler = new ClientProtocolHandler();
		
		DefaultIoFilterChainBuilder chain = config.getFilterChain();
        chain.addLast("codec", new ProtocolCodecFilter(
                new ObjectSerializationCodecFactory()));
		
		host = "localhost";
		port = 1234;
	
		ConnectFuture future = connector.connect(new InetSocketAddress(host, port), handler, config);
		future.join();
		if (!future.isConnected()) {
			System.out.println("failed to connect");
			return;
		}
		IoSession session = future.getSession();
		session.write(new SendChangesMsg(null, null));
	}
	
	public void ensureConnected() throws NotConnectedToServerException {
		if(getState() != STATUS_CONNECTED)
			throw new NotConnectedToServerException();
	}

	@Override
	public String[] getActiveUsers(CoedFile file) throws NotConnectedToServerException {
		ensureConnected();

		return null;
	}

	@Override
	public CoedFileLine[] getChanges(CoedFile file) throws NotConnectedToServerException {
		ensureConnected();
		
		return null;
	}

	@Override
	public String getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean releaseLock(CoedFileLock lock) throws NotConnectedToServerException {
		ensureConnected();
		
		return false;
	}

	@Override
	public boolean requestLock(CoedFileLock lock) throws NotConnectedToServerException {
		ensureConnected();
		
		return false;
	}

	@Override
	public boolean sendChanges(CoedFile file, CoedFileLine line) throws NotConnectedToServerException {
		ensureConnected();
		new SendChangesMsg(file, line); // TODO: send it
		return false;
	}

	@Override
	public void addChangeListener(IFileObserver fileObserver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addFileChangeListener(CoedFile file,
			IFileObserver fileObserver) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeChangeListener(IFileObserver fileObserver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addStateListener(ICollabStateObserver stateObserver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeStateListener(ICollabStateObserver stateObserver) {
		// TODO Auto-generated method stub
		
	}

}
