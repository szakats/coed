package coed.base.common;

import coed.base.data.CoedFile;
import coed.base.data.CoedFileLine;
import coed.base.data.CoedFileLock;
import coed.base.data.IFileObserver;
import coed.base.data.exceptions.NotConnectedToServerException;

public interface ICoedCollaborator {
       public final String STATUS_CONNECTED="Working online";
	   public final String STATUS_ERROR="Could not connect to server";
	   public final String STATUS_OFFLINE="Working offline";
	   
	   public String getState();
	   
	   /**
	    * TODO: Description 
	    * @param file
	    * @param line
	    * @return
	    */
	   public boolean sendChanges(CoedFile file, CoedFileLine line)
	   		throws NotConnectedToServerException;
	   
	   /**
	    * TODO: Description
	    * @param file
	    * @return
	    */
	   public CoedFileLine[] getChanges(CoedFile file)
	   		throws NotConnectedToServerException;
	   
	   /**
	    * TODO: Description
	    * @param file
	    * @return
	    */
	   public String[] getActiveUsers(CoedFile file)
	   		throws NotConnectedToServerException;
	   
	   /**
	    * TODO: Description
	    * @param file
	    * @param fileObserver
	    * @return
	    */
	   public boolean addFileChangeListener(CoedFile file, IFileObserver fileObserver)
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
	   
	   public void addStateListener(ICollabStateObserver stateObserver);
	   
	   public void removeStateListener(ICollabStateObserver stateObserver);
}
