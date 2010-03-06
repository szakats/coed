package coed.base.comm;

public interface ICollabStateListener {
	/**
	 * Called when the connection state of the collaborator changes.
	 * @param to the new state
	 */
	public void collabStateChanged(String to);
	
	/**
	 * Called when an error occurs during authentication
	 */
	public void authenticationError();
}
