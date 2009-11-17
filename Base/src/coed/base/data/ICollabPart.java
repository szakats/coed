package coed.base.data;

import coed.base.data.exceptions.NotConnectedToServerException;
import coed.base.util.IFuture;

public interface ICollabPart {

	/**
	 * Notify the server (and consequently other connected clients)
	 * that a given line has changed.
	 * @param line the line that changed
	 * @return a future containing a boolean value of true
	 * 			if it succeeded or false if for example the line was locked already.
	 * 			TODO: maybe throw an exception with the exact reason of the failure ?
	 * @throws NotConnectedToServerException
	 */
	public IFuture<Boolean> sendChanges(CoedFileLine line)
  		throws NotConnectedToServerException;
	
	/**
	 * Get the full contents of this file from the server.
	 * @return a future string containing the contents
	 */
	public IFuture<String> getCurrentContent();
	
  /**
   * Get the changes that have been done to this file since the last call
   * to either getCurrentContent, getChanges or goOnline.
   * @return a future containing the lines that changed
   * @throws NotConnectedToServerException
   */
	public IFuture<CoedFileLine[]> getChanges()
  		throws NotConnectedToServerException;
  
  /**
   * Get the list of users who are currently editing this file
   * @return a future array of strings containing the names of the users 
   * @throws NotConnectedToServerException
   */
	public IFuture<String[]> getActiveUsers()
  		throws NotConnectedToServerException;
  
  /**
   * Add a listener for the changed state of this file.
   * The listener will be notified only the first time the file changes
   * since the last call to getChanges, getCurrentContent or goOnline.
   * @param fileObserver the listener to add to the set of change listeners
   */
  public void addChangeListener(IFileObserver fileObserver);
  
  /**
   * Remove a change listener from the set of listeners
   * @param fileObserver the listener to remove
   */
  public void removeChangeListener(IFileObserver fileObserver); 
;
  /**
   * TODO: Description
   * @param lock
   * @return
   */
  public boolean requestLock(CoedFileLock lock)
  		throws NotConnectedToServerException;
  
  /**
   * TODO: Description
   * @param lock
   * @return
   */
  public boolean releaseLock(CoedFileLock lock)
  		throws NotConnectedToServerException;
  
  /**
   * Turn on the collaborative editing mode for this file,
   * only works if the object is a file.
   * The method takes the local version of the file and
   * returns its remote version. If the file has not
   * been registered as online yet or the content happens
   * to be the same, the method's input and output will match.
   * @param contents a string  local contents of the file 
   * 	(does not need to be the one saved to the hard disk)
   * @return a string representing 
   */
  public IFuture<String> goOnline(String contents);
  
  /**
   * Turn on the collaborative mode for one or more files,
   * depending on the type of this object.
   * If this object is a file, the method will put only this
   * file online and it will return an array containing only 
   * itself.
   * If it is a folder, the method will recursively put all
   * descendant files online.
   * This method reads the local version of the file[s] from
   * the hard disk and replaces it if necessary with the
   * remote version.
   * @return the list of files now put online as ICoedObjects
   */
  public IFuture<ICoedObject[]> goOnline();
  
  /**
   * Turn off the collaborative mode for this file.
   * No further change updates will be received.
   */
  public void goOffline();
}
