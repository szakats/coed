package coed.collab.protocol;

/**
 * This message is sent:
 * - by the client when it want a particular file to be made online
 * @author szakats
 */
public class GoOnlineMsg extends CoedMessage {
	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public GoOnlineMsg(String fileName) {
		super();
		this.fileName = fileName;
	}
}
