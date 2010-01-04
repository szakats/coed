package coed.collab.server;

import java.io.IOException;

import coed.collab.connection.CoedConnection;
import coed.collab.connection.CoedConnectionAcceptor;

public class CollaboratorServer implements CoedConnectionAcceptor.Listener {

	private CoedConnectionAcceptor acceptor = new CoedConnectionAcceptor();
    private int port = 1234;
    private FileManager fm;
	
	public CollaboratorServer() {
		acceptor.addListener(this);
		acceptor.listen(port);
		fm = new FileManager();
	}

	@Override
	public void connected(CoedConnection conn) {
		System.out.println("client connected");
		Session session = new Session(conn, this);
		conn.addListener(session);
		// TODO: store sessions somehow ?
	}
	
	public boolean isFileOnline(String path){
		return fm.containsFile(path);
	}
	
	public void addNewFile(String path, String contents) throws IOException{
		ServerFile sf = new ServerFile(path);
		sf.changeContents(contents);
		fm.addFile(sf);
	}
	
	public ServerFile getServerFile(String path){
		return fm.getFile(path);
	}
	
	public void removeServerFile(String path) {
		fm.removeFile(path);
	}
}
