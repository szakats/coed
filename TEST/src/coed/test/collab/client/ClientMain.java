package coed.test.collab.client;

import java.util.concurrent.ExecutionException;

import coed.base.comm.ICoedCollaborator;
import coed.base.comm.ICollabStateListener;
import coed.base.data.CoedObject;
import coed.base.data.TextModification;
import coed.base.data.exceptions.NotConnectedException;
import coed.base.util.IFutureListener;
import coed.collab.client.CollaboratorClient;
import coed.collab.connection.ICoedConnection;
import coed.collab.protocol.AuthentificationMsg;
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
				collab.getConn().send(new AuthentificationMsg("user1"));

				new GoOnlineTest();
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
	
	class GoOnlineTest implements IFutureListener<String> {
		
		GoOnlineTest() {
			ret.goOnline("contents").addListener(this);
		}

		@Override
		public void got(String result) {
			System.out.println("received contents: " + result);
			new SendChangesTest();
		}
  
		@Override
		public void caught(Throwable e) {
			e.printStackTrace();
		}
		
	}
	
	class SendChangesTest implements IFutureListener<String> {
		
		SendChangesTest() {
			System.out.println("sending changes");
			TextModification[] mods = new TextModification[1];
			mods[0] = new TextModification(1,3,"333","user1");
			collab.getConn().send(new SendChangesMsg("file",mods));
		}

		@Override
		public void got(String result) {
			System.out.println("sent contents client1 " + result);
		}
  
		@Override
		public void caught(Throwable e) {
			e.printStackTrace();
		}
		
	}
	
	public ClientMain() {
		collab = new CollaboratorClient(null, "");
		StaticVersioner vers = new StaticVersioner();
		
		ret = new CoedObject("file", true);
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
