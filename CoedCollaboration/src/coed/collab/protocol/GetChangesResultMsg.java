package coed.collab.protocol;

/**
 * Add class description
 */
public class GetChangesResultMsg extends CoedMessage {
	//TODO: fix this class. to work with TextPortion and TextModification
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
