package coed.test.collab.client;

import coed.base.comm.ICoedCollaborator;
import coed.base.comm.ICollabStateObserver;
import coed.base.data.CoedObject;
import coed.collab.client.CollaboratorClient;
import coed.collab.connection.ICoedConnection;
import coed.collab.protocol.SendChangesMsg;
import coed.versioning.client.StaticVersioner;

public class ClientMain {
	private CollaboratorClient collab;
	
	class CollabListenerTest implements ICollabStateObserver {
		@Override
		public void collabStateChanged(String to) {
			System.out.println("If i heard correctly, the connection state is: " + to);
			if(to == ICoedCollaborator.STATUS_CONNECTED) {
				ICoedConnection conn = collab.getConn();
				assert conn != null;
				conn.send(new SendChangesMsg(null, null));
			}
		}
	}
	
	public ClientMain() {
		collab = new CollaboratorClient(null, "");
		StaticVersioner vers = new StaticVersioner();
		
		CoedObject ret = new CoedObject("sadf", true);
		ret.init(vers.makeVersionedObject(ret), collab.makeCollabObject(ret));
		
		collab.addStateListener(new CollabListenerTest());
		
		ret.goOnline("asfd");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClientMain main = new ClientMain();
	}

}
