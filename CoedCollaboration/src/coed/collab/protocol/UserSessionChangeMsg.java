/**
 * 
 */
package coed.collab.protocol;

/**
 * @author neobi008
 *
 */
public class UserSessionChangeMsg extends CoedMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer sessionId;
	private String user;
	private Boolean joined;
	
	public Boolean hasJoined() {
		return joined;
	}
	
	public Boolean hasLeft() {
		return !joined;
	}

	public void setJoined(Boolean joined) {
		this.joined = joined;
	}

	public UserSessionChangeMsg(Integer sessionId, String user, Boolean joined) {
		super();
		this.sessionId = sessionId;
		this.user = user;
		this.joined = joined;
	}

	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
