package coed.collab.client;

import coed.base.common.ICoedVersioner;
import coed.collab.data.*;

/**
 * TODO: javadoc
 * @author Izso
 *	
 */
public interface ICoedCommunicator extends ICoedVersioner, ICoedCollaborator{
	
	public String getVersionerType();
   
}