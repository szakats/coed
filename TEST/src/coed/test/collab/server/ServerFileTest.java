/**
 * JUnit 4.x test 
 * @author Neobi
 */
package coed.test.collab.server;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


import org.junit.Before;
import org.junit.Test;

import coed.base.data.TextModification;
import coed.base.data.TextPortion;
import coed.collab.server.ServerFile;
import coed.collab.server.Session;

public class ServerFileTest {
	private ServerFile sf;
	
	@Before public void setUp(){
		sf = new ServerFile("path");
	}
	
	@Test public void testSetPath(){
		sf.setPath("xyz");
		assertEquals(sf.getPath(),"xyz");
	}
	
	@Test public void testChangeContents(){
		String cont = new String("content");
		try{
		sf.changeContents(cont);
		}catch(Exception ex) {}
		assertTrue(sf.getCurrentContents().equals(cont));
	}
	
	@Test public void testAddSession(){
		Session s1 = new Session(null,null);
		sf.addSession(s1);
		assertEquals(sf.getNrOfSessions(),1);
		//test that it does not add the same session twice
		sf.addSession(s1);
		assertEquals(sf.getNrOfSessions(),1);
	}
	
	@Test public void testRemoveSession(){
		Session s1 = new Session(null,null);
		sf.addSession(s1);
		assertEquals(sf.getNrOfSessions(),1);
		sf.removeSession(s1);
		assertEquals(sf.getNrOfSessions(),0);
		sf.removeSession(s1);
		assertEquals(sf.getNrOfSessions(),0);
	}
	
	@Test public void testGetNrOfSessions(){
		assertEquals(sf.getNrOfSessions(),0);
		Session s1 = new Session(null,null);
		sf.addSession(s1);
		assertEquals(sf.getNrOfSessions(),1);
		sf.removeSession(s1);
		assertEquals(sf.getNrOfSessions(),0);
	}
	
	@Test 
	public void testSetChangePointer(){
		Session s1 = new Session(null,null);
		
		sf.addSession(s1);
		//put one change into the queue
		try{
		sf.changeContents("a");
		}catch(Exception e){}
		sf.addChange(new TextModification(0,10,"abcdefghij"), s1);
		sf.setChangePointer(s1,1);
		assertEquals(sf.getChangePointer(s1),1);
		
		//test that in case of a greater value, the pointer is set to the top
		sf.setChangePointer(s1,5);
		assertEquals(sf.getChangePointer(s1),1);
		
		//test that if the session is not int the hashmap, no changepointer is added
		sf.removeSession(s1);
		sf.setChangePointer(s1,1); 
		assertEquals(sf.getChangePointer(s1),-1);
	}
	
	@Test 
	public void testGetChangePointer(){
		Session s1 = new Session(null,null);
		//if session not registered, the pointer is -1
		assertEquals(sf.getChangePointer(s1),-1);
		
		sf.addSession(s1);
		try{
			sf.changeContents("a");
			}catch(Exception e){}
		sf.addChange(new TextModification(0,10,"abcdefghij"), s1);
		sf.setChangePointer(s1,1);
		assertEquals(sf.getChangePointer(s1),1);
	}
	
	@Test public void testAddChange(){
		Session s1 = new Session(null,null);
		s1.setUserName("user1");
		String oldcont = "old content";
		try{
			sf.changeContents(oldcont);
		}catch (Exception ex) {}
		sf.addChange(new TextModification(0,10,"abcdefghij","user1"), s1);
		//test that if session is not registered, addchange has no effect
		assertEquals(sf.getCurrentContents(),oldcont);
		
		sf.addSession(s1);
		sf.addChange(new TextModification(0,10,"abcdefghij","user1"), s1);
		assertEquals(sf.getCurrentContents(),"abcdefghijold content");
		
		//test if the offsets are properly updated
		//case 1: changes from the same session.
		sf.addChange(new TextModification(5,3,"333","user1"), s1);
		sf.addChange(new TextModification(1,3,"111","user1"), s1);
		//now the "333" change should be shifted
		assertEquals(sf.getCurrentContents(),"a111bcde333fghijold content");
		
		//case 2: changes from 2 different sessions
		Session s2 = new Session(null,null);
		s2.setUserName("user2");
		sf.addSession(s2);
		//now user2 has the newest contents. we show this by setting
		//the changepointer to maximum.
		sf.setChangePointer(s2, 4);
		// now s2 issues a change to position 2
		sf.addChange(new TextModification(2,3,"222","user2"), s2);
		assertEquals(sf.getCurrentContents(),"a122211bcde333fghijold content");
		//now s1 issues another change to position 1. but his version is 
		//"a111bcde333fghujold content. thus, the change will be shifted
		sf.addChange(new TextModification(1,3,"000","user1"), s1);
		assertEquals(sf.getCurrentContents(),"a000122211bcde333fghijold content");
	}
	
	@Test public void testRequestLock(){
		Session s1 = new Session(null,null);
		s1.setUserName("user1");
		Session s2 = new Session(null,null);
		s2.setUserName("user2");
		//if not registered, false is returned upon return from requestlock
		assertEquals(sf.RequestLock(new TextPortion(10,10), s1),false);
		
		//case 1: if registered, try to put lock
		sf.addSession(s1);
		assertTrue(sf.RequestLock(new TextPortion(10,10), s1));
		
		//case2: two locks from the same session. 
		//locks do not overlap
		sf.RequestLock(new TextPortion(10,2), s1);
		sf.RequestLock(new TextPortion(3,5), s1);
		//now, because of the second request, the first lock is on 13,2
		//thus, trying to lock 14,2 by another session should fail!
		assertEquals(sf.RequestLock(new TextPortion(13,2), s2),false);
		
		//case3: two locks from same user that overlap.
		//the locks are combined(concatenated)
		assertEquals(sf.RequestLock(new TextPortion(13,2), s1),true);
		
		//case4: two locks from different users.
		//locks do not overlap, thus the one with 
		//the greater offset will be shifted
		sf.RequestLock(new TextPortion(20,5), s1);
		sf.RequestLock(new TextPortion(15,5), s2);
		//now, the first lock is shifted to 25,5. thus, trying to lock
		//26,5 by s2 should fail
		assertEquals(sf.RequestLock(new TextPortion(26,5), s2),false);
		
		//case5: two locks form different users.
		//they overlap, thus the second request should fail
		sf.RequestLock(new TextPortion(30,5), s1);
		assertEquals(sf.RequestLock(new TextPortion(28,5), s2),false);
	}
	
	@Test public void testReleaseLock(){
		Session s1 = new Session(null,null);
		s1.setUserName("user1");
		Session s2 = new Session(null,null);
		s2.setUserName("user2");
		
		sf.addSession(s1);
		sf.addSession(s2);
		sf.setChangePointer(s1,0);
		sf.setChangePointer(s2,0);
		
		//a session locks a portion, and then unlocks it.
		sf.RequestLock(new TextPortion(20,5), s1);
		//portion locked. now a second try by different user should fail
		assertEquals(sf.RequestLock(new TextPortion(20,5), s2),false);
		//now release the lock, thus trying to aquire a lock to same place 
		//should succeed.
		sf.ReleaseLock(new TextPortion(20,5), s1);
		assertEquals(sf.RequestLock(new TextPortion(20,5), s1),true);
		
		//if session s2 tries to unlock a lock that was
		//issued by s1, it should fail.
		sf.ReleaseLock(new TextPortion(20,5), s2);
		//this should have failed, thus, trying to lock the same place
		//by s2 should fail
		assertEquals(sf.RequestLock(new TextPortion(20,5), s2),false);
	}
}
