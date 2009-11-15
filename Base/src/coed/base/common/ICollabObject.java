package coed.base.common;

import coed.base.data.CoedFileLine;
import coed.base.data.CoedFileLock;
import coed.base.data.IFileObserver;
import coed.base.data.exceptions.NotConnectedToServerException;

public interface ICollabObject {
	
	public boolean sendChanges(CoedFileLine line)
  		throws NotConnectedToServerException;
  
  /**
   * TODO: Description
   * @param file
   * @return
   */
	public CoedFileLine[] getChanges()
  		throws NotConnectedToServerException;
  
  /**
   * TODO: Description
   * @param file
   * @return
   */
	public String[] getActiveUsers()
  		throws NotConnectedToServerException;
  
  /**
   * TODO: Description
   * @param file
   * @param fileObserver
   * @return
   */
  public boolean addFileChangeListener(IFileObserver fileObserver)
  		throws NotConnectedToServerException;
  
  /**
   * TODO: Description
   * @param fileObserver
   */
  public void addChangeListener(IFileObserver fileObserver);
  
  /**
   * TODO: Description
   * @param fileObserver
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
	*
   */
  public void goOnline();
  
  /**
   * 
   */
  public void goOffline();
}
