package coed.collab.protocol;

/**
 * This message is sent:
 * - by the client when it wants the changes made to a particular file
 * @author szakats
 */
public class GetChangesMsg extends CoedMessage {
	private String fileName;

	public GetChangesMsg(String fileName) {
		super();
		this.fileName = fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
}
