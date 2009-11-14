package coed.collab.client;

import coed.base.common.ICoedCollaborator;
import coed.base.common.ICoedCommunicator;
import coed.base.common.ICoedVersioner;
import coed.base.common.ICollabStateObserver;
import coed.base.data.CoedFile;
import coed.base.data.CoedFileLine;
import coed.base.data.CoedFileLock;
import coed.base.data.CoedProject;
import coed.base.data.IFileObserver;
import coed.base.data.exceptions.NotConnectedToServerException;
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
	public boolean checkoutFile(CoedFile file) {
		return v.checkoutFile(file);
	}

	@Override
	public boolean checkoutFiles(CoedFile[] files) {
		return v.checkoutFiles(files);
	}

	@Override
	public boolean checkoutProject(CoedProject project) {
		return v.checkoutProject(project);
	}

	@Override
	public boolean commitFile(CoedFile file) {
		return v.commitFile(file);
	}

	@Override
	public boolean commitFiles(CoedFile[] files) {
		return v.commitFiles(files);
	}

	@Override
	public boolean commitProject(CoedProject project) {
		return v.commitProject(project);
	}

	@Override
	public CoedProject getProjectInfo(String name) {
		return v.getProjectInfo(name);
	}

	@Override
	public String getProjectList() {
		return v.getProjectList();
	}

	@Override
	public String getState() {
		return null;
	}

	@Override
	public String[] getActiveUsers(CoedFile file) throws NotConnectedToServerException {
		return c.getActiveUsers(file);
	}

	@Override
	public CoedFileLine[] getChanges(CoedFile file) throws NotConnectedToServerException {
		return c.getChanges(file);
	}

	@Override
	public boolean releaseLock(CoedFileLock lock) throws NotConnectedToServerException {
		return c.releaseLock(lock);
	}

	@Override
	public boolean requestLock(CoedFileLock lock) throws NotConnectedToServerException {
		return c.requestLock(lock);
	}

	@Override
	public boolean sendChanges(CoedFile file, CoedFileLine line) throws NotConnectedToServerException {
		return c.sendChanges(file, line);
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

	@Override
	public void addChangeListener(IFileObserver obs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CoedFile getFileInfo(String file) {
		return v.getFileInfo(file);
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
	
}
