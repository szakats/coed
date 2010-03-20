package coed.base.comm;

import coed.base.data.ICoedFile;
import coed.base.data.ICollabFilePart;
import coed.base.util.IFuture;

/**
 * ICoedCollaborator is the component of ICoedCommunicator
 * which handles the collaborative editing in Coed.
 * @author szakats
 *
 */
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
	   public void addStateListener(ICollabStateListener stateObserver);
	   
	   /**
	    * Remove a given ICollabStateObserver from the list of listeners.
	    * @param stateObserver
	    */
	   public void removeStateListener(ICollabStateListener stateObserver);
	   
	   /**
	    * Return all files which have been put online.
	    * @return
	    */
	   public IFuture<ICollabFilePart[]> getAllOnlineFiles();
	   
	   /**
	    * Start trying to connect to the collaboration server
	    * and then keep the connection alive
	    */
	   public void startCollab();
	   
	   /**
	    * Disconnect from the server and stop trying to connect
	    */
	   public void endCollab();
}
