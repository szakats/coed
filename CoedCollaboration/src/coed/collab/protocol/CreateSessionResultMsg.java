package coed.collab.protocol;

/**
 * This message is sent:
 * - by the server as a reply to the GoOnlineMsg
 * @author szakats
 */
public class CreateSessionResultMsg extends CoedMessage {
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CreateSessionResultMsg(Integer id) {
		super();
		this.id = id;
	}
}
