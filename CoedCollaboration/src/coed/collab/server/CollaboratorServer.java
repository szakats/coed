package coed.collab.server;

import java.io.IOException;

import coed.collab.connection.CoedConnection;
import coed.collab.connection.CoedConnectionAcceptor;

public class CollaboratorServer implements CoedConnectionAcceptor.Listener {

	private CoedConnectionAcceptor acceptor = new CoedConnectionAcceptor();
    private int port = 1234;
    private FileManager fm;
    private UserManager um;
    private String configPath;
	
	public CollaboratorServer(String configPath) {
		acceptor.addListener(this);
		acceptor.listen(port);
		fm = new FileManager();
		um = new UserManager(configPath);
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
	
	public boolean validateUser(String user, String password){
		return um.validateUser(user, password);
	}
}
