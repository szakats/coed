package coed.collab.protocol;

/**
 * This message is sent:
 * - by the client when it wishes to get the contents of a particular file
 * @author szakats
 */
public class GetContentsMsg {
	private String fileName;

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public GetContentsMsg(String fileName) {
		super();
		this.fileName = fileName;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	
}
