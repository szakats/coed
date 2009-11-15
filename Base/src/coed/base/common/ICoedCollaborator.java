package coed.base.common;

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
	
	   
	   /**
	    * 
	    * @param stateObserver
	    */
	   public void addStateListener(ICollabStateObserver stateObserver);
	   
	   /**
	    * 
	    * @param stateObserver
	    */
	   public void removeStateListener(ICollabStateObserver stateObserver);

	   /**
	    * 
	    * @param obj
	    * @return
	    */
	   public ICollabObject makeCollabObject(ICoedObject obj);
}
