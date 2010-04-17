/**
 * 
 */
package coed.plugin.base;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.PartInitException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;

import coed.base.comm.ICoedCollaborator;
import coed.base.comm.ICoedCommunicator;
import coed.base.comm.ICollabStateListener;
import coed.base.data.ICoedFile;
import coed.base.data.IFileChangeListener;
import coed.base.data.TextModification;
import coed.base.data.exceptions.InvalidConfigFileException;
import coed.base.data.exceptions.UnknownVersionerTypeException;
import coed.base.util.IFuture;
import coed.base.util.IFuture2Listener;
import coed.base.util.IFutureListener;

import coed.collab.client.CoedCommunicatorFactory;
import coed.plugin.views.ui.AllSessionsView;

/**
 * @author neobi008
 *
 */
public class Controller implements IController, ICollabStateListener, IDocumentListener, IPartListener, IFileChangeListener {
	
	private ICoedCommunicator communicator;
	private String configPath;
	private AllSessionsView allSessionsView;
	private Map<AbstractDecoratedTextEditor,ICoedFile> editorToFile;
	private Map<ICoedFile,AbstractDecoratedTextEditor> fileToEditor;
	private AbstractDecoratedTextEditor activeEditor;
	
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
		activeEditor = null;
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
	
	private void setEditorAndFileAsActive(AbstractDecoratedTextEditor editor) {
		setEditorAsActive(editor);
		ICoedFile file = editorToFile.get(activeEditor);
		if(file != null) file.addChangeListener(this);
	}
	
	private void setEditorAsActive(AbstractDecoratedTextEditor editor){
		activeEditor=editor;
		getEditorDocument(activeEditor).addDocumentListener(this);
		
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
						setEditorAndFileAsActive(editor);
						
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
		
		//TODO if such file selected, that there is no copy on the client machine,
		//create the file phisically on hard drive...( or even the whole directory structure)
		
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
					   
					   Controller controller = ((Controller)Activator.getController()); 
					   if (page != null) {
				
						 //TODO when we will have listeners, remove and then re-add the listener
						   AbstractDecoratedTextEditor editor = (AbstractDecoratedTextEditor)IDE.openEditor(page, file, true);
						   setEditorContent(editor, contents);
						   fileToEditor.put(coedFile,editor);
						   editorToFile.put(editor,coedFile);
						   controller.setEditorAndFileAsActive(editor);
						   System.out.println("editor opened");		
					   }
					   else
					   {
						   page = dw.openPage(null);
						   AbstractDecoratedTextEditor editor = (AbstractDecoratedTextEditor)IDE.openEditor(page, file, true);
						   setEditorContent(editor, contents);
						   fileToEditor.put(coedFile,editor);
						   editorToFile.put((AbstractDecoratedTextEditor)editor,coedFile);
						   controller.setEditorAndFileAsActive(editor);
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
				
				//if does not exists, create directory structure and file
			/*	File fileToOpen = new File(filePath);
				try {
					FileUtils.touch(fileToOpen);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
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
		Display.getCurrent().asyncExec(new InputProcessor(event));	
	}
	
	class InputProcessor implements Runnable{
		DocumentEvent event;
		
		InputProcessor(DocumentEvent event){
			this.event = event;
		}

		@Override
		public void run() {
			// e.g if the content is changed during join
			if(!editorToFile.containsKey(activeEditor))
				return;
			
			System.out.println("Modification sent: "+event.fText);
			editorToFile.get(activeEditor).sendChanges(new TextModification(event.fOffset, event.fLength, event.fText, communicator.getUserName()));		
		}
	}
	
	//-----------------------------------------
	//methods of the IPartListener interface, dealing with activating/deactivating editors

	@Override
	public void partActivated(IWorkbenchPart part) {
		if (editorToFile.containsKey(part)) {
			setEditorAndFileAsActive((AbstractDecoratedTextEditor) part);
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		if (part.equals(activeEditor)){
			getEditorDocument(activeEditor).removeDocumentListener(this);
			editorToFile.get(activeEditor).removeChangeListener(this);
			activeEditor=null;
		}
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		// TODO Auto-generated method stub
		
	}

	//--------------------------------IFileChangeListener methods -------------------------//
	@Override
	public void hasChanges(ICoedFile file) {
		file.getChanges().addListener(new IFutureListener<TextModification[]>() {

			@Override
			public void got(TextModification[] result) {
				IDocument activeDocument = getEditorDocument(activeEditor);
				DocumentUpdater rnbl = new DocumentUpdater(result, activeDocument, Controller.this);
				Display.getDefault().asyncExec(rnbl);	
			}

			@Override
			public void caught(Throwable e) {
				// TODO Auto-generated method stub
				
			}
			
		});	
	}
	
	/** DocumentUpdater - inner class for updating document content according to the changes received **/
	
	class DocumentUpdater implements Runnable {
		
		private TextModification[] mods;
		private IDocument doc;
		private IDocumentListener documentListener;
		
		public DocumentUpdater(TextModification[] mods, IDocument doc, IDocumentListener documentListener){
			this.mods=mods;
			this.doc=doc;
			this.documentListener=documentListener;
		}
		
		public void processChanges(TextModification[] mods, IDocument doc) throws BadLocationException{
			if (mods!=null && mods.length > 0) {
				doc.removeDocumentListener(documentListener);
				for (int i = 0; i < mods.length; i++) {
					doc.replace(mods[i].getOffset(), mods[i].getLength(), mods[i].getText());
				}
				doc.addDocumentListener(documentListener);
			}
		}

		@Override
		public void run() {	
				try {
					processChanges(mods, doc);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
