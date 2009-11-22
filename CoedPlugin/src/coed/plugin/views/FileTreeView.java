package coed.plugin.views;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;

import coed.base.data.ICoedObject;
import coed.plugin.base.Activator;
import coed.plugin.mocksfordebug.MockCoedObject;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class FileTreeView extends ViewPart implements IFileTree{

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "coed.plugin.views.Files";

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class TreeObject implements IAdaptable {
		private String name;
		private TreeParent parent;
		
		public TreeObject(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setParent(TreeParent parent) {
			this.parent = parent;
		}
		public TreeParent getParent() {
			return parent;
		}
		public String toString() {
			return getName();
		}
		public Object getAdapter(Class key) {
			return null;
		}
	}
	
	class TreeParent extends TreeObject {
		private ArrayList children;
		public TreeParent(String name) {
			super(name);
			children = new ArrayList();
		}
		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}
		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}
		public TreeObject [] getChildren() {
			return (TreeObject [])children.toArray(new TreeObject[children.size()]);
		}
		public boolean hasChildren() {
			return children.size()>0;
		}
	}

	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {
		private TreeParent invisibleRoot;

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				//if (invisibleRoot==null) initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}
		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject)child).getParent();
			}
			return null;
		}
		public Object [] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent)parent).getChildren();
			}
			return new Object[0];
		}
		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent)parent).hasChildren();
			return false;
		}
		
		/**
		 * Method that converts a string containing the path of the file
		 * into an array of strings (the folders and the file)
		 * @param file - the path of the file
		 * @return array of strings containing folders and the file
		 */
		private String[] processString(String file) {
			String[] result=file.split("\\/");
			return result;
		}
		
		/**
		 * Method used in the initialize method
		 * It checks to see if a root in the tree contains a given parent
		 * Method helps when a directory already exists, and doesn't need to be created
		 * @param root - the parent in which we are searching
		 * @param parent - the object that is searched
		 * @return
		 */
		private TreeObject getParentTree(TreeParent root, TreeObject parent){
			if(root==null) return null;
			TreeObject[] all=root.getChildren();
			for(int i=0;i<all.length;i++)
				if(all[i].getName().equals(parent.getName())) 
					return all[i];
			return null;
		}
		
		/**
		 * Method that does double-sorting
		 * 1. Sorts lexicographically (folders and files in alphabetical order)
		 * 2. Sorts according to length(this way folders will precede files)
		 * @param string - array containing all paths
		 * @return - sorted paths
		 */
		private String[] sort(String[] string) {
			String[] sorted = string;
			int ok,i;
			String aux;
			do {
				ok=1;
				for(i=0;i<sorted.length-1;i++)
					if(sorted[i].compareTo(sorted[i+1])>0){
						ok=0;
						aux=sorted[i];
						sorted[i]=sorted[i+1];
						sorted[i+1]=aux;
					}	
			}while(ok==0);
			do {
				ok=1;
				for(i=0;i<sorted.length-1;i++)
					if(sorted[i].length()<sorted[i+1].length()) {
						ok=0;
						aux=sorted[i];
						sorted[i]=sorted[i+1];
						sorted[i+1]=aux;
					}
			}while(ok==0);
			return sorted;
		}
		/**
		 * Function that processes the array of files and creates the tree structure
		 * @param files - array containing all files
		 */
		private void initialize(ICoedObject[] files) {
			int nrOfFiles=files.length;
			String[] temp=null;
			String[] paths= new String[files.length];
			String[] sortedpaths=null;
			int j;
			for(j=0;j<nrOfFiles;j++)
				paths[j]=files[j].getPath();
			sortedpaths=sort(paths);
			TreeObject parent,file;
			TreeParent root,realParent,realRoot;
			invisibleRoot = new TreeParent("");
			for(int i=0;i<nrOfFiles;i++) {
				temp=processString(sortedpaths[i]);
				file = new TreeObject(temp[temp.length-1]);
				
				if(temp.length==1) 
					//TODO: exception ?!
					temp=null;
				else {
					root = new TreeParent(temp[0]);
					realRoot=(TreeParent)getParentTree(invisibleRoot,root);
					if(realRoot==null){
						invisibleRoot.addChild(root);
						for(j=1;j<(temp.length-1);j++) {
							parent = new TreeParent(temp[j]);
							root.addChild(parent);
							root=(TreeParent)parent;
						}
						root.addChild(file);
					}
					else{
						for(j=1;j<(temp.length-1);j++) {
							parent = new TreeParent(temp[j]);
							realParent = (TreeParent)getParentTree(realRoot,parent);
							if(realParent==null){
								realRoot.addChild(parent);
								root=(TreeParent)parent;
								for(int k=j+1;k<(temp.length-1);k++){
									parent = new TreeParent(temp[j]);
									root.addChild(parent);
									root=(TreeParent)parent;
								}
								root.addChild(file);
								break;
							}
							else realRoot=realParent;
						}
						if(j>=temp.length-1) realRoot.addChild(file);
					}				
				}
			}
		}
	}
	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return obj.toString();
		}
		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if (obj instanceof TreeParent)
			   imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	@SuppressWarnings("static-access")
	public FileTreeView() {
		Activator.getDefault().getController().attachFileTree(this);
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		//viewer.setInput(getViewSite());
		
		MockCoedObject obj1 = new MockCoedObject("Base/src/coed/common/ICoedCollaborator.java");
		MockCoedObject obj2 = new MockCoedObject("Base/src/coed/common/ICoedComm.java");
		MockCoedObject obj3 = new MockCoedObject("Base/src/coed/sex/Pina.java");
		MockCoedObject obj4 = new MockCoedObject("Base/src/coed/Lofasz.java");
		MockCoedObject obj5 = new MockCoedObject("Coed/src/coed/common/Slut.java");
		MockCoedObject[] objects={obj1,obj2,obj3,obj4,obj5};
		displayFileTree(objects);
		
		

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "CoedEditor.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	/**
	 * Method used to display the File Tree in a view
	 * @param files - ICoedObject array containing the files that will be displayed
	 */
	public void displayFileTree(ICoedObject[] files) {
		ViewContentProvider v = new ViewContentProvider();
		v.initialize(files);
		viewer.setContentProvider(v);
		viewer.setInput(getViewSite());
	}
	
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				FileTreeView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				if (window != null) {
				    IWorkbenchPage page = window.getActivePage();
				    if (page != null) {
				        IEditorPart editor = page.getActiveEditor();
				        try {
				        	AbstractDecoratedTextEditor textEditor = (AbstractDecoratedTextEditor) editor;
				        	Activator.getController().startCollabFor(textEditor);
				        } catch (Exception e) {
				        	e.printStackTrace();
				        }
				    }
				}
			}
		};
		action1.setText("Share/Join");
		action1.setToolTipText("Share this document for editing with co-workers");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		
		action2 = new Action() {
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				if (window != null) {
				    IWorkbenchPage page = window.getActivePage();
				    if (page != null) {
				        IEditorPart editor = page.getActiveEditor();
				        try {
				        	AbstractDecoratedTextEditor textEditor = (AbstractDecoratedTextEditor) editor;
				        	Activator.getController().endCollabFor(textEditor);
				        } catch (Exception e) {
				        	e.printStackTrace();
				        }
				    }
				}
			}
		};
		action2.setText("Stop collaboration");
		action2.setToolTipText("Stop collaborative editing session on this document");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		doubleClickAction = new Action() {
			public void run() {
				//TODO: check if is folder, decide what to do
				ISelection selection = viewer.getSelection();
				TreePath obj = ((ITreeSelection)selection).getPaths()[0];
				String p = "";
				while (obj.getLastSegment()!=null) {
					p=obj.getLastSegment().toString()+"/"+p;
					obj = obj.getParentPath();
				}
				p=(p.subSequence(0, p.length()-1)).toString();
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(p));
				IEditorDescriptor desc = PlatformUI.getWorkbench().
				        getEditorRegistry().getDefaultEditor(file.getName());
				try {
					page.openEditor(new FileEditorInput(file), desc.getId());
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Coed View",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}