package coed.base.comm;

import java.util.Map;

import coed.base.config.Config;
import coed.base.config.ICoedConfig;
import coed.base.data.ICoedFile;
import coed.base.util.IFuture;
import coed.base.util.IFuture2;
/**
 * ICoedCommunicator is the interface presented to the plugin,
 * it has a versioning and collaborative component.
 */
public interface ICoedCommunicator extends ICoedVersioner, ICoedCollaborator {
	
	  /**
	   * Turn on the collaborative editing mode for this file,
	   * only works if the object is a file.
	   * The method takes the local version of the file and
	   * returns a future containing its remote version. 
	   * @param contents the contents of the local version
	   * 		of the file
	   * @return a future string containing the remote version
	   * 		of the file or if the file has not been registered
	   * 		as online yet or the content happens to be the same
	   * 		then a future null
	   * @throws future NotConnectedException
	   */
	public IFuture<ICoedFile> createSession(String path, String contents);
	
	public IFuture2<ICoedFile, String> joinSession(String path, Integer id);
	
	public String getUserName();
	
	public ICoedConfig getConfig();
}