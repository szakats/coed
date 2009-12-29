package coed.collab.protocol;

public class FileChangedMsg extends CoedMessage {
	private String fileName;

	public FileChangedMsg(String fileName) {
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
