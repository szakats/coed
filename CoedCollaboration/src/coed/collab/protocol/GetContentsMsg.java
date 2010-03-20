package coed.collab.protocol;

/**
 * This message is sent:
 * - by the client when it wishes to get the contents of a particular file
 * @author szakats
 */
public class GetContentsMsg extends CoedMessage {
	private Integer id;

	/**
	 * @param fileName the fileName to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	public GetContentsMsg(Integer id) {
		super();
		this.id = id;
	}

	/**
	 * @return the fileName
	 */
	public Integer getId() {
		return id;
	}
	
}
