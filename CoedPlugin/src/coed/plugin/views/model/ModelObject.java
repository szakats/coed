/**
 * 
 */
package coed.plugin.views.model;

/**
 * @author neobi008
 *
 */
public class ModelObject {
	
	private ModelDirectory parent;
	private String path, name;
	protected IModelListener listener;
	
	public ModelObject(ModelDirectory parent, String path){
		this.parent = parent;
		this.path = path;
		String[] names = path.split("\\/");
		this.name = names[names.length - 1];
	}

	public ModelDirectory getParent() {
		return parent;
	}

	public void setParent(ModelDirectory parent) {
		this.parent = parent;
	}

	public String getPath() {
		return path;
	}
	
	public String getName() {
		return name;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public void setListener(IModelListener listener) {
		this.listener = listener;
	}
}
