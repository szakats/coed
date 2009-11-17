package coed.base.comm;

public interface ICollabStateObserver {
	/**
	 * Called when the connection state of the collaborator changes.
	 * @param to the new state
	 */
	public void collabStateChanged(String to);
}
