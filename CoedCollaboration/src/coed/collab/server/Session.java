package coed.collab.server;

import org.apache.mina.common.IoSession;

import coed.collab.protocol.CoedMessage;

public class Session {
	private IoSession io;
	
	public Session(IoSession io) {
		this.io = io;
	}
	
	public void write(CoedMessage msg) {
		io.write(msg);
	}
}
