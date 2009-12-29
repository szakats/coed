package coed.collab.protocol;

import coed.base.data.TextModification;

/**
 * This message is sent:
 * - by the server as a reply to the GetChangesMsg
 * @author szakats
 */
public class GetChangesReplyMsg extends CoedMessage {
	private TextModification mods[];
	
	public GetChangesReplyMsg(TextModification[] mods) {
		this.mods = mods;
	}

	/**
	 * @param mods the mods to set
	 */
	public void setMods(TextModification mods[]) {
		this.mods = mods;
	}

	/**
	 * @return the mods
	 */
	public TextModification[] getMods() {
		return mods;
	}
}
