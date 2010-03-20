/**
 * 
 */
package coed.base.comm;

import coed.base.data.CoedFile;
import coed.base.data.ICoedFile;
import coed.base.data.ICollabFilePart;
import coed.base.util.IFuture;
import coed.base.util.IFuture2;

/**
 * @author szakats
 *
 */
public interface ICoedCollaboratorPart extends ICoedCollaborator {
	  /**
	   * Turn on the collaborative editing mode for this file,
	   * only works if the object is a file.
	   * The method takes the local version of the file and
	   * returns a future containing its remote version. 
	 * @param contents the contents of the local version
	   * 		of the file
	 * @param file TODO
	   * @return a future string containing the remote version
	   * 		of the file or if the file has not been registered
	   * 		as online yet or the content happens to be the same
	   * 		then a future null
	   * @throws future NotConnectedException
	   */
	public IFuture<ICollabFilePart> createCollabSession(String path, String contents, CoedFile file);
	
	
	public IFuture2<ICollabFilePart, String> joinCollabSession(String path, Integer id, CoedFile file);
}
