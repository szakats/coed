package coed.collab.client;

import coed.base.data.CoedFile;
import coed.collab.client.config.Config;
import coed.collab.data.CoedFileLine;
import coed.collab.data.CoedFileLock;
import coed.collab.data.IFileObserver;

public class CollaboratorClient implements ICoedCollaborator {
	private String host;
	private int port;
	
	public CollaboratorClient(Config conf) {
	
		host = conf.getString("server.host");
		port = conf.getInt("server.port");
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
	public boolean listenToChanges(CoedFile file, IFileObserver fileObserver) {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return false;
	}

}
