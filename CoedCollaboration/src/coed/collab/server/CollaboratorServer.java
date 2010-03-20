package coed.collab.server;

import java.io.IOException;

import coed.collab.connection.CoedConnection;
import coed.collab.connection.CoedConnectionAcceptor;
import coed.collab.protocol.CreateSessionResultMsg;
import coed.collab.protocol.SendContentsMsg;

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
	
	/*public void addNewFile(String path, String contents) throws IOException{
		
	}*/
	
	public ServerFile getServerFile(Integer id){
		return fm.getFile(id);
	}
	
	public void removeServerFile(Integer id) {
		fm.removeFile(id);
	}
	
	public boolean validateUser(String user, String password){
		return um.validateUser(user, password);
	}
	
	public Integer createSession(String path, String contents) {
		ServerFile sf = fm.addFile(path,contents);
		return sf.getId();
	}
	
	public String joinSession(Integer id, Session session) {
		ServerFile sf = fm.getFile(id);
		sf.addSession(session);
		return sf.getCurrentContents();
	}
	
	public boolean existsSession(Integer id) {
		return true;
	}
	
	public FileManager getFileManager() {
		return this.fm;
	}
}
