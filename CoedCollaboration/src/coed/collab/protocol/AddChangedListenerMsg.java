package coed.collab.protocol;

public class AddChangedListenerMsg extends CoedMessage {
	private String file;

	public AddChangedListenerMsg(String file) {
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
