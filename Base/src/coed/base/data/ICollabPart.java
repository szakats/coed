package coed.base.data;

import coed.base.util.IFuture;

public interface ICollabPart {

	/**
	 * Notify the server (and consequently other connected clients)
	 * that a given line has changed.
	 * @param line the line that changed
	 * @return a future containing a boolean value of true
	 * 			if it succeeded or false if for example the line was locked already.
	 * 			TODO: maybe throw an exception with the exact reason of the failure ?
	 * @throws future NotOnlineException
	 */
	public IFuture<Boolean> sendChanges(TextModification line);
	
	/**
	 * Get the full contents of this file from the server.
	 * @return a future string containing the contents
	 * @throws future NotOnlineException
	 */
	public IFuture<String> getRemoteContents();
	
  /**
   * Get the changes that have been done to this file since the last call
   * to either getCurrentContent, getChanges or goOnline.
   * @return a future containing the lines that changed
   * @throws future NotOnlineException
   */
	public IFuture<TextModification[]> getChanges();
  
  /**
   * Get the list of users who are currently editing this file
   * @return a future array of strings containing the names of the users 
   * @throws future NotOnlineException
   */
	public IFuture<String[]> getActiveUsers();
  
  /**
   * Add a listener for the changed state of this file.
   * The listener will be notified only the first time the file changes
   * since the last call to getChanges, getCurrentContent or goOnline.
   * @param listener the listener to add to the set of change listeners
   * @return TODO
   * @throws future NotOnlineException
   */
	public IFuture<Void> addChangeListener(IFileChangeListener listener);
  
	/**
	 * Remove a change listener from the set of listeners
	 * @param listener the listener to remove
	 * @return TODO
	 * @throws future NotOnlineException
	 */
	public IFuture<Void> removeChangeListener(IFileChangeListener listener); 

  /**
   * TODO: Description
   * @param lock
   * @return TODO
   * @throws future NotOnlineException
   */
  public IFuture<Boolean> requestLock(TextPortion lock);
  
  /**
   * TODO: Description
   * @param lock
   * @return
   * @throws future NotOnlineException
   */
  public IFuture<Boolean> releaseLock(TextPortion lock);
  
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
  public IFuture<String> goOnline(String contents);
  
  /**
   * Turn off the collaborative mode for this file.
   * No further change updates will be received.
   * @throws future NotOnlineException
   * @return TODO
   */
  public IFuture<Void> goOffline();
}
