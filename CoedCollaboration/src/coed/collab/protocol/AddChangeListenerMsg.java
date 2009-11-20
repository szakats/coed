package coed.collab.protocol;

public class AddChangeListenerMsg extends CoedMessage {
	private String file;

	public AddChangeListenerMsg(String file) {
		this.file = file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}
}
