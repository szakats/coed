package coed.collab.protocol;

import coed.base.data.TextModification;


/**
 * This message is sent:
 * - by the client when some changes have been made
 * - by the server as a reply to the GetChangesMsg
 */
public class SendChangesMsg extends CoedMessage {
	private Integer Id;
	private TextModification[] mods;
	
	public SendChangesMsg(Integer Id, TextModification[] mods) {
		this.setId(Id);
		this.setMods(mods);
	}

	/**
	 * @param file the file to set
	 */
	public void setId(Integer Id) {
		this.Id = Id;
	}

	/**
	 * @return the file
	 */
	public Integer getId() {
		return Id;
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
