package coed.collab.protocol;

import coed.base.data.TextModification;


/**
 * ADD CLASS DESCRIPTION
 */
public class SendChangesMsg extends CoedMessage {
	//TODO: fix this class. to work with TextPortion and TextModification
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
