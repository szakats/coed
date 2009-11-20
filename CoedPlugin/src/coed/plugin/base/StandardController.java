package coed.plugin.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import coed.base.comm.ICoedCommunicator;
import coed.base.comm.ICollabStateObserver;
import coed.base.data.ICoedObject;
import coed.base.data.IFileChangeListener;
import coed.base.data.TextModification;
import coed.base.data.TextPortion;
import coed.base.data.exceptions.InvalidConfigFileException;
import coed.base.data.exceptions.NotConnectedToServerException;
import coed.base.data.exceptions.UnknownVersionerTypeException;
import coed.base.util.IFuture;
import coed.plugin.exceptions.GetFileInEditorException;
import coed.plugin.mocksfordebug.MockCoedCollaborator;
import coed.plugin.views.IFileTree;
import coed.plugin.views.IUserList;

/**
 * Class that controls all actions in plugin.
 * Views and menus of the plugin register with this class in order to operate. 
 * @author Izso
 *
 */
public class StandardController implements IPluginController, IPartListener, IFileChangeListener, IDocumentListener, ICollabStateObserver {
	/**
	 * Location of the config file. Will be an absolute path.
	 */
	private String configLocation;
	
	/**
	 * An instance of the ICoedCommunictor, used to communicate with the server
	 */
	private ICoedCommunicator communicator;
	
	/**
	 * Currently managed editors, coupled with their ICoedObjects
	 */
	private HashMap<AbstractDecoratedTextEditor,ICoedObject> editors;
	
	/**
	 * The editor which is on-top at the moment
	 */
	private AbstractDecoratedTextEditor activeEditor;
	
	/**
	 * The document part of the active editor
	 */
	private IDocument activeDocument;
	
	/**
	 * List of annotations made to the document by the plugin.
	 */
	private ArrayList<Annotation> annotations;
	
	/**
	 * Reference to the attached file view. It shows the online documents on the server.
	 */
	private IFileTree fileTree;
	
	/**
	 * Reference to the users view. It shows the online users on the server.
	 */
	private IUserList userList;
	
	/**
	 * Index of the event to ignore in the IDocumentListener part
	 */
	private Long ignoreEvent= null;
	
	/**
	 * Keeping last thread to be able to wait for its completion
	 */
	private Thread lastUpdate;
	
	/**
	 * Reference to currently locked lines for the user - in form of an array of line numbers
	 */
	private TextPortion lockedLines;
	
	public StandardController(){
		//TODO: ask an ICoedCommunicator-factory to give us an instance
		configLocation=ResourcesPlugin.getWorkspace().getRoot().getRawLocation().toOSString();
		try {
			this.communicator = new MockCoedCollaborator();//new CoedCommunicatorFactory().create(configLocation);
		} catch (UnknownVersionerTypeException e) {
			// TODO Auto-generated catch block
			this.communicator=null;
			e.printStackTrace();
		} catch (InvalidConfigFileException e) {
			// TODO Auto-generated catch block
			this.communicator=null;
			e.printStackTrace();
			MessageDialog.openError(null, "Coed Plugin - Startup exception", "The configuration file contains errors!\nEither disable this plugin, or fix the config file (workspace\\.coed\\config.ini)");
		}
		
		this.editors = new HashMap<AbstractDecoratedTextEditor, ICoedObject>();
		this.annotations = new ArrayList<Annotation>(); 
		this.activeEditor = null;
	}

	@Override
	public boolean attachFileTree(IFileTree filet) {
		//TODO: not maybe it should be a list...
		this.fileTree=filet;
		return true;	
	}
	
	@Override
	public void detachFileTree(IFileTree filet) {
		//TODO: not maybe it should be a list...
		if (this.fileTree.equals(filet)) this.fileTree=null;
		
	}

	@Override
	public boolean attachUserList(IUserList userl) {
		//TODO: not maybe it should be a list...
		this.userList=userl;
		return true;
	}

	@Override
	public void detachUserList(IUserList userl) {
		//TODO: not maybe it should be a list...
		if (this.userList.equals(userl)) this.userList=null;
	}
	
