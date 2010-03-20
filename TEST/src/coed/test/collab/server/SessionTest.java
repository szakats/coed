/**
 * 
 */
package coed.test.collab.server;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import coed.base.data.TextModification;
import coed.base.data.TextPortion;
import coed.base.util.CoedFuture;
import coed.base.util.IFuture;
import coed.collab.connection.ICoedConnection;
import coed.collab.connection.ICoedConnectionListener;
import coed.collab.protocol.AddChangedListenerMsg;
import coed.collab.protocol.AuthentificationMsg;
import coed.collab.protocol.CoedMessage;
import coed.collab.protocol.FileChangedMsg;
import coed.collab.protocol.GetChangesMsg;
import coed.collab.protocol.GetChangesReplyMsg;
import coed.collab.protocol.CreateSessionMsg;
import coed.collab.protocol.CreateSessionResultMsg;
import coed.collab.protocol.ReleaseLockMsg;
import coed.collab.protocol.RequestLockMsg;
import coed.collab.protocol.RequestLockReplyMsg;
import coed.collab.protocol.SendChangesMsg;
import coed.collab.protocol.SendContentsMsg;
import coed.collab.server.CollaboratorServer;
import coed.collab.server.Session;

/**
 * @author Neobi008
 * Class for testing main functionalities of the Session class, and also
 * main functionalitites of the server itself.
 */
public class SessionTest {
	private Session session1;
	private CollaboratorServer server;
	private ICoedConnection conn1;
	public CoedMessage message1;
	
	private Session session2;
	private ICoedConnection conn2;
	public CoedMessage message2;
	
	class MockConnection implements ICoedConnection{
		
		private long curSequenceID;
		
		public MockConnection(){
			curSequenceID = 0;
		}
		
