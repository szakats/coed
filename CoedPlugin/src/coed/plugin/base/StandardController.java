package coed.plugin.base;

import java.util.HashMap;

import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import coed.base.common.ICoedCommunicator;
import coed.base.data.CoedFile;
import coed.base.data.CoedProject;
import coed.plugin.views.IFileTree;
import coed.plugin.views.IUserList;


public class StandardController implements IPluginController {
	private ICoedCommunicator communicator;
	private HashMap<AbstractDecoratedTextEditor,CoedFile> editors;
	private AbstractDecoratedTextEditor activeEditor;
	private IDocument activeDocument;
	private IFileTree fileTree;
	private IUserList userList;
	
	public StandardController(){
		//TODO: ask an ICoedCommunicator-factory to give us an instance
		this.communicator = null;
		
		this.editors = new HashMap<AbstractDecoratedTextEditor, CoedFile>();
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
	
	private CoedFile findCoedFileFor(AbstractDecoratedTextEditor texte){
		return null;
	}
	
	private void setAsActive(AbstractDecoratedTextEditor texte){
		activeEditor=texte;
		activeDocument=activeEditor.getDocumentProvider().getDocument(activeEditor.getEditorInput());
		//TODO: should update based on changes
		//TODO: need to listen for user input 
	}
	
	@Override
	public void startCollabFor(AbstractDecoratedTextEditor texte) {
		editors.put(texte, findCoedFileFor(texte));
		//TODO: set as active ?
		setAsActive(texte);
		/*texte.getDocumentProvider().
			getAnnotationModel(activeEditor.getEditorInput()).
			addAnnotation(new Annotation("org.eclipse.ui.workbench.texteditor.spelling", true, "xxx"), new Position(100, 200));*/
		//TODO: listen to it, to see if it is focused, then make it current
		
	}

	@Override
	public void endCollabFor(AbstractDecoratedTextEditor texte) {
		//TODO: maybe do some stuff before closing
		//TODO: restore contents if needed
		if (this.activeEditor.equals(texte)) {
			this.activeDocument=null;
			this.activeEditor=null;
		}
		editors.remove(texte);
		//TODO: stop listening for focus changes&co.
	}

	@Override
	public String[] getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getCollabUsers(CoedFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CoedFile requestInfo(CoedFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CoedProject requestInfo(CoedProject project) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean requestVersionAction(String action, CoedFile file) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean requestVersionAction(String action, CoedProject project) {
		// TODO Auto-generated method stub
		return false;
	}



}