	private ICoedObject findCoedFileFor(AbstractDecoratedTextEditor texte) throws GetFileInEditorException{
	   IFile file =null;
	    	
	   file = (IFile) texte.getEditorInput().getAdapter(IFile.class);
	   
	   if (file!=null) {
		   if (communicator!=null && file!=null) {
			   return communicator.getObject(file.getProject().getName()+"/"+file.getProjectRelativePath().toString());
		   } else {
			   throw new GetFileInEditorException();
		   }
	   }
	   throw new GetFileInEditorException();
	}
	
	private void setAsActive(AbstractDecoratedTextEditor texte){
		activeEditor=texte;
		activeDocument=activeEditor.getDocumentProvider().getDocument(activeEditor.getEditorInput());
		activeDocument.addDocumentListener(this);
		editors.get(texte).addChangeListener(this);
		//TODO: should update based on changes
		//TODO: need to listen for user input 
	}
	
	@Override
	public void startCollabFor(AbstractDecoratedTextEditor texte) {
		if (texte==null) {
			MessageDialog.openError(null, "Coed Plugin", "Can not start collaborative editing if no editor is open!");
			return;
		}
		if (communicator==null || communicator.getState()==ICoedCommunicator.STATUS_OFFLINE) {
			return;
		}
		
		if (editors.get(texte)!=null) {
			setAsActive(texte);
			return;
		} 
		
		try {			
			editors.put(texte, findCoedFileFor(texte));
			setAsActive(texte);
			/*DEBUG*/System.out.println("Going collab for: "+texte);
			texte.getSite().getPage().addPartListener(this);
			
			editors.get(activeEditor).goOnline();
		} catch (GetFileInEditorException e) {
			MessageDialog.openError(null, "Coed Plugin - File Error", "There had been an error when determining the location of the file you are" +
					"currently editing.\nPlease refresh the editor, or reopen the file and try again!");
		}	
	}

	@Override
	public void endCollabFor(AbstractDecoratedTextEditor texte) {
		//TODO: maybe do some stuff before closing
		//TODO: restore contents if needed
		
		/*DEBUG*/System.out.println("Ending collab for: "+texte);
		
		if (activeEditor!=null && activeEditor.equals(texte)) {
			activeDocument.removeDocumentListener(this);
			editors.get(texte).removeChangeListener(this);
			editors.get(texte).goOffline();
			activeDocument=null;
			activeEditor=null;
		}
		editors.remove(texte);
		//last one closes the door
		if (editors.size()==0) {
			texte.getSite().getPage().removePartListener(this);
		}
	}

	@Override
	public String[] getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getCollabUsers(ICoedObject file) {
		try {
			return file.getActiveUsers().get();
		} catch(NotConnectedToServerException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean requestVersionAction(String action, ICoedObject file) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean requestVersionAction(String action) {
		// TODO Auto-generated method stub
		return false;
	}

	//----------------------------------------------------
	//Listeners for editor activation, deactivation.
	//Won't use all of them, because we are not really interested in every action :)
	
	
	@Override
	public void partActivated(IWorkbenchPart part) {
		if (editors.containsKey(part)) {
			setAsActive((AbstractDecoratedTextEditor) part);
			/*DEBUG*/System.out.println("Resumed : "+part);
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		//not doing anything because activate is fired after 'to top'.
		//so listening to that instead.
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		if (editors.containsKey(part)) {
			//not try-catching here, because we are pretty sure if it
			// is in the map, it is ok.
			endCollabFor((AbstractDecoratedTextEditor)part);
		}
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		if (part.equals(activeEditor)){
			activeDocument.removeDocumentListener(this);
			activeEditor=null;
			activeDocument=null;
			/*DEBUG*/System.out.println("Paused : "+part);
		}
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		//not doing anything here. :-"
	}

	//This is the part where the controller receives info about changes to coed files.
	//At this moment it will update the active document, and ignore other updates.
	//ATTENTION: This is just an informing method, it does not contain the actual data
	
	public synchronized void hasChanges(ICoedObject file) {
		//TODO: real equality checking
		if (activeEditor!=null && editors.get(activeEditor).getPath().equals(file.getPath())) {
			DocumentUpdater rnbl = new DocumentUpdater(file, activeDocument, lastUpdate, this);
			lastUpdate=rnbl;
			Display.getDefault().asyncExec(rnbl);		
		}
	}

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
		lockedLines = new TextPortion(event.fOffset, event.fLength);
	}

	@Override
	public void documentChanged(DocumentEvent event) {
		Thread t = new InputProcessor(this, lockedLines, event);
		//t.start();
		Display.getCurrent().asyncExec(t);
	}
	
	/**
	 * Inner class for handling and reverting inputs to the document.
	 * Work is needed here....
	 * @author Izso
	 *
	 */
	class InputProcessor extends Thread {
		StandardController outer;
		DocumentEvent event;
		TextPortion lock;
		
		public InputProcessor (StandardController outer, TextPortion lock, DocumentEvent event){
			this.outer=outer;
			this.event=event;
			this.lock=lock;
		}
		
		public void run(){
			try {
				if (!editors.get(activeEditor).requestLock(lock)) {
					ignoreEvent=event.fModificationStamp;
					lock=null;
				} 
			} catch (NotConnectedToServerException e) {
				e.printStackTrace();
			}
			if (ignoreEvent!=null && ignoreEvent.equals(event.fModificationStamp)){
				
				ignoreEvent=null;
				try {
					Annotation ann= new Annotation("org.eclipse.ui.workbench.texteditor.spelling", true, "This is line is currently being edited by someone else");
					annotations.add(ann);
					Integer eLine;
					eLine = event.fDocument.getLineOffset(event.fOffset);
					activeEditor.getDocumentProvider().getAnnotationModel(activeEditor.getEditorInput()).
								addAnnotation(ann, new Position(activeDocument.getLineOffset(eLine),activeDocument.getLineLength(eLine))
								);
				} catch (BadLocationException e1) {
					System.out.print("StandardController:InputProcessor");e1.printStackTrace();
				}
				try {
					//clean up the mess :)
					activeDocument.removeDocumentListener(outer);
					synchronized (event.fDocument) {
						event.fDocument.replace(event.fOffset, event.getText().length(), "");
					}
					activeDocument.addDocumentListener(outer);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				
			} else if (lock!=null) {
				//if I've got something to send:
				try {
					editors.get(activeEditor).sendChanges(new TextModification(event.fOffset, event.fLength, event.fText));
					editors.get(activeEditor).releaseLock(lock);
					lockedLines=null;
				} catch (NotConnectedToServerException e) {
					e.printStackTrace();
				}
			}
				
		
		}
	}
	
	/**
	 * Class for displaying the changes of a document
	 * @author Izso
	 *
	 */
	class DocumentUpdater extends Thread {
		private ICoedObject file;
		private IDocument doc;
		private Thread waitOn;
		private IDocumentListener outer;
		
		public DocumentUpdater(ICoedObject file, IDocument doc, Thread waitOn, IDocumentListener outer) {
			this.file=file;
			this.doc=doc;
			 this.waitOn=waitOn;
			 this.outer=outer;
		}
		
		public void showChanges(ICoedObject file, IDocument doc) throws BadLocationException, NotConnectedToServerException {
			IFuture<TextModification[]> modsF;
			TextModification[] mods = null;
			try {
				modsF = file.getChanges();
				mods = modsF.get();
			} catch(NotConnectedToServerException e) {
				e.printStackTrace();
				throw e;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (mods!=null && mods.length > 0) {
				for (int i = 0; i < mods.length; i++) {
					doc.removeDocumentListener(outer);
					doc.replace(mods[i].getOffset(), mods[i].getLength(), mods[i].getText());
					doc.addDocumentListener(outer);
					
				}
			}
		}

		@Override
		public void run() {
			if (waitOn!=null){
				while(waitOn.isAlive()) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				showChanges(file, doc);
			} catch (NotConnectedToServerException e) {
				e.printStackTrace();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			this.notifyAll();
		}
	}

	@Override
	public void collabStateChanged(String to) {
		// TODO Auto-generated method stub
		
	}
}
