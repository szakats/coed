package coed.collab.protocol;

import coed.base.data.CoedFileLine;

public class GetChangesResultMsg extends CoedMessage {
	private CoedFileLine[] lines;
	
	public GetChangesResultMsg(CoedFileLine[] lines) {
		setLines(lines);
	}

	/**
	 * @param lines the lines to set
	 */
	public void setLines(CoedFileLine[] lines) {
		this.lines = lines;
	}

	/**
	 * @return the lines
	 */
	public CoedFileLine[] getLines() {
		return lines;
	}
}