		@Override
		public void addListener(ICoedConnectionListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isConnected() {
			return true;
		}

		@Override
		public void removeListener(ICoedConnectionListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public IFuture<Void> reply(CoedMessage to, CoedMessage with) {
			message1 = with;
			message2 = with;
			//CoedFuture<CoedMessage> result = new CoedFuture<CoedMessage>();
			//result.set(new SendContentsMsg("online content"));
			return new CoedFuture<Void>();
		}

		@Override
		public IFuture<CoedMessage> replySeq(CoedMessage to, CoedMessage with) {
			// TODO Auto-generated method stub
			message1 = with;
			message2 = with;
			CoedFuture<CoedMessage> result = new CoedFuture<CoedMessage>();
			result.set(new SendContentsMsg("online content"));
			return result;
		}

		@Override
		public IFuture<Void> send(CoedMessage msg) {
			message1 = msg;
			message2 = msg;
			return null;
		}

		@Override
		public IFuture<CoedMessage> sendSeq(CoedMessage msg) {
			message1 = msg;
			message2 = msg;
			return null;
		}

		@Override
		public void disconnect() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	@Before public void setUp(){
		conn1 = new MockConnection();
		server = new CollaboratorServer("..\\.\\config.ini");
		session1 = new Session(conn1,server);
		
		conn2 = new MockConnection();
		session2 = new Session(conn2,server);
	}
	
	@Test 
	public void testAuthentificationMsgHanlde(){
		session1.received(new AuthentificationMsg("userName1",""));
		assertTrue(session1.getUserName().equals("userName1"));
		
		session2.received(new AuthentificationMsg("userName2",""));
		assertTrue(session2.getUserName().equals("userName2"));
	}
	
	@Test
	public void testGoOnlineMsg_SendContentsMsgHandle(){
		session1.setUserName("userName1");
		session2.setUserName("userName2");
		//the file is not already online
		session1.received(new CreateSessionMsg("fileName"));
		//now there should have been sent a GoOnlineResult<false> msg to the connection
		assertTrue( !((CreateSessionResultMsg)message1).isAlreadyOnline() );
		//thus, file is not already online.
		try{
			Thread.sleep(100);
		}catch(Exception e){}
		//file should be online now
		assertTrue(server.isFileOnline("fileName"));
		
		//the contents of the server should be "online content" ...see MockConnection innerclass
		//thus, SendContentsMsg also tested
		assertTrue(server.getServerFile("fileName").getCurrentContents().equals("online content"));
		
		//if we try to put the file now online again, it should return that it is
		//already online, sending back GoOnlineResultMsg<true>
		session2.received(new CreateSessionMsg("fileName"));
		assertTrue( ((CreateSessionResultMsg)message2).isAlreadyOnline() );
		
		//but, session2 should be registered to this file as well
		assertEquals(server.getServerFile("fileName").getActiveUsers().length,2);
	}
	
	@Test
	public void testRequestLockMsg(){
		session1.setUserName("userName1");
		session2.setUserName("userName2");
		//put file online
		session1.received(new CreateSessionMsg("fileName"));
		//register session2 to that file
		session2.received(new CreateSessionMsg("fileName"));
		
		//lock portion (3,3) by session1
		session1.received(new RequestLockMsg("fileName",new TextPortion(3,3)));
		
		//lock should have been granted
		assertTrue( ((RequestLockReplyMsg)message1).getResult() );
		
		//trying to lock the same portion again by session2
		session2.received(new RequestLockMsg("fileName",new TextPortion(3,3)));
		
		//lock should have been denied
		assertTrue( !((RequestLockReplyMsg)message2).getResult() );
	}
	
	@Test
	public void testReleaseLockMsg(){
		//put file online
		session1.setUserName("userName1");
		session2.setUserName("userName2");
		
		session1.received(new CreateSessionMsg("fileName"));
		//register session2 to that file
		session2.received(new CreateSessionMsg("fileName"));
		
		//lock portion (3,3) by session1
		session1.received(new RequestLockMsg("fileName",new TextPortion(3,3)));
		
		//trying to release lock by other session
		session2.received(new ReleaseLockMsg("fileName",new TextPortion(3,3)));
		
		//attempt should have failed, thus lock should be still there
		//thus, attempting to lock same portion should fail
		session2.received(new RequestLockMsg("fileName",new TextPortion(3,3)));
		
		assertTrue( !((RequestLockReplyMsg)message2).getResult() );
		
		//now release lock by session1
		session1.received(new ReleaseLockMsg("fileName",new TextPortion(3,3)));
		
		//attempt should have succeeded, thus trying to lock same portion
		//by session2, should succeed as well
		session2.received(new RequestLockMsg("fileName",new TextPortion(3,3)));
		
		assertTrue( ((RequestLockReplyMsg)message2).getResult() );
	}
	
	@Test
	public void testSendChangesMsg(){
		//put file online
		session1.setUserName("userName1");
		session1.received(new CreateSessionMsg("fileName"));
	
		//register session2 to that file
		session2.setUserName("userName2");
		session2.received(new CreateSessionMsg("fileName"));
		
		//send change, but do not put listener on the file
		TextModification[] mods = new TextModification[1];
		mods[0] = new TextModification(2,2,"33","userName1");
		session1.received(new SendChangesMsg("fileName",mods));
		
		//now content on the server should be "onl33ine content"
		assertTrue(server.getServerFile("fileName").getCurrentContents().equals("on33line content"));	
	}
	
	@Test
	public void testGetChangesMsg(){
		session1.setUserName("userName1");
		session2.setUserName("userName2");
		//put file online
		session1.received(new CreateSessionMsg("fileName"));
		//register session2 to that file
		session2.received(new CreateSessionMsg("fileName"));
		
		//send change, but do not put listener on the file
		TextModification[] mods = new TextModification[1];
		mods[0] = new TextModification(2,2,"33","userName1");
		session1.received(new SendChangesMsg("fileName",mods));
		
		//getting changes for the same user should return an empty answer
		//because for his session there are no changes
		session1.received(new GetChangesMsg("fileName"));
		assertEquals( ((GetChangesReplyMsg)message1).getMods().length,0 );
		
		//for session2 there should be 1 change
		session2.received(new GetChangesMsg("fileName"));
		assertEquals( ((GetChangesReplyMsg)message2).getMods().length,1 );
	}
	
	/**
	 * This test case also test the functioning of the FileChangedListener
	 */
	
	@Test
	public void testAddChangedListenerMsgHandle(){
		session1.setUserName("userName1");
		session2.setUserName("userName2");
		
		//put file online
		session1.received(new CreateSessionMsg("fileName"));
		//register session2 to that file
		session2.received(new CreateSessionMsg("fileName"));
		
		//add ChangeListener for the first session
		session1.received(new AddChangedListenerMsg("fileName"));
		
		//now if session1 makes a change, he won't be notified, because it is his own change.
		TextModification[] mods = new TextModification[1];
		mods[0] = new TextModification(2,2,"33","userName1");
		session1.received(new SendChangesMsg("fileName",mods));
		
		//now if we verify the last received message , it should be the response for the
		//goOnline request, thus, no FileChanged message was received by this session
		assertTrue(message1 instanceof CreateSessionResultMsg);
		
		//add ChangeListener for the second session
		session2.received(new AddChangedListenerMsg("fileName"));
		
		//now if session1 makes a change, session2 will be notified by a FileChangesMsg
		session1.received(new SendChangesMsg("fileName",mods));
		
		assertTrue(((FileChangedMsg)message2).getFileName().equals("fileName"));
	}
	
	
	
}
