/**
 * 
 */
package coed.plugin.base;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import coed.base.comm.ICoedCollaborator;
import coed.base.comm.ICoedCommunicator;
import coed.base.comm.ICollabStateListener;
import coed.base.data.exceptions.InvalidConfigFileException;
import coed.base.data.exceptions.UnknownVersionerTypeException;
import coed.collab.client.CoedCommunicatorFactory;
import coed.plugin.views.ui.AllSessionsView;

/**
 * @author neobi008
 *
 */
public class Controller implements IController, ICollabStateListener{
	
	ICoedCommunicator communicator;
	String configPath;
	AllSessionsView allSessionsView;
	
	Controller() {
		configPath = ResourcesPlugin.getWorkspace().getRoot().getRawLocation().toOSString();
		
		try {
			communicator = new CoedCommunicatorFactory().create(configPath);
			
			communicator.addStateListener(this);
		} catch (InvalidConfigFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownVersionerTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void createSession(AbstractDecoratedTextEditor editor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void joinSession(String path, String sessionID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leaveSession(AbstractDecoratedTextEditor editor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collabStateChanged(String to) {
		if(to == ICoedCollaborator.STATUS_CONNECTED) {
			allSessionsView.notifyConnected();
		}
	}

	@Override
	public void endCollaboration() {
		communicator.endCollab();
	}

	@Override
	public void startCollaboration() {
		communicator.startCollab();
	}

	@Override
	public String getCollabState() {
		return communicator.getState();
	}

	@Override
	public void attachView(AllSessionsView view) {
		allSessionsView = view;
	}

	@Override
	public void authenticationError() {
		Display.getDefault().asyncExec(new AuthErrorMessage());
		allSessionsView.notifyAuthError();
	}
	
	class AuthErrorMessage implements Runnable {

		@Override
		public void run() {
			Status status = new Status(IStatus.ERROR, "CoED plugin", 0,
					"Invalid username or password", null);

	        // Display the dialog
	        ErrorDialog.openError(Display.getCurrent().getActiveShell(),
	            "CoED Error", "Authentication Failure", status);
		}
	}
}
