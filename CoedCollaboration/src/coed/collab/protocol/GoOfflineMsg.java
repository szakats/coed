package coed.collab.protocol;

/**
 * This message is sent:
 * - by the client when it want a particular file to be made offline
 * @author szakats
 */
public class GoOfflineMsg extends CoedMessage {
	private Integer Id;

	public GoOfflineMsg(Integer id) {
		super();
		Id = id;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}
}
