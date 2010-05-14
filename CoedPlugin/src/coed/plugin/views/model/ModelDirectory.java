/**
 * 
 */
package coed.plugin.views.model;

import java.util.LinkedList;
import java.util.List;

/**
 * @author neobi008
 *
 */
public class ModelDirectory extends ModelObject {

	private List<ModelObject> children = new LinkedList<ModelObject>();
	
	public ModelDirectory(ModelDirectory parent, String path, String meta) {
		super(parent, path, meta);
	}
	
	public void addObject(ModelObject object){
		children.add(object);
		object.setParent(this);
		if(listener != null) listener.changed(this);
	}
	
	public void removeObject(ModelObject object){
		children.remove(object);
		object.setParent(null);
		if(listener != null) listener.changed(this);
	}
	
	 public List<ModelObject> getChildren() {
		 return children; 
	 }
	 
	 public void clearChildren() {
		 children.clear();
	 }
	 
	 public String toString() {
		 return getName();
	 }
}
