package coed.collab.client;

import coed.collab.data.CoedFile;
import coed.collab.data.CoedFileLine;
import coed.collab.data.CoedFileLock;
import coed.collab.data.CoedProject;
import coed.collab.data.IFileObserver;

public class Collaborator implements ICoedCommunicator{

	@Override
	public boolean checkoutFile(CoedFile file) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkoutFiles(CoedFile[] files) {
		// TODO Auto-generated method stub
		return false;
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
