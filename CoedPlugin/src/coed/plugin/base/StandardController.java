package coed.plugin.base;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Synchronizer;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import coed.base.comm.ICoedCommunicator;
import coed.base.comm.ICollabStateListener;
import coed.base.config.Config;
import coed.base.data.ICoedObject;
import coed.base.data.IFileChangeListener;
import coed.base.data.TextModification;
import coed.base.data.TextPortion;
import coed.base.data.exceptions.InvalidConfigFileException;
import coed.base.data.exceptions.NotConnectedException;
import coed.base.data.exceptions.UnknownVersionerTypeException;
import coed.base.util.IFuture;
import coed.base.util.IFutureListener;
import coed.collab.client.CoedCommunicatorFactory;
import coed.collab.client.CollaboratorClient;
import coed.collab.client.Communicator;
import coed.plugin.exceptions.GetFileInEditorException;
import coed.plugin.mocksfordebug.MockCoedCollaborator;
import coed.plugin.text.locks.ITextLockManager;
import coed.plugin.text.locks.TextLockFactory;
import coed.plugin.views.IFileTree;
import coed.plugin.views.IUserList;

/**
 * Class that controls all actions in plugin.
 * Views and menus of the plugin register with this class in order to operate. 
 * @author Izso
 *
 */
public class StandardController implements IPluginController, IPartListener, IFileChangeListener, IDocumentListener, ICollabStateListener {
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
	 * Reference to currently locked lines for the user - in form of an array of line numbers
	 */
	private TextPortion lockedLines;
	
	/**
	 * The lock manager instance for this class. 
	 * It is needed because several strategies can be used for locking.
	 */
	private ITextLockManager locker;
	
	/**
	 * Username of the current user. It is needed to be put in the meta-info of changes.
	 */
	private String userName;
	
	/**
	 * Log4j logger for this class - INFO level will be set
	 */
	private final static Logger logger = Logger.getLogger(StandardController.class.toString());
	
	public StandardController(){
		//TODO: ask an ICoedCommunicator-factory to give us an instance
		configLocation=ResourcesPlugin.getWorkspace().getRoot().getRawLocation().toOSString();
		
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.INFO);
		logger.info("Starting with config file at "+configLocation);
		
