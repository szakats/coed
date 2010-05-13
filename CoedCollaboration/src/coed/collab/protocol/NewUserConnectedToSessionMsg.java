/**
 * 
 */
package coed.collab.protocol;

/**
 * @author neobi008
 *
 */
public class NewUserConnectedToSessionMsg {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer sessionId;
	private String user;
	
	public NewUserConnectedToSessionMsg(Integer sessionId, String user){
		this.sessionId = sessionId;
		this.user = user;
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
