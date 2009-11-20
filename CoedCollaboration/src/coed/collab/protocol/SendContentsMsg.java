package coed.collab.protocol;

/**
 * This message is sent:
 * - by the client as a reply to GoOnlineResultMsg if the file is not already online.
 * - by the server as a reply to GetContentsMsg
 * (in both cases the previous message specifies the fileName)
 * @author szakats
 */
public class SendContentsMsg extends CoedMessage {
	private String contents;

	public SendContentsMsg(String contents) {
		super();
		this.contents = contents;
	}

	/**
	 * @param contents the contents to set
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * @return the contents
	 */
	public String getContents() {
		return contents;
	}
}
