package coed.collab.client;

import coed.versioning.client.NullVersioner;
import coed.base.common.ICoedVersioner;
import coed.collab.client.config.Config;

public class CoedCommunicatorFactory {
	
	class UnknownTypeException extends Exception
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		UnknownTypeException() {}
	}
	
	public ICoedCommunicator create(String type, String configPath) throws UnknownTypeException
	{
		Config conf = new Config(configPath);
		if(type.equals(ICoedVersioner.NULL)) {
			return new Communicator(new NullVersioner(), new CollaboratorClient(conf), conf);
		} else if(type.equals(ICoedVersioner.GIT)) {
			//return new Communicator(new GitVersioner(), new Collaborator());
		}
		throw new UnknownTypeException();
	}
}
