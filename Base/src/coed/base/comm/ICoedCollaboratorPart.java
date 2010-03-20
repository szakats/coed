/**
 * 
 */
package coed.base.comm;

import coed.base.data.CoedFile;
import coed.base.data.ICollabFilePart;
import coed.base.util.IFuture;
import coed.base.util.IFuture2;

/**
 * @author szakats
 *
 */
public interface ICoedCollaboratorPart extends ICoedCollaborator {
	  /**
	   * Start a new collaboration session for the file at a given path. 
	   * Note that there can be more sessions for the same file.
	   * The method takes the local version of the file and returns a new
	   * CollabFilePart for it.
	   * @param path the path (and name) of the file to share
	   * @param contents the contents of the local version of the file
	   * @param parent the parent of the new part
	   * @return a future CollabFilePart for the session
	   * @throws future NotConnectedException
	   */
	public IFuture<ICollabFilePart> createCollabSession(String path, String contents, CoedFile parent);
	
	/**
	 * Join an existing session.
	 * @param path the path (and name) of the session
	 * @param id the id of the session
	 * @param file
	 * @return a future CollabFilePart and the string containing the remote version
	 * 			of the file
	 * @throws NoSuchSessionException if the session does not exist
	 */
	public IFuture2<ICollabFilePart, String> joinCollabSession(String path, Integer id, CoedFile file);
}
