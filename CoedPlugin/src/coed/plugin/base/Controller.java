/**
 * 
 */
package coed.plugin.base;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;

import coed.base.comm.ICoedCollaborator;
import coed.base.comm.ICoedCommunicator;
import coed.base.comm.ICollabStateListener;
import coed.base.data.ICoedFile;
import coed.base.data.exceptions.InvalidConfigFileException;
import coed.base.data.exceptions.UnknownVersionerTypeException;
import coed.base.util.Pair;
import coed.collab.client.CoedCommunicatorFactory;
import coed.plugin.views.ui.AllSessionsView;

/**
 * @author neobi008
 *
 */
public class Controller implements IController, ICollabStateListener, IDocumentListener {
	
	private ICoedCommunicator communicator;
	private String configPath;
	private AllSessionsView allSessionsView;
	private Map<AbstractDecoratedTextEditor,ICoedFile> editorToFile;
	private Map<ICoedFile,AbstractDecoratedTextEditor> fileToEditor;
	
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
		
		editorToFile = new HashMap<AbstractDecoratedTextEditor,ICoedFile>();
		fileToEditor = new HashMap<ICoedFile,AbstractDecoratedTextEditor>();
	}
	
	private IDocument getEditorDocument(AbstractDecoratedTextEditor editor) {
		IDocumentProvider provider = editor.getDocumentProvider();
		if(provider == null) return null;
		return provider.getDocument(editor.getEditorInput());
	}
	
	private String getEditorContents(AbstractDecoratedTextEditor editor) {
		IDocument document = getEditorDocument(editor);
		if(document == null) return null;
		return document.get();
	}
	
	private String getEditorFilePath(AbstractDecoratedTextEditor editor) {
		IFile file = (IFile) editor.getEditorInput().getAdapter(IFile.class);
		if(file == null) return null;
		return "/"+file.getProject().getName()+"/"+file.getProjectRelativePath().toString();
	}

	@Override
	public void createSession(AbstractDecoratedTextEditor editor) {
		String path = getEditorFilePath(editor);
		String contents = getEditorContents(editor);
		if(path != null && contents != null) {
			ICoedFile coedFile;
			try {
				coedFile = communicator.createSession(path, contents).get();
				editorToFile.put(editor, coedFile);
				fileToEditor.put(coedFile, editor);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	void setEditorContent(AbstractDecoratedTextEditor editor, String content) {
		IDocument document = getEditorDocument(editor);
		assert document != null;
		document.removeDocumentListener(this);
		document.set(content);
		document.addDocumentListener(this);
	}

	@Override
	public void joinSession(String path, Integer collabID) {
		try {
			Pair<ICoedFile, String> pair = communicator.joinSession(path, collabID).get();
			AbstractDecoratedTextEditor editor = fileToEditor.get(pair.getFirst());
			assert editor != null;
			setEditorContent(editor, pair.getSecond());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void documentChanged(DocumentEvent event) {
		// TODO Auto-generated method stub
		
	}
}
