package coed.base.comm;

public interface ICollabStateListener {
	/**
	 * Called when the connection state of the collaborator changes.
	 * @param to the new state
	 */
	public void collabStateChanged(String to);
}
