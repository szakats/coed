package coed.collab.protocol;

/**
 * This message is sent:
 * - by the client when it wants the changes made to a particular file
 * @author szakats
 */
public class GetChangesMsg extends CoedMessage {
	private Integer Id;

	public GetChangesMsg(Integer id) {
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
