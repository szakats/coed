/**
 * 
 */
package coed.plugin.views.ui;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Composite;

import coed.plugin.base.Activator;
import coed.plugin.views.model.ModelDirectory;
import coed.plugin.views.model.ModelFile;
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
		root =  new ModelDirectory(null, "");
	}
	
	@Override
	public void createOtherControls() {
		addCreateSessionAction();
		addLeaveSessionAction();
		addLoginAction();
		
		/*addFile("asdf", "yt");
		addFile("asdf1", "");
		addFile("a324sdf", "");
		addFile("xyz/tuv", "");
		addFile("xyz/yut", "");
		addFile("xyz/tuv/hjh", "asdf");
		addFile("xyz/hgh/thd", "fjf");*/
	}
	
	public void clearModel(){
		assert root instanceof ModelDirectory;
		((ModelDirectory)root).clearChildren();
	}

	@Override
	public Object getModelRoot() {
		// TODO Auto-generated method stub
		
		return root;
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {
		// TODO Auto-generated method stub
		ISelection sel = event.getSelection();
		if(sel instanceof TreeSelection) {
			Object elem = ((TreeSelection)sel).getFirstElement();
			if(elem instanceof ModelFile) {
				ModelFile mf = ((ModelFile)elem);
				Activator.getController().joinSession(mf.getPath(), Integer.parseInt(mf.getMeta()));
			}
		}
	}
}
