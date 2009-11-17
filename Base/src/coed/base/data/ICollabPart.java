package coed.base.data;

import coed.base.data.exceptions.NotConnectedToServerException;
import coed.base.util.IFuture;

public interface ICollabPart {

	public boolean sendChanges(CoedFileLine line)
  		throws NotConnectedToServerException;
	
	public IFuture<String> getCurrentContent();
	
  /**
   * TODO: Description
   * @param file
   * @return
   */
	public IFuture<CoedFileLine[]> getChanges()
  		throws NotConnectedToServerException;
  
  /**
   * TODO: Description
   * @param file
   * @return
   */
	public IFuture<String[]> getActiveUsers()
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
  public IFuture<Boolean> goOnline();
  
  /**
   * 
   */
  public void goOffline();
}
