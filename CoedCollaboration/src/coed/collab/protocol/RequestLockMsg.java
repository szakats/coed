package coed.collab.protocol;

import coed.base.data.TextPortion;

public class RequestLockMsg extends CoedMessage{
	private String file;
	private TextPortion portion;
	
	public RequestLockMsg(String file, TextPortion portion) {
		this.file = file;
		this.portion = portion;
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
	
	public void setPortion(TextPortion portion){
		this.portion = portion;
	}
	
	public TextPortion getPortion(){
		return this.portion;
	}
}
