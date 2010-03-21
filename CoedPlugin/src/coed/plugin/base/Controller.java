/**
 * 
 */
package coed.plugin.base;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.ui.PartInitException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.jdt.ui.actions.*;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;

import coed.base.comm.ICoedCollaborator;
import coed.base.comm.ICoedCommunicator;
import coed.base.comm.ICollabStateListener;
import coed.base.data.ICoedFile;
import coed.base.data.exceptions.InvalidConfigFileException;
import coed.base.data.exceptions.UnknownVersionerTypeException;
import coed.base.util.IFuture2Listener;
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
			class CreateJob implements Runnable {
				String contents, path;
				AbstractDecoratedTextEditor editor;
			
				public CreateJob(String contents, String path,
						AbstractDecoratedTextEditor editor) {
					super();
					this.contents = contents;
					this.path = path;
					this.editor = editor;
				}

				@Override
				public void run() {
					try {
						
						ICoedFile coedFile = communicator.createSession(path, contents).get();
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
			
			Display.getDefault().asyncExec(new CreateJob(contents, path, editor));
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
		
		class OpenEditorJob implements Runnable{
			
			private IFile file;
			private ICoedFile coedFile;
			private String contents;
			
			public OpenEditorJob(IFile file, ICoedFile coedFile, String contents){
				this.file = file;
				this.coedFile = coedFile;
				this.contents = contents;
			}
			
			@Override
			public void run() {
				IWorkbenchWindow dw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				try {
				   if (dw != null) {
					   IWorkbenchPage page = dw.getActivePage();
					   System.out.println("workbench window accessed");
					   if (page != null) {
						   IEditorPart editor = IDE.openEditor(page, file, true); 
						 //TODO when we will have listeners, remove and then re-add the listener
						   ((AbstractDecoratedTextEditor)editor).getDocumentProvider().getDocument(editor.getEditorInput()).set(contents);
						   fileToEditor.put(coedFile,(AbstractDecoratedTextEditor)editor);
						   editorToFile.put((AbstractDecoratedTextEditor)editor,coedFile);
						   System.out.println("editor opened");
					   }
					   else
					   {
						   page = dw.openPage(null);
						   IEditorPart editor = IDE.openEditor(page, file, true);
						   //TODO when we will have listeners, remove and then re-add the listener
						   ((AbstractDecoratedTextEditor)editor).getDocumentProvider().getDocument(editor.getEditorInput()).set(contents);
						   fileToEditor.put(coedFile,(AbstractDecoratedTextEditor)editor);
						   editorToFile.put((AbstractDecoratedTextEditor)editor,coedFile);
						   System.out.println("editor opened new page");	   
					   }
				   }
				} catch (PartInitException e) {
				    	e.printStackTrace();	
				} catch (WorkbenchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}	
		
		class FListener implements IFuture2Listener<ICoedFile, String> {

			@Override
			public void got(ICoedFile result1, String result2) {
				
				String filePath = ResourcesPlugin.getWorkspace().getRoot().getRawLocation().toOSString()+"\\testttt\\"+result1.getPath();
				//File fileToOpen = new File(filePath);
				
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(filePath));
				System.out.println("opening file "+filePath);
				
				
				Display.getDefault().asyncExec(new OpenEditorJob(file,result1,result2));
			}

			@Override
			public void caught(Throwable e) {
				e.printStackTrace();
			}
		}
		communicator.joinSession(path, collabID).addListener(new FListener());
	}

	@Override
	public void leaveSession(AbstractDecoratedTextEditor editor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collabStateChanged(String to) {
		if(to == ICoedCollaborator.STATUS_CONNECTED) {
			allSessionsView.notifyConnected();
			
			if(allSessionsView != null) {
				class GetSessionJob implements Runnable {

					@Override
					public void run() {
						try {
							Map<Integer, String> sessionMap = communicator.getCollabSessions().get();
							for(Map.Entry<Integer, String> e : sessionMap.entrySet()) {
								allSessionsView.addFile(e.getValue(), e.getKey().toString());
								System.out.println("got session " + e.getKey());
							}
							if(!sessionMap.isEmpty()) {
								allSessionsView.refreshView();
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				Display.getDefault().asyncExec(new GetSessionJob());
			}
		}
	}

	@Override
	public void logoffFromServer() {
		communicator.endCollab();
	}

	@Override
	public void loginToServer() {
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
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				Status status = new Status(IStatus.ERROR, "CoED plugin", 0,
						"Invalid username or password", null);

		        // Display the dialog
		        ErrorDialog.openError(Display.getCurrent().getActiveShell(),
		            "CoED Error", "Authentication Failure", status);
			}
		});
		allSessionsView.notifyAuthError();
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
