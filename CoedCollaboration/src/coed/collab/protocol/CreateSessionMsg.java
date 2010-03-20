package coed.collab.protocol;

/**
 * This message is sent:
 * - by the client when it want a particular file to be made online
 * @author szakats
 */
public class CreateSessionMsg extends CoedMessage {
	private String fileName;
	private String contents;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public CreateSessionMsg(String fileName, String contents) {
		super();
		this.fileName = fileName;
		this.contents = contents;
	}
}
