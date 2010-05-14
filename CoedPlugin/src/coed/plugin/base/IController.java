package coed.plugin.base;

import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import coed.base.config.Config;
import coed.base.config.ICoedConfig;
import coed.plugin.views.UserListView;
import coed.plugin.views.ui.AllSessionsView;
import coed.plugin.views.ui.UsersView;

public interface IController {
	public void createSession(AbstractDecoratedTextEditor editor);
	public void joinSession(String path, Integer sessionID);
	public void leaveSession(AbstractDecoratedTextEditor editor);
	
	public void loginToServer();
	public void logoffFromServer();
	
	public String getCollabState();
	
	public void attachAllSessionsView(AllSessionsView view);
	public void attachUsersView(UsersView view);
	
	public ICoedConfig getConfig();
}