package coed.base.common;

import coed.base.data.CoedFile;
import coed.base.data.CoedProject;
import coed.base.data.IFileObserver;

/**
 * TODO: javadoc
 * @author Izso
 *	
 */
public interface ICoedCommunicator extends ICoedVersioner, ICoedCollaborator{
	
	public String getVersionerType();
	
	public CoedProject createProject(String name);
	public String[] getProjectList();
}