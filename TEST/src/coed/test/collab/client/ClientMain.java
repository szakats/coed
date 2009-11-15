package coed.test.collab.client;

import coed.base.common.CoedObject;
import coed.base.common.ICollabStateObserver;
import coed.collab.client.CollaboratorClient;
import coed.collab.client.ServerConnection;
import coed.collab.protocol.SendChangesMsg;
import coed.versioning.client.StaticVersioner;

public class ClientMain {
	CollaboratorClient collab;
	
	class CollabListenerTest implements ICollabStateObserver {
		@Override
		public void update() {
			System.out.println("If i heard correctly, the server is now " + collab.getState());
		}
	}
	
	public ClientMain() {
		collab = new CollaboratorClient(null, "");
		StaticVersioner vers = new StaticVersioner();
		
		CoedObject ret = new CoedObject("sadf", true);
		ret.init(vers.makeVersionedObject(ret), collab.makeCollabObject(ret));
		
		collab.addStateListener(new CollabListenerTest());
		
		ret.goOnline();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		
		ServerConnection conn = collab.getConn();
		if(conn == null) return;
	
		conn.send(new SendChangesMsg(null, null));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClientMain main = new ClientMain();
	}

}
