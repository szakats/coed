package coed.test.collab.client;

import java.util.concurrent.ExecutionException;

import coed.base.comm.ICoedCollaborator;
import coed.base.comm.ICollabStateListener;
import coed.base.data.CoedObject;
import coed.base.data.exceptions.NotConnectedException;
import coed.base.util.IFutureListener;
import coed.collab.client.CollaboratorClient;
import coed.collab.connection.ICoedConnection;
import coed.collab.protocol.GoOnlineMsg;
import coed.collab.protocol.SendChangesMsg;
import coed.versioning.client.StaticVersioner;

public class ClientMain {
	private CollaboratorClient collab;
	private CoedObject ret;
	
	class CollabListenerTest implements ICollabStateListener, IFutureListener<String> {
		@Override
		public void collabStateChanged(String to) {
			System.out.println("If i heard correctly, the connection state is: " + to);
			if(to == ICoedCollaborator.STATUS_CONNECTED) {
				//ICoedConnection conn = collab.getConn();
				//assert conn != null;

				ret.goOnline("asdf").addListener(this);
			}
		}

		@Override
		public void got(String result) {
			System.out.println("received contents: " + result);
		}

		@Override
		public void caught(Throwable e) {
			e.printStackTrace();
		}
	}
	
	public ClientMain() {
		collab = new CollaboratorClient(null, "");
		StaticVersioner vers = new StaticVersioner();
		
		ret = new CoedObject("sadf", true);
		ret.init(vers.makeVersionedObject(ret), collab.makeCollabObject(ret));
		
		collab.addStateListener(new CollabListenerTest());
		
		collab.startCollab();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClientMain main = new ClientMain();
	}

}
