/**
 * 
 */
package coed.plugin.views.ui;

import org.eclipse.swt.widgets.Composite;

import coed.plugin.base.Activator;
import coed.plugin.views.model.ModelDirectory;
import coed.plugin.views.model.ModelObject;

/**
 * @author neobi008
 *
 */
public class AllSessionsView extends SessionView {
	
	private ModelObject root;

	/**
	 * 
	 */
	public AllSessionsView() {
		// TODO Auto-generated constructor stub
		Activator.getController().attachView(this);
		root =  new ModelDirectory(null, "asdf");
	}
	
	@Override
	public void createOtherControls() {
		addCreateSessionAction();
		addJoinSessionAction();
		addLeaveSessionAction();
		addLoginAction();
		
		addFile("asdf", "yt");
		addFile("asdf1", "");
		addFile("a324sdf", "");
		addFile("xyz/tuv", "");
		addFile("xyz/yut", "");
		addFile("xyz/tuv/hjh", "asdf");
		addFile("xyz/hgh/thd", "fjf");
	}

	@Override
	public Object getModelRoot() {
		// TODO Auto-generated method stub
		
		return root;
	}

}
