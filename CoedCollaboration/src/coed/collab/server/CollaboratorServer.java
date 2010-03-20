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
	
	public Integer createSession(String path, String contents) {
		/* String contents = ((SendContentsMsg)result).getContents();
		//System.out.println("got contents " + contents);
		try{
			server.addNewFile(fileName,((SendContentsMsg)result).getContents());
			ServerFile sf = server.getServerFile(fileName);
			onlineFiles.add(sf);
			sf.addSession(Session.this);
		}
		catch(IOException ex) {}*/
		return 0;
	}
	
	public String joinSession(Integer id) {
		/*ServerFile sf = server.getServerFile(fileName);
		onlineFiles.add(sf);
		sf.addSession(Session.this);
		conn.reply(msg, new CreateSessionResultMsg(true)).addErrorListener(this);*/
		return "";
	}
	
	public boolean existsSession(Integer id) {
		return true;
	}
}
