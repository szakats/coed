package coed.base.comm;

import coed.base.data.ICoedObject;
import coed.base.data.ICollabObject;
import coed.base.util.IFuture;

public interface ICoedCollaborator {
       public final String STATUS_CONNECTED="Working online";
	   public final String STATUS_ERROR="Could not connect to server";
	   public final String STATUS_OFFLINE="Working offline";
	   
	   /**
	    * Get the connection state to the server.
	    * STATUS_OFFLINE means the keep-alive thread is shut down because
	    * there are no online objects
	    * STATUS_ERROR means that the client is trying to connect but has not
	    * succeeded yet
	    * STATUS_CONNECTED means the connection is fully established
	    * @return the state
	    */
	   public String getState();
	   
	   /**
	    * Add an observer which gets notified when the connection state
	    * of the collaborator to the server changes.
	    * @param stateObserver
	    */
	   public void addStateListener(ICollabStateObserver stateObserver);
	   
	   /**
	    * Remove a given ICollabStateObserver from the list of listeners.
	    * @param stateObserver
	    */
	   public void removeStateListener(ICollabStateObserver stateObserver);

	   /**
	    * Make a new a ICollabObject and set it as a child of the given ICoedObject.
	    * The new object will be created in accordance with the path in its parent.
	    * @param obj the parent of the object to be created
	    * @return the new collaboration
	    */
	   public ICollabObject makeCollabObject(ICoedObject obj);
	   
	   /**
	    * Return all files which have been put online.
	    * @return
	    */
	   public IFuture<ICollabObject[]> getAllOnlineFiles();
}
