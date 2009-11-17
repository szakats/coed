package coed.test.collab.client;

import coed.base.comm.ICollabStateObserver;
import coed.base.data.CoedObject;
import coed.collab.client.CollaboratorClient;
import coed.collab.client.ServerConnection;
import coed.collab.protocol.SendChangesMsg;
import coed.versioning.client.StaticVersioner;

public class ClientMain {
	
	class CollabListenerTest implements ICollabStateObserver {
		@Override
		public void collabStateChanged(String to) {
			System.out.println("If i heard correctly, the connection state is: " + to);
		}
	}
	
	public ClientMain() {
		CollaboratorClient collab = new CollaboratorClient(null, "");
		StaticVersioner vers = new StaticVersioner();
		
		CoedObject ret = new CoedObject("sadf", true);
		ret.init(vers.makeVersionedObject(ret), collab.makeCollabObject(ret));
		
		collab.addStateListener(new CollabListenerTest());
		
		ret.goOnline("asfd");
		
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
