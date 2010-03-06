/**
 * 
 */
package coed.plugin.views.model;

/**
 * @author neobi008
 *
 */
public class ModelFile extends ModelObject{
	private String meta;

	public ModelFile(ModelDirectory parent, String path, String meta) {
		super(parent, path);
		this.meta = meta;
	}
	
	public String toString() {
		if(meta != null)
			return getName() + "(" + meta + ")";
		else
			return getName();
	}
	
	public void setMeta(String meta) {
		this.meta = meta;
	}
	
	public String getMeta() {
		return meta == null ? "" : meta;
	}
}
