package coed.collab.client;

import java.net.InetSocketAddress;

import org.apache.mina.common.ConnectFuture;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;

import coed.base.common.ICoedCollaborator;
import coed.base.data.CoedFile;
import coed.base.data.CoedFileLine;
import coed.base.data.CoedFileLock;
import coed.base.data.IFileObserver;
import coed.collab.client.config.ICoedConfig;
import coed.collab.protocol.SendChangesMsg;

public class CollaboratorClient implements ICoedCollaborator {
	private String host;
	private int port;
	
	public CollaboratorClient(ICoedConfig conf) {
		
		SocketConnector connector = new SocketConnector();
	
		host = conf.getString("server.host");
		port = conf.getInt("server.port");
		SocketConnectorConfig config = new SocketConnectorConfig();
		
		host = "localhost";
		port = 1234;
		
		ClientProtocolHandler handler = new ClientProtocolHandler();
	
		ConnectFuture future1 = connector.connect(new InetSocketAddress(host, port), handler, config);
        future1.join();
        if (!future1.isConnected()) {
            //return false;
        }
        //session = future1.getSession();
       // session.write("LOGIN " + name);
	}

	@Override
	public String[] getActiveUsers(CoedFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CoedFileLine[] getChanges(CoedFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean releaseLock(CoedFileLock lock) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean requestLock(CoedFileLock lock) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendChanges(CoedFile file, CoedFileLine line) {
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

}
