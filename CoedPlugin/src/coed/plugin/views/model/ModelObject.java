/**
 * 
 */
package coed.plugin.views.model;

/**
 * @author neobi008
 *
 */
public class ModelObject {
	private String meta;
	private ModelDirectory parent;
	private String path, name;
	protected IModelListener listener;
	
	public ModelObject(ModelDirectory parent, String path, String meta){
		this.parent = parent;
		this.path = path;
		this.meta = meta;
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
	
	public String getFullPath() {
		if(getParent() == null)
			return "";
		else
			return getParent().getFullPath() + "/" + getPath();
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
	
	public boolean isRoot() {
		return getParent() == null;
	}
	
	public void setMeta(String meta) {
		this.meta = meta;
	}
	
	public String getMeta() {
		return meta == null ? "" : meta;
	}
	
	
	public String toString() {
		if(getMeta() != null && !getMeta().equals(""))
			return getName() + "(" + getMeta() + ")";
		else
			return getName();
	}
}
