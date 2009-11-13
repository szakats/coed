package coed.collab.protocol;

import coed.base.data.CoedFile;
import coed.base.data.CoedFileLine;

public class SendChangesMsg extends CoedMessage {
	private CoedFile file;
	private CoedFileLine line;
	
	public SendChangesMsg(CoedFile file, CoedFileLine line) {
		this.file = file;
		this.line = line;
	}
}
