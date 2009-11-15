package coed.collab.protocol;

import coed.base.data.CoedFileLine;

public class SendChangesMsg extends CoedMessage {
	private String file;
	private CoedFileLine line;
	
	public SendChangesMsg(String file, CoedFileLine line) {
		this.file = file;
		this.line = line;
	}
}
