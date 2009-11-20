package coed.base.comm;

import coed.base.data.ICoedObject;

/**
 * ICoedCommunicator is the interface presented to the plugin,
 * it has a versioning and collaborative component.
 */
public interface ICoedCommunicator extends ICoedVersioner, ICoedCollaborator{
	
	public String[] getProjectList();
	
	public ICoedObject getObject(String path);
	public ICoedObject addObject(String path);
}