package coed.plugin.text.locks;

import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import coed.base.data.ICoedObject;
import coed.base.data.TextPortion;

/**
 * Interface for classes that will manage locks of several files, requesting and releasing them...
 * They will differ, however in how they take the portion of the text to be locked when editing.
 * @author Izso
 *
 */
public interface ITextLockManager {
	
	public TextPortion requestLock(AbstractDecoratedTextEditor texte, ICoedObject coedo, TextPortion textp);
	
	void releaseLock(ICoedObject coedo, TextPortion ticket);
	
	void findAndReleaseLocks(ICoedObject coedo, TextPortion zone);
		
}
