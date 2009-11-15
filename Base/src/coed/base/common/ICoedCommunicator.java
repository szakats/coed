package coed.base.common;

/**
 * TODO: javadoc
 * @author Izso
 *	
 */
public interface ICoedCommunicator extends ICoedVersioner, ICoedCollaborator{
	
	public String getVersionerType();
	
	public String[] getProjectList();
	
	public ICoedObject getObject(String path);
	public ICoedObject addObject(String path);
}