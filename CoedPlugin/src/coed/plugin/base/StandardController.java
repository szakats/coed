package coed.plugin.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.WorkbenchPart;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import coed.base.comm.ICoedCommunicator;
import coed.base.data.CoedFileLine;
import coed.base.data.CoedFileLock;
import coed.base.data.ICoedObject;
import coed.base.data.IFileObserver;
import coed.base.data.exceptions.InvalidConfigFileException;
import coed.base.data.exceptions.NotConnectedToServerException;
import coed.base.data.exceptions.UnknownVersionerTypeException;
import coed.collab.client.CoedCommunicatorFactory;
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
public class StandardController implements IPluginController, IPartListener, IFileObserver, IDocumentListener {
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
	
	private Thread lastUpdate;
	
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
     
		if (communicator!=null && file!=null) {
			return communicator.getObject(file.getProject().getName()+"/"+file.getProjectRelativePath().toString());
		} else {
			throw new GetFileInEditorException();
		} 
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
		if (communicator==null || communicator.getState()==ICoedCommunicator.STATUS_OFFLINE) {
			reconnectDialog();
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
			return file.getActiveUsers();
		} catch(NotConnectedToServerException e) {
			e.printStackTrace();
			reconnectDialog();
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
	
	public synchronized void update(ICoedObject file) {
		//TODO: real equality checking
		if (activeEditor!=null && editors.get(activeEditor).getPath().equals(file.getPath())) {
			DocumentUpdater rnbl = new DocumentUpdater(file, activeDocument, lastUpdate, this);
			lastUpdate=rnbl;
			Display.getDefault().asyncExec(rnbl);		
		}
	}
	
	
	protected void reconnectDialogWithRestart(Thread tor){
		if (reconnectDialog()==true) tor.start();
	}
	
	private boolean reconnectDialog(){
		while ((communicator==null || communicator.getState()!=ICoedCommunicator.STATUS_CONNECTED) && MessageDialog.openQuestion(
				null,
				"Coed Plugin",
				"The server could not be contacted.\n\nDo you want to try connecting again?")
			) {
			
			try {
				this.communicator = new MockCoedCollaborator();//new CoedCommunicatorFactory().create(configLocation);
			} catch (UnknownVersionerTypeException e) {
				// TODO Auto-generated catch block
				this.communicator=null;
				e.printStackTrace();
				return false;
			} catch (InvalidConfigFileException e) {
				// TODO Auto-generated catch block
				this.communicator=null;
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
		System.out.println("About to be changed "+event.fModificationStamp);
		try {
			ArrayList<Integer> affLines = new ArrayList<Integer>(); 
			Integer fline = activeDocument.getLineOfOffset(event.fOffset);
			affLines.add(fline);
			
			Integer[] lines ={activeDocument.getLineOfOffset(event.fOffset)};
			if (!editors.get(activeEditor).requestLock(new CoedFileLock(lines))) ignoreEvent=event.fModificationStamp+1;
		} catch (BadLocationException e) {
			e.printStackTrace();
		} catch (NotConnectedToServerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void documentChanged(DocumentEvent event) {
		Integer[] lines = new Integer[1];
		try {
			lines[0]=activeDocument.getLineOfOffset(event.fOffset);
		} catch (BadLocationException e2){// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		if (ignoreEvent!=null && ignoreEvent.equals(event.fModificationStamp)){
			ignoreEvent=null;
			System.out.println("NOT ALLOWED: Changed "+event.fModificationStamp);
			try {
				Annotation ann= new Annotation("org.eclipse.ui.workbench.texteditor.spelling", true, "This is line is currently being edited by someone else");
				annotations.add(ann);
				activeEditor.getDocumentProvider().getAnnotationModel(activeEditor.getEditorInput()).
							addAnnotation(ann, new Position(activeDocument.getLineOffset(lines[0]),activeDocument.getLineLength(lines[0]))
							);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				activeDocument.removeDocumentListener(this);
				event.fDocument.replace(event.fOffset, event.getText().length(), "");
				activeDocument.addDocumentListener(this);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (lines[0]!=null) {
			System.out.println("Changed "+event.fModificationStamp);
			//build coedFileLines
			//ArrayList<CoedFileLine> lines= new ArrayList<CoedFileLine>();
			
			String[] text = {event.fText};
			
			
			try {
				editors.get(activeEditor).sendChanges(new CoedFileLine(activeDocument.getLineOfOffset(event.fOffset), text, "editing"));
				editors.get(activeEditor).releaseLock(new CoedFileLock(lines));
			} catch (NotConnectedToServerException e) {
				reconnectDialog();
				e.printStackTrace();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
			
	}
	
	
	
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
			CoedFileLine[] lines;
			try {
				lines = file.getChanges();
			} catch(NotConnectedToServerException e) {
				e.printStackTrace();
				throw e;
			}
			if (lines.length > 0) {
				for (int i = 0; i < lines.length; i++) {
					String[] text = lines[i].getText();
					Integer lineNum = lines[i].getLine();
					Integer curOffs = doc.getLineOffset(lineNum);
					doc.replace(curOffs, doc.getLineLength(lineNum), (text==null || text[0]==null) ? "" : text[0]+"\n");
					if (text!=null && text[0]!=null) {
						curOffs+=text[0].length()+1;
						for (int j = 1; j < text.length; j++) {
							doc.replace(curOffs, 0, text[j]+"\n");
							curOffs+=text[j].length()+1;
						}
					} 
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				doc.removeDocumentListener(outer);
				showChanges(file, doc);
			} catch (NotConnectedToServerException e) {
				e.printStackTrace();
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				doc.addDocumentListener(outer);
			}
			this.notifyAll();
		}
	}
}