		try {
			this.communicator = new CoedCommunicatorFactory().create(configLocation);
			communicator.startCollab();
			
			//getting user name
			Config conf = new Config(configLocation+"\\.coed\\config.ini");
			userName=conf.getString("user.name");
			
			//settign up lock strategy
			String lockStrategy=(conf.getString("lock.strategy")==null) ? TextLockFactory.BASIC_MANAGER : conf.getString("lock.strategy");
			Integer lockDelay=(conf.getInt("lock.release.delay")==null) ? 0 : conf.getInt("lock.release.delay");
			logger.info("Starting with lock manager: "+lockStrategy+" and delay "+lockDelay);
			this.locker=TextLockFactory.getManagerFor(lockStrategy,lockDelay);
			
		} catch (UnknownVersionerTypeException e) {
			// TODO Auto-generated catch block
			this.communicator=null;
			e.printStackTrace();
		} catch (InvalidConfigFileException e) {
			// TODO Auto-generated catch block
			this.communicator=null;
			e.printStackTrace();
			MessageDialog.openError(null, "Coed Plugin - Startup exception", "The configuration file contains errors!\nEither disable this plugin, or fix the config file (workspace\\.coed\\config.ini)");
		} finally {
			if (this.locker==null)
				try {
					this.locker=TextLockFactory.getManagerFor(TextLockFactory.BASIC_MANAGER);
				} catch (InvalidConfigFileException e) {
					//This should be OK
				}
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
			   return communicator.getObject("/"+file.getProject().getName()+"/"+file.getProjectRelativePath().toString());
		   } else {
			   throw new GetFileInEditorException();
		   }
	   }
	   throw new GetFileInEditorException();
	}
	
	/**
	 * Sets a given editor as THE active editor, i.e it is on top, and user is editing.
	 * It performs some extra actions, like displaying user list editing the given doc.
	 * @param texte
	 */
	private void setAsActive(AbstractDecoratedTextEditor texte){
		activeEditor=texte;
		activeDocument=activeEditor.getDocumentProvider().getDocument(activeEditor.getEditorInput());
		activeDocument.addDocumentListener(this);
		editors.get(texte).addChangeListener(this);
		
		//displaying active users.
		if (this.userList != null) {
			/*editors.get(texte).getActiveUsers().addListener(new IFutureListener<String[]>() {
				
				@Override
				public void caught(Throwable e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void got(String[] result) {
					userList.displayUsers(result);
				}
			});*/
		}
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
			logger.info("Going collab for: "+texte);
			texte.getSite().getPage().addPartListener(this);
			String content = editors.get(activeEditor).goOnline(activeEditor.getDocumentProvider().getDocument(activeEditor.getEditorInput()).get()).get();
			if (content!=null) {
				activeDocument.removeDocumentListener(this);
				activeEditor.getDocumentProvider().getDocument(activeEditor.getEditorInput()).set(content);
				activeDocument.addDocumentListener(this);
			}
			fileTree.displayFile(editors.get(activeEditor));
		/*
			IWorkbench wb = PlatformUI.getWorkbench();
		 	IProgressService ps = wb.getProgressService();
		 	ps.busyCursorWhile(new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					
					try {
						String content = editors.get(activeEditor).goOnline(activeEditor.getDocumentProvider().getDocument(activeEditor.getEditorInput()).get()).get();
						activeEditor.getDocumentProvider().getDocument(activeEditor.getEditorInput()).set(content);
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});*/
		} catch (GetFileInEditorException e) {
			MessageDialog.openError(null, "Coed Plugin - File Error", "There had been an error when determining the location of the file you are" +
					"currently editing.\nPlease refresh the editor, or reopen the file and try again!");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Override
	public void endCollabFor(AbstractDecoratedTextEditor texte) {
		//TODO: maybe do some stuff before closing
		//TODO: restore contents if needed
		
		logger.info("Ending collab for: "+texte);
		
		fileTree.removeFile(editors.get(activeEditor));
		
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
			logger.info("Resumed : "+part);
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
			logger.info("Paused : "+part);
		}
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		//not doing anything here. :-"
	}

	//This is the part where the controller receives info about changes to coed files.
	//At this moment it will update the active document, and ignore other updates.
	//ATTENTION: This is just an informing method, it does not contain the actual data
	
	private boolean inQueueFlag = false;
	private int got = 0, shown = 0;
	
	Thread watcherThread = null;
	
	class WatcherThread extends Thread
	{
		public void run() {
			while(true) {
				if(got < shown) {
					int got1 = got, shown1 = shown;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(got == got1 && shown == shown1) {
						System.out.println("stalled");
					}
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public synchronized void hasChanges(ICoedObject file) {
		//TODO: real equality checking
		logger.info("Collab file has changes: "+file.getPath());
		//if (activeEditor!=null && editors.get(activeEditor).getPath().equals(file.getPath())) {
			DocumentUpdater rnbl = new DocumentUpdater(file, activeDocument, this);
			if(inQueueFlag) {
				System.out.println("attempt to add one more to queue");
				//return;
			}
			if(watcherThread == null) {
				watcherThread = new WatcherThread();
				watcherThread.start();
			}
			
			inQueueFlag = true;
			got++;
			Display.getDefault().asyncExec(rnbl);		
		//}
	}

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
		lockedLines = new TextPortion(event.fOffset, event.fLength+event.fText.length());
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
			logger.info("Processing input at "+lock.toString());
			this.outer=outer;
			this.event=event;
			this.lock=lock;
		}
		
		public void run(){
			//if (editors.get(activeEditor).requestLock(lock).get()==false) {
			lock = locker.requestLock(activeEditor, editors.get(activeEditor), lock);
			if (lock==null){
				ignoreEvent=event.fModificationStamp;
				logger.info("Lock failed for: "+event);
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
				editors.get(activeEditor).sendChanges(new TextModification(event.fOffset, event.fLength, event.fText, userName));
				//editors.get(activeEditor).releaseLock(lock);
				locker.releaseLock(editors.get(activeEditor), lock);
				lockedLines=null;
			}
				
		
		}
	}
	
	/**
	 * Class for displaying the changes of a document
	 * @author Izso
	 *
	 */
	class DocumentUpdater implements Runnable {
		private ICoedObject file;
		private IDocument doc;
		private IDocumentListener outer;
		
		public DocumentUpdater(ICoedObject file, IDocument doc, IDocumentListener outer) {
			this.file=file;
			this.doc=doc;
			 this.outer=outer;
		}
		
		public void showChanges(ICoedObject file, IDocument doc) throws BadLocationException, NotConnectedException {
			IFuture<TextModification[]> modsF;
			TextModification[] mods = null;
			try {
				modsF = file.getChanges();
				mods = modsF.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (mods!=null && mods.length > 0) {
				doc.removeDocumentListener(outer);
				for (int i = 0; i < mods.length; i++) {
					doc.replace(mods[i].getOffset(), mods[i].getLength(), mods[i].getText());
				}
				doc.addDocumentListener(outer);
			}
		}

		@Override
		public void run() {
			try {
				showChanges(file, doc);
			} catch (NotConnectedException e) {
				e.printStackTrace();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			inQueueFlag = false;
			shown++;
		}
	}

	@Override
	public void collabStateChanged(String to) {
		// TODO Auto-generated method stub
		
	}
}
