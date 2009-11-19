package coed.collab.server;

import coed.collab.connection.CoedConnection;
import coed.collab.connection.CoedConnectionAcceptor;

public class CollaboratorServer implements CoedConnectionAcceptor.Listener {

	private CoedConnectionAcceptor acceptor = new CoedConnectionAcceptor();
    
    private int port = 1234;
	
	public CollaboratorServer() {
		acceptor.listen(port);
	}

	@Override
	public void connected(CoedConnection conn) {
		// TODO: create new session
	}
}
