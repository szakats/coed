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
import coed.collab.client.config.ICoedConfig;

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
	public String getVersionerType() {
		return v.getType();
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setBasePath(String path){
		this.basePath = path;
	}

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
}
