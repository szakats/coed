package coed.collab.protocol;

public class UserSessionListenerMsg extends CoedMessage {
	private Integer sessionId;
	private Boolean add;
	
	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	public Boolean getAdd() {
		return add;
	}

	public void setAdd(Boolean add) {
		this.add = add;
	}

	public UserSessionListenerMsg(Integer sessionId, Boolean add) {
		super();
		this.sessionId = sessionId;
		this.add = add;
	}
	
}
