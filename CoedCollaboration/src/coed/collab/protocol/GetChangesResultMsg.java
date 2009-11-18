package coed.collab.protocol;

import coed.base.data.TextModification;

public class GetChangesResultMsg extends CoedMessage {
	//TODO: fix this class. to work with TextPortion and TextModification
	private TextModification mods[];
	
	public GetChangesResultMsg(TextModification[] mods) {
		this.setMods(mods);
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
