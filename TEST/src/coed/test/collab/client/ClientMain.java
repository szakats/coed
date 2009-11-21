package coed.test.collab.client;

import java.util.concurrent.ExecutionException;

import coed.base.comm.ICoedCollaborator;
import coed.base.comm.ICollabStateListener;
import coed.base.data.CoedObject;
import coed.base.data.exceptions.NotConnectedException;
import coed.collab.client.CollaboratorClient;
import coed.collab.connection.ICoedConnection;
import coed.collab.protocol.SendChangesMsg;
import coed.versioning.client.StaticVersioner;

public class ClientMain {
	private CollaboratorClient collab;
	private CoedObject ret;
	
	class CollabListenerTest implements ICollabStateListener {
		@Override
		public void collabStateChanged(String to) {
			System.out.println("If i heard correctly, the connection state is: " + to);
			if(to == ICoedCollaborator.STATUS_CONNECTED) {
				ICoedConnection conn = collab.getConn();
				assert conn != null;
				try {
					conn.send(new SendChangesMsg(null, null));
				} catch (NotConnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public ClientMain() {
		collab = new CollaboratorClient(null, "");
		StaticVersioner vers = new StaticVersioner();
		
		ret = new CoedObject("sadf", true);
		ret.init(vers.makeVersionedObject(ret), collab.makeCollabObject(ret));
		
		collab.addStateListener(new CollabListenerTest());
		
		try {
			String contents = ret.goOnline("asdf").get();
			System.out.println("received contents: " + contents);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClientMain main = new ClientMain();
	}

}
