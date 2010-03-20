package coed.plugin.base;

import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import coed.plugin.views.ui.AllSessionsView;

public interface IController {
	public void createSession(AbstractDecoratedTextEditor editor);
	public void joinSession(String path, Integer sessionID);
	public void leaveSession(AbstractDecoratedTextEditor editor);
	
	public void startCollaboration();
	public void endCollaboration();
	
	public String getCollabState();
	
	public void attachView(AllSessionsView view);
}