package coed.base.common;

import coed.base.data.CoedFile;
import coed.base.data.CoedFileLine;
import coed.base.data.CoedFileLock;
import coed.base.data.IFileObserver;

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
	   public boolean sendChanges(CoedFile file, CoedFileLine line);
	   
	   /**
	    * TODO: Description
	    * @param file
	    * @return
	    */
	   public CoedFileLine[] getChanges(CoedFile file);
	   
	   /**
	    * TODO: Description
	    * @param file
	    * @return
	    */
	   public String[] getActiveUsers(CoedFile file);
	   
	   /**
	    * TODO: Description
	    * @param file
	    * @param fileObserver
	    * @return
	    */
	   public boolean listenToChanges(CoedFile file, IFileObserver fileObserver);

	   /**
	    * TODO: Description
	    * @param lock
	    * @return
	    */
	   public boolean requestLock(CoedFileLock lock);
	   
	   /**
	    * TODO: Description
	    * @param lock
	    * @return
	    */
	   public boolean releaseLock(CoedFileLock lock);
}
