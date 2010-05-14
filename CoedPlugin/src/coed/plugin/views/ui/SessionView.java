/**
 * 
 */
package coed.plugin.views.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import coed.base.comm.ICoedCollaborator;
import coed.plugin.base.Activator;
import coed.plugin.views.model.ModelDirectory;
import coed.plugin.views.model.ModelFile;
import coed.plugin.views.model.ModelObject;

/**
 * @author neobi008
 *
 */
public abstract class SessionView extends ViewPart implements IDoubleClickListener{
	
	private Text text;
	private TreeViewer treeViewer;
	private SessionViewLabelProvider labelProvider;
	private Action createSessionAction, joinSessionAction, leaveSessionAction, loginAction;
	protected ViewerSorter dirfileSorter;

	@Override
	public void createPartControl(Composite parent) {
		/* Create a grid layout object so the text and treeviewer
		 * are layed out the way I want. */
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 2;
		parent.setLayout(layout);
		
		/* Create a "label" to display information in. I'm
		 * using a text field instead of a lable so you can
		 * copy-paste out of it. */
		text = new Text(parent, SWT.READ_ONLY | SWT.SINGLE | SWT.BORDER);
		// layout the text field above the treeviewer
		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		text.setLayoutData(layoutData);
		
		// Create the tree viewer as a child of the composite parent
		treeViewer = new TreeViewer(parent);
		treeViewer.setContentProvider(new SessionViewContentProvider());
		labelProvider = new SessionViewLabelProvider();
		treeViewer.setLabelProvider(labelProvider);
		
		treeViewer.setUseHashlookup(true);
		
		// layout the tree viewer below the text field
		layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.grabExcessVerticalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.verticalAlignment = GridData.FILL;
		treeViewer.getControl().setLayoutData(layoutData);
		
		createSorters();
		
		createOtherControls();
		
		treeViewer.setInput(getModelRoot());
		treeViewer.expandAll();
		
		treeViewer.addDoubleClickListener(this);
	}
	
	protected AbstractDecoratedTextEditor getEditor() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
		    IWorkbenchPage page = window.getActivePage();
		    if (page != null) {
		        IEditorPart editor = page.getActiveEditor();
		        try {
		        	AbstractDecoratedTextEditor textEditor = (AbstractDecoratedTextEditor) editor;
		        	return textEditor;
		        } catch (Exception e) {
		        	e.printStackTrace();
		        }
		    }
		}
		return null;
	}
	
	public void addCreateSessionAction() {
		createSessionAction = new Action() {
			public void run() {
				AbstractDecoratedTextEditor editor = getEditor();
				if(editor != null)
					Activator.getController().createSession(editor);
			}
		};
		
		createSessionAction.setToolTipText("Create a new collaboration session with the active file");
		createSessionAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		
		getToolBar().add(createSessionAction);
	}
	
	public void addLeaveSessionAction() {
		
		leaveSessionAction = new Action() {
			public void run() {
				AbstractDecoratedTextEditor editor = getEditor();
				if(editor != null)
					Activator.getController().leaveSession(editor);
			}
		};
		
		leaveSessionAction.setToolTipText("Leave the collaboration session for the active file");
		leaveSessionAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		
		getToolBar().add(leaveSessionAction);
	}
	
	public void addLoginAction() {
		
		loginAction = new Action() {
			public void run() {
				String state = Activator.getController().getCollabState();
				if(state == ICoedCollaborator.STATUS_OFFLINE) {
					Activator.getController().loginToServer();
					loginAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
							getImageDescriptor(ISharedImages.IMG_DEF_VIEW));
					loginAction.setToolTipText("Logging in ...");
				} else {
					Activator.getController().logoffFromServer();
					loginAction.setToolTipText("Log in to the collaboration server");
					loginAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
							getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED));
				}
			}
		};
		
		loginAction.setToolTipText("Log in to the collaboration server");
		loginAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED));
		
		getToolBar().add(loginAction);
	}
	
	
	protected IToolBarManager getToolBar() {
		return getViewSite().getActionBars().getToolBarManager();
	}
	
	public abstract void createOtherControls();
	public abstract Object getModelRoot();

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	public void addFile(String path, String meta) {
		String[] names = path.split("\\/");
		recAddFile((ModelDirectory)getModelRoot(), path, names, 0, meta);
	}
	
	public void removeFile(String path, String meta) {
		String[] names = path.split("\\/");
		recRemoveFile((ModelDirectory)getModelRoot(), names, 0, meta);
	}
	
	private void recAddFile(ModelDirectory parent, String fullPath, String[] names, int idx, String meta) {
		if(idx == names.length ) 
			return;
		
		if(idx == names.length - 1)
			parent.addObject(new ModelFile(parent, fullPath, meta));
		else {
			ModelDirectory child = null;
			for(ModelObject c : parent.getChildren())
				if(c instanceof ModelDirectory && c.getName().equals(names[idx])) {
					child = (ModelDirectory)c;
					break;
				}
				
			if(child == null) {
				child = new ModelDirectory(parent, names[idx], "");
				parent.addObject(child);
			}
			
			recAddFile(child, fullPath, names, idx + 1, meta);
		}
	}
	
	private int recRemoveFile(ModelDirectory parent, String[] names, int idx, String meta) {
		if(idx == names.length ) 
			return 0;
		
		if(idx == names.length - 1) {
			for(ModelObject c : parent.getChildren())
				if(c instanceof ModelFile && c.getName().equals(names[idx]) &&
						((ModelFile)c).getMeta().equals(meta)) {
					parent.removeObject(c);
					break;
				}
		} else {
			ModelDirectory child = null;
			for(ModelObject c : parent.getChildren())
				if(c instanceof ModelDirectory && c.getName().equals(names[idx])) {
					child = (ModelDirectory)c;
					break;
				}
				
			if(child == null)
				return -1;
			
			int ret = recRemoveFile(child, names, idx + 1, meta);
			if(ret == -1)
				return ret;
			if(ret == 0)
				parent.removeObject(child);
		}
		
		return parent.getChildren().size();
	}
	
	class DirFileSorter extends ViewerSorter {
		public int category(Object element) {
			if(element instanceof ModelDirectory)
				return 1;
			else
				return 2;
		}
	}

	private void createSorters() {
		dirfileSorter = new DirFileSorter();
		treeViewer.setSorter(dirfileSorter);
	}
	
	public void notifyConnected() {
		loginAction.setToolTipText("Log out");
		loginAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED));
	}
	
	public void notifyAuthError() {
		loginAction.setToolTipText("Log in to the collaboration server");
		loginAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED));
	}
	
	public void refreshView() {
		treeViewer.refresh(getModelRoot(), false);
	}
}
