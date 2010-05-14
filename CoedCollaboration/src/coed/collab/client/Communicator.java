package coed.collab.client;

import java.io.File;
import java.util.Map;

import coed.base.comm.IAllSessionsListener;
import coed.base.comm.ICoedCollaborator;
import coed.base.comm.ICoedCollaboratorPart;
import coed.base.comm.ICoedCommunicator;
import coed.base.comm.ICoedVersioner;
import coed.base.comm.ICoedVersionerPart;
import coed.base.comm.ICollabStateListener;
import coed.base.comm.IUserChangeListener;
import coed.base.config.ICoedConfig;
import coed.base.data.CoedFile;
import coed.base.data.ICoedFile;
import coed.base.data.ICollabFilePart;
import coed.base.data.IVersionedFilePart;
import coed.base.util.CoedFuture;
import coed.base.util.CoedFuture2;
import coed.base.util.IFuture;
import coed.base.util.IFuture2;
import coed.base.util.IFuture2Listener;
import coed.base.util.IFutureListener;
import coed.collab.protocol.CoedMessage;

/**
 * This represents an object that will deal with all kinds of communication between 
 * plugin and client. Contains ICoedVersioner and ICoedCollaborator instances, and
 * the collaborative and versioning actions are delegated. All the settings needed 
 * to configure the instances are provided by a Config object. 
 * 
 * @author Neobi
 *
 */
public class Communicator implements ICoedCommunicator {

	private ICoedVersionerPart v;  // versioner
	private ICoedCollaboratorPart c; // collaborator
	private ICoedConfig conf; //configurator, containing information regarding user account

	/**
	 * the path relative to which the other paths are given
	 * eg. the project path
	 */
	private String basePath;
	
	public Communicator(ICoedVersionerPart versioner, ICoedCollaboratorPart collaborator, ICoedConfig config, String basePath)
	{
		this.v = versioner;
		this.c = collaborator;
		this.conf = config;
		this.basePath = basePath;
	}
	
	@Override
	public String getState() {
		return c.getState();
	}
	
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Set the base path of the communicator(i.e. the path to which we
	 * give the relative paths). This usually should be the path of
	 * our workspace
	 * @param path
	 */
	public void setBasePath(String path){
		this.basePath = path;
	}
	
	/**
	 * Adding a state listener to this communicator. The listener will signal
	 * every change occured in the state of the communicator.
	 */
	@Override
	public void addStateListener(ICollabStateListener stateObserver) {
		c.addStateListener(stateObserver);
	}

	@Override
	public void removeStateListener(ICollabStateListener stateObserver) {
		c.removeStateListener(stateObserver);
	}

	@Override
	public IFuture<ICollabFilePart[]> getAllOnlineFiles() {
		return c.getAllOnlineFiles();
	}

	@Override
	public void endCollab() {
		c.endCollab();
	}

	@Override
	public void startCollab() {
		c.startCollab();
	}
	
	class CreateSessionListener implements IFutureListener<ICollabFilePart> {
		public CoedFuture<ICoedFile> future;
		public CoedFile file;
		
		CreateSessionListener(String path) {
			 file = new CoedFile(path);
			 future = new CoedFuture<ICoedFile>();
		}

		@Override
		public void got(ICollabFilePart result) {
			file.init(v.makeVersionedFile(file), result); 
			future.set(file);
		}

		@Override
		public void caught(Throwable e) {
			future.throwEx(e);
		}
	}
	
	class JoinSessionListener implements IFuture2Listener<ICollabFilePart, String> {
		public CoedFuture2<ICoedFile, String> future;
		public CoedFile file;
		
		JoinSessionListener(String path) {
			 file = new CoedFile(path);
			 future = new CoedFuture2<ICoedFile, String>();
		}

		@Override
		public void got(ICollabFilePart part, String contents) {
			file.init(v.makeVersionedFile(file), part); 
			future.set(file, contents);
		}

		@Override
		public void caught(Throwable e) {
			future.throwEx(e);
		}
	}

	@Override
	public IFuture<ICoedFile> createSession(String path, String contents) {
		System.out.println("creating session for " + path);
		CreateSessionListener fl = new CreateSessionListener(path);
		c.createCollabSession(path, contents, fl.file).addListener(fl);
		return fl.future;
	}

	@Override
	public IFuture2<ICoedFile, String> joinSession(String path, Integer id) {
		System.out.println("joining session " + path + "(" + id + ")");
		JoinSessionListener fl = new JoinSessionListener(path);
		c.joinCollabSession(path, id, fl.file).addListener(fl);
		return fl.future;
	}

	@Override
	public IFuture<Map<Integer, String>> getCollabSessions() {
		return c.getCollabSessions();
	}

	@Override
	public String getUserName() {
		return conf.getString("user.name");
	}

	@Override
	public void addAllSessionsListener(IAllSessionsListener listener) {
		c.addAllSessionsListener(listener);
		
	}

	@Override
	public void removeAllSessionsListener(IAllSessionsListener listener) {
		c.removeAllSessionsListener(listener);
		
	}
}
