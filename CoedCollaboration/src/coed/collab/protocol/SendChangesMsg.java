package coed.collab.protocol;

import coed.base.data.CoedFileLine;

public class SendChangesMsg extends CoedMessage {
	private String file;
	private CoedFileLine line;
	
	public SendChangesMsg(String file, CoedFileLine line) {
		this.setFile(file);
		this.setLine(line);
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

	/**
	 * @param line the line to set
	 */
	public void setLine(CoedFileLine line) {
		this.line = line;
	}

	/**
	 * @return the line
	 */
	public CoedFileLine getLine() {
		return line;
	}
}
