package coed.test.collab.client;

import java.util.concurrent.ExecutionException;

import coed.base.comm.ICoedCollaborator;
import coed.base.comm.ICollabStateListener;
import coed.base.config.Config;
import coed.base.config.ICoedConfig;
import coed.base.data.CoedObject;
import coed.base.data.ICoedObject;
import coed.base.data.IFileChangeListener;
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


	class Client extends Thread implements ICollabStateListener {
		protected CollaboratorClient collab;
		protected CoedObject obj;
		protected ICoedConfig config = new Config();
		
		Client() {
			config.setString("server.host", "localhost");
			config.setInt("server.port", 1234);
		}
		
		public synchronized void connect() {
			collab = new CollaboratorClient(config, "");
			StaticVersioner vers = new StaticVersioner();
			
			obj = new CoedObject("file", true);
			obj.init(vers.makeVersionedObject(obj), collab.makeCollabObject(obj));
			
			collab.addStateListener(this);
			
			collab.startCollab();

			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public synchronized void collabStateChanged(String to) {
			System.out.println("If i heard correctly, the connection state is: " + to);
			if(to == ICoedCollaborator.STATUS_CONNECTED) {
				ICoedConnection conn = collab.getConn();
				assert conn != null;

				this.notify();
			}
		}
	}
	
	class Client1 extends Client {
		Client1() {
			config.setString("user.name", "client1");
		}

		@Override
		public synchronized void run() {
			connect();
			try {
				String contents = obj.goOnline("contents").get();
				System.out.println("client1 contents: " + contents);
				Thread.sleep(6000);
				obj.sendChanges(new TextModification(1,3,"333","client1"));
				System.out.println("client1 sent changes");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class Client2 extends Client implements IFileChangeListener {
		Client2() {
			config.setString("user.name", "client2");
		}

		@Override
		public synchronized void run() {
			connect();
			try {
				Thread.sleep(3000);
				String contents = obj.goOnline("contents").get();
				System.out.println("client2 contents: " + contents);
				obj.addChangeListener(this);
				wait();	// wait for changes
				TextModification[] mods = obj.getChanges().get();
				System.out.println("client2 got modifications:");
				for(TextModification mod : mods)
					System.out.println(mod.toString());
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public synchronized void hasChanges(ICoedObject file) {
			notify();
		}
	}

	public ClientMain() {
		Client1 c1 = new Client1();
		Client2 c2 = new Client2();
		c1.start();
		c2.start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClientMain main = new ClientMain();
	}

}
