package coed.collab.protocol;

import coed.base.data.TextModification;


/**
 * This message is sent:
 * - by the client when some changes have been made
 * - by the server as a reply to the GetChangesMsg
 */
public class SendChangesMsg extends CoedMessage {
	private String file;
	private TextModification[] mods;
	
	public SendChangesMsg(String file, TextModification[] mods) {
		this.setFile(file);
		this.setMods(mods);
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
	 * @param mods the mods to set
	 */
	public void setMods(TextModification[] mods) {
		this.mods = mods;
	}

	/**
	 * @return the mods
	 */
	public TextModification[] getMods() {
		return mods;
	}
}
