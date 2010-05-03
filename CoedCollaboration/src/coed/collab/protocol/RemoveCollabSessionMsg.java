/**
 * 
 */
package coed.collab.protocol;

/**
 * @author neobi008
 *
 */
public class RemoveCollabSessionMsg extends CoedMessage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String path;
	
	public RemoveCollabSessionMsg(Integer id, String path){
		this.id = id;
		this.path = path;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}

