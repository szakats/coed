package coed.test.collab.client;

import coed.base.common.CoedObject;
import coed.collab.client.CollaboratorClient;
import coed.collab.client.ServerConnection;
import coed.collab.protocol.SendChangesMsg;
import coed.versioning.client.StaticVersioner;

public class ClientMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CollaboratorClient collab = new CollaboratorClient(null);
		StaticVersioner vers = new StaticVersioner();
		
		CoedObject ret = new CoedObject("sadf", true);
		ret.init(vers.makeVersionedObject(ret), collab.makeCollabObject(ret));
		
		ret.goOnline();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		
		ServerConnection conn = collab.getConn();
	
		conn.send(new SendChangesMsg(null, null));
	}

}
