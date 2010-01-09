package coed.collab.protocol;

/**
 * This message is sent:
 * - by the client when it want a particular file to be made offline
 * @author szakats
 */
public class GoOfflineMsg extends CoedMessage {
	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public GoOfflineMsg(String fileName) {
		super();
		this.fileName = fileName;
	}
}
