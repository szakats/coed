package coed.collab.client;

import java.io.File;

import coed.base.comm.ICoedCollaborator;
import coed.base.comm.ICoedCommunicator;
import coed.base.comm.ICoedVersioner;
import coed.base.comm.ICollabStateObserver;
import coed.base.data.CoedObject;
import coed.base.data.ICoedObject;
import coed.base.data.ICollabObject;
import coed.base.data.IVersionedObject;
import coed.base.util.IFuture;
import coed.collab.client.config.ICoedConfig;

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

	private ICoedVersioner v;  // versioner
	private ICoedCollaborator c; // collaborator
	private ICoedConfig conf; //configurator, containing information regarding user account
	
	/**
	 * the path relative to which the other paths are given
	 * eg. the project path
	 */
	private String basePath; 
	
	public Communicator(ICoedVersioner versioner, ICoedCollaborator collaborator, ICoedConfig config)
	{
		this.v = versioner;
		this.c = collaborator;
		this.conf = config;
	}
	
	@Override
	public String[] getProjectList() {
		return null;
	}

	@Override
	public String getState() {
		return null;
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
	public void addStateListener(ICollabStateObserver stateObserver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeStateListener(ICollabStateObserver stateObserver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ICoedObject addObject(String path) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Returns an ICoedObject given by the relative path
	 */
	@Override
	public ICoedObject getObject(String path) {
		// TODO Auto-generated method stub
		File f = new File(basePath+path);
		CoedObject ret = new CoedObject(path, f.isFile());
		ret.init(makeVersionedObject(ret), makeCollabObject(ret));
		return ret;
	}

	@Override
	public IVersionedObject makeVersionedObject(ICoedObject obj) {
		return v.makeVersionedObject(obj);
	}

	@Override
	public ICollabObject makeCollabObject(ICoedObject obj) {
		return c.makeCollabObject(obj);
	}

	@Override
	public IFuture<ICollabObject[]> getAllOnlineFiles() {
		return c.getAllOnlineFiles();
	}
}
