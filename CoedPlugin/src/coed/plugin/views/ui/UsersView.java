package coed.plugin.views.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

import coed.plugin.base.Activator;
import coed.plugin.views.model.ModelDirectory;
import coed.plugin.views.model.ModelFile;
import coed.plugin.views.model.ModelObject;

/**
 * @author neobi008
 *
 */
public abstract class UsersView extends ViewPart implements IDoubleClickListener{
	
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
		Activator.getController().attachUsersView(this);
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
	
	protected IToolBarManager getToolBar() {
		return getViewSite().getActionBars().getToolBarManager();
	}
	
	public  void createOtherControls()
	{}
	
	public abstract Object getModelRoot();

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	public void addUserToSession(Integer id, String user) {
		ModelDirectory child = null;
		for(ModelObject c : ((ModelDirectory)getModelRoot()).getChildren()) {
			if(!(c instanceof ModelDirectory))
				continue;
			Integer childId = Integer.parseInt(((ModelDirectory)c).getMeta());
			if(childId.equals(id)) {
				child = (ModelDirectory)c;
				child.addObject(new ModelFile(child,user,""));
				break;
			}
		}
	}
	
	public void removeUserFromSession(Integer id, String user) {
		for(ModelObject c : ((ModelDirectory)getModelRoot()).getChildren()){
			if(!(c instanceof ModelDirectory))
				continue;
			Integer childId = Integer.parseInt(((ModelDirectory)c).getMeta());
			if(childId.equals(id)) {
				for(ModelObject obj : ((ModelDirectory)c).getChildren()){
					if (obj.getName().equals(user)){
						((ModelDirectory)c).removeObject(obj);
						break;
					}
				}
			}
		}
	}
	
	public void clearModel(){
		Object root = getModelRoot();
		assert root instanceof ModelDirectory;
		((ModelDirectory)root).clearChildren();
	}
	
	public void removeSession(Integer id) {
		for(ModelObject c : ((ModelDirectory)getModelRoot()).getChildren()){
			if(!(c instanceof ModelDirectory))
				continue;
			Integer childId = Integer.parseInt(((ModelDirectory)c).getMeta());
			if(childId.equals(id)) {
				((ModelDirectory)getModelRoot()).removeObject(c);
				break;
			}
		}
	}
	
	public void addSession(String session, Integer id){
		ModelDirectory dir = new ModelDirectory((ModelDirectory)getModelRoot(),session, id.toString());
		((ModelDirectory)getModelRoot()).addObject(dir);
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
	
	public void refreshView() {
		treeViewer.refresh(getModelRoot(), false);
	}

}
