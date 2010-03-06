/**
 * 
 */
package coed.plugin.views.ui;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import coed.plugin.views.model.IModelListener;
import coed.plugin.views.model.ModelDirectory;
import coed.plugin.views.model.ModelObject;

/**
 * @author neobi008
 *
 */
public class SessionViewContentProvider implements ITreeContentProvider, IModelListener {
	private TreeViewer viewer;
	
	@Override
	public Object[] getChildren(Object parent) {
		if(parent instanceof ModelDirectory) {
			return ((ModelDirectory)parent).getChildren().toArray();
		} else
			return null;
	}

	@Override
	public Object getParent(Object element) {
		if(element instanceof ModelObject)
			return ((ModelObject)element).getParent();
		else
			return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof ModelDirectory) {
			return !((ModelDirectory)element).getChildren().isEmpty();
		} else
			return false;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer)viewer;
		if(oldInput instanceof ModelObject && newInput instanceof ModelObject) {
			setListenerTo(((ModelObject)oldInput), null);
			setListenerTo(((ModelObject)newInput), this);
		}
	}
	
	private void setListenerTo(ModelObject obj, IModelListener listener) {
		if(obj instanceof ModelDirectory)
			for(ModelObject c : ((ModelDirectory)obj).getChildren()) 
				setListenerTo(c, listener);
		else
			obj.setListener(listener);
	}

	@Override
	public void changed(ModelObject changedObject) {
		viewer.refresh(changedObject, false);
	}

}
