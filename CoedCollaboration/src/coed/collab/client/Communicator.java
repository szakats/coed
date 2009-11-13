package coed.collab.client;

import coed.base.common.ICoedCollaborator;
import coed.base.common.ICoedCommunicator;
import coed.base.common.ICoedVersioner;
import coed.base.data.CoedFile;
import coed.base.data.CoedFileLine;
import coed.base.data.CoedFileLock;
import coed.base.data.CoedProject;
import coed.base.data.IFileObserver;
import coed.collab.client.config.ICoedConfig;

public class Communicator implements ICoedCommunicator {

	private ICoedVersioner v;  // versioner
	private ICoedCollaborator c; // collaborator
	//private ICoedConfig conf; //configurator, containing information regarding user account 
	
	public Communicator(ICoedVersioner versioner, ICoedCollaborator collaborator, ICoedConfig config)
	{
		this.v = versioner;
		this.c = collaborator;
		//this.conf = config;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean commitFile(CoedFile file) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean commitFiles(CoedFile[] files) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean commitProject(CoedProject project) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CoedFile getFileInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CoedProject getProjectInfo(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProjectList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getState() {
		// TODO Auto-generated method stub
		return null;
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
	public CoedFile getFileInfo(CoedFile file) {
		// TODO Auto-generated method stub
		return null;
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
	
}
