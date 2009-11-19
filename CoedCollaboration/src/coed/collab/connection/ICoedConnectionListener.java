package coed.collab.connection;

import coed.collab.protocol.CoedMessage;

public interface ICoedConnectionListener {
	void received(CoedMessage msg);
	void connected();
	void disconnected();
}
