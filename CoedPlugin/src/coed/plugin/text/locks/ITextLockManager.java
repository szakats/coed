package coed.plugin.text.locks;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import coed.base.data.ICoedFile;
import coed.base.data.TextPortion;

/**
 * Interface for classes that will manage locks of several files, requesting and releasing them...
 * They will differ, however in how they take the portion of the text to be locked when editing.
 * @author Izso
 *
 */
public interface ITextLockManager {
	
	public TextPortion requestLock(IDocument doc, ICoedFile coedo, Integer lineNr);
	
	void releaseLock(ICoedFile coedo, TextPortion ticket);
	
	void findAndReleaseLocks(ICoedFile coedo, TextPortion zone);
		
}
