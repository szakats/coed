package coed.plugin.views;

import java.util.Arrays;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;

import coed.plugin.base.Activator;


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

public class UserListView extends ViewPart implements IUserList {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "coed.plugin.views.Users";
	
	private TableViewer viewer;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;
	private ViewContentProvider v;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class ViewContentProvider implements IStructuredContentProvider {
		private String[] users=null;
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public void setElements(String[] users) {
			this.users=users;
		}
		public void addElement(String user){
			if(users==null)
			{
				String[] temp={user};
				setElements(temp);
			}
			else
			{
				boolean ok=false;
				int i;
				String[] temp=new String[users.length+1];
				for(i=0;i<users.length;i++)
				{
					temp[i]=users[i];			
					if(users[i].compareTo(user)==0) ok=true;
				}
				if(!ok){
					temp[users.length]=user;
					Arrays.sort(temp);
					users=temp;
				}
			}
		}
		public boolean removeElement(String user){
			if(users==null)
				return false;
			else {
				boolean ok=false;
				int i;
				String[] temp=new String[users.length-1];
				for(i=0;i<users.length-1;i++)
				{  
					if(users[i].compareTo(user)==0) ok=true;
					if(ok==false)
						temp[i]=users[i];
					else temp[i]=users[i+1];
				}
				if(ok==true) users=temp;
			}
			return true;
		}
		public Object[] getElements(Object parent) {
			return users;
		}
		/**
		 * Method used for testing!
		 * Remove when program finished!
		 * @return
		 */
		public String[] getUsers(){
			return users;
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	@SuppressWarnings("static-access")
	public UserListView() {
		//Activator.getDefault().getController().attachUserListView(this);
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		v=new ViewContentProvider();
		viewer.setContentProvider(v);
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		
		
		
		//initializations users for test
		/*
		String[] x= {"a"};
		displayUsers(x);
		String y="b";
		displayUser(y);
		y="q";
		displayUser(y);
		y="b";
		removeUser(y);*/
		
		
		
		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "coed.plugin.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}
	
	/**
	 * Method for displaying the user list in the view.
	 * Must be called whenever the list of users changes
	 * @param users - array of strings containing the users
	 */
	public void displayUsers(String[] users) {
		v.setElements(users);
		viewer.setContentProvider(v);
		viewer.setInput(getViewSite());
	}
	
	public void displayUser(String user){
		v.addElement(user);
		viewer.setContentProvider(v);
		viewer.setInput(getViewSite());
	}
	
	public void removeUser(String user){
		if(v.removeElement(user))
		{
			viewer.setContentProvider(v);
			viewer.setInput(getViewSite());
		}
	}
	
	/**
	 * Method used for testing.
	 * Remove when program is finished!
	 * Getter for user list
	 * @return - user list
	 */
	public String[] getUsers(){
		return v.getUsers();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				UserListView.this.fillContextMenu(manager);
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
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
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
			"Users View",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}

