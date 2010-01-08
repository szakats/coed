package coed.test.collab.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;

import coed.base.util.IFutureListener;
import coed.collab.connection.CoedConnection;
import coed.collab.connection.CoedConnectionAcceptor;
import coed.collab.connection.ICoedConnectionListener;
import coed.collab.protocol.CoedMessage;
import coed.collab.protocol.SendContentsMsg;

public class ConnectionTest {
	
	@Before public void setUp() {
		
	}
	
	class TestSession implements ICoedConnectionListener {
		public CoedConnection conn;
		boolean gotConnected = false;
		boolean gotDisconnected = false;
		boolean gotMsg = false;
		@Override
		public void connected() {
			gotConnected = true;
		}

		@Override
		public void disconnected() {
			gotDisconnected = true;
		}

		@Override
		public void received(CoedMessage msg) {
			gotMsg = true;
			conn.reply(msg, new SendContentsMsg(null));
		}
	}
	
	TestSession ses;
	CoedConnection remoteConn;
	
	@Test public void testAll() {
		class TestAcceptor extends CoedConnectionAcceptor {
			
		}
		
		class TestAccListener implements CoedConnectionAcceptor.Listener {
			boolean gotConnected = false;
			@Override
			public void connected(CoedConnection conn) {
				gotConnected = true;
				remoteConn = conn;
				ses = new TestSession();
				ses.conn = conn;
				conn.addListener(ses);
			}
		}
		
		class TestConnListener implements ICoedConnectionListener {
			boolean gotConnected = false;
			boolean gotDisconnected = false;

			@Override
			public void connected() {
				gotConnected = true;
			}

			@Override
			public void disconnected() {
				gotDisconnected = true;
			}

			@Override
			public void received(CoedMessage msg) {
				// TODO Auto-generated method stub
				
			}
		}
		
		TestAcceptor acc = new TestAcceptor();
		acc.listen(1234);
		TestAccListener acli = new TestAccListener();
		acc.addListener(acli);
		TestConnListener lis = new TestConnListener();
		
		ses = null;
		CoedConnection conn = null;
		try {
			conn = CoedConnection.connect("localhost", 1234).get();
			assertTrue(conn != null);
			conn.addListener(lis);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		assertTrue(conn.isConnected() && ses != null);
		assertTrue(ses.gotConnected && lis.gotConnected);
		
		class TestMsgListener implements IFutureListener<CoedMessage> {
			boolean gotMsg = false;
			@Override
			public void got(CoedMessage result) {
				gotMsg = true;
			}

			@Override
			public void caught(Throwable e) {
				// TODO Auto-generated method stub
				
			}
			
		}
		
		TestMsgListener msglis = new TestMsgListener();
		
		String contents = "sms";
		conn.sendSeq(new SendContentsMsg(contents)).addListener(msglis);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertTrue(ses.gotMsg == true);
		assertTrue(msglis.gotMsg == true);
		
		conn.disconnect();
		assertTrue(!conn.isConnected());
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertTrue(!remoteConn.isConnected());
	}
}
