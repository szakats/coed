package coed.plugin.views.ui;

import org.eclipse.jface.viewers.DoubleClickEvent;

import coed.plugin.base.Activator;
import coed.plugin.views.model.ModelDirectory;
import coed.plugin.views.model.ModelObject;

public class MySessionUsersView extends UsersView {
	
	private ModelObject root;

	/**
	 * 
	 */
	public MySessionUsersView() {
		// TODO Auto-generated constructor stub
		Activator.getController().attachUsersView(this);
		root =  new ModelDirectory(null, "", "");
	}

	@Override
	public Object getModelRoot() {
		return root;
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {
		// TODO Auto-generated method stub
		
	}
}
