package coed.collab.client;

import coed.base.common.ICoedCommunicator;
import coed.base.common.ICoedVersioner;
import coed.base.data.exceptions.InvalidConfigFileException;
import coed.base.data.exceptions.UnknownVersionerTypeException;
import coed.collab.client.config.Config;
import coed.versioning.client.NullVersioner;

public class CoedCommunicatorFactory {
	
		
	public ICoedCommunicator create(String configPath) throws UnknownVersionerTypeException, InvalidConfigFileException
	{
		Config conf = new Config(configPath);
		String type = conf.getString("versioner.type");
		
		if(type.equals(ICoedVersioner.NULL)) {
			return new Communicator(new NullVersioner(), new CollaboratorClient(conf), conf);
		} else if(type.equals(ICoedVersioner.GIT)) {
			//return new Communicator(new GitVersioner(), new Collaborator());
		}
		throw new UnknownVersionerTypeException();
	}
}
