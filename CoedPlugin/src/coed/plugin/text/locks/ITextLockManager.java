package coed.plugin.text.locks;

/**
 * Interface for classes that will manage locks of several files, requesting and releasing them...
 * They will differ, however in how they take the portion of the text to be locked when editing.
 * @author Izso
 *
 */
public interface ITextLockManager {
	
	Long requestLock();
	
	void releaseLock(Long ticket);
	
	void releaseLock();
		
}
