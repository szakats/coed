package coed.test.collab.server;

import static org.junit.Assert.assertEquals;
/**
 * JUnit 4.x test 
 * @author Neobi
 */
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

import coed.base.data.TextPortion;
import coed.collab.server.ServerLock;
import coed.collab.server.Session;

public class ServerLockTest1{
	private ServerLock lock1;
	
	@Before public void setUp(){
		lock1 = new ServerLock(new TextPortion(10,20), new Date(), new Session(null,null));
	}
	
	@Test public void testSetOffset(){
		lock1.setOffset(15);
		assertEquals(lock1.getOffset(),15);
	}
	
	@Test public void testSetLength(){
		lock1.setLength(20);
		assertEquals(lock1.getLength(),20);
	}
	
	@Test public void testSetSession(){
		Session s1 = new Session(null,null);
		lock1.setSession(s1);
		assertTrue(lock1.getSession().equals(s1));
	}
	
	@Test public void testSetMod(){
		TextPortion p = new TextPortion(30,30);
		lock1.setMod(p);
		assertTrue(lock1.getMod().equals(p));
	}
	
	@Test public void testOverlaps(){
		ServerLock lock2 = new ServerLock(new TextPortion(10,20), new Date(), new Session(null,null));
		lock1.setMod(new TextPortion(8,15));
		assertTrue(lock1.overlaps(lock2));
	}

}
