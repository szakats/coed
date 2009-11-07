package coed.collab.client;

import coed.versioning.client.NullVersioner;

public class CoedCommunicatorFactory {
	
	public final static String NULL = "null";
	public final static String GIT = "git";
	
	class UnknownTypeException extends Exception
	{
		UnknownTypeException() {}
	}
	
	public ICoedCommunicator create(String type) throws UnknownTypeException
	{
		if(type.equals(NULL)) {
			return new Communicator(new NullVersioner(), new Collaborator());
		} else if(type.equals(GIT)) {
			//return new Communicator(new GitVersioner(), new Collaborator());
		}
		throw new UnknownTypeException();
	}
}
