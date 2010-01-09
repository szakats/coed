/**
 * JUnit 4.x test 
 * @author Neobi
 */

package coed.test.collab.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import coed.base.data.TextModification;
import coed.collab.server.ChangeQueue;
import coed.collab.server.CoedFileChange;
import coed.collab.server.ServerFile;

public class ChangeQueueTest1 {
	private ChangeQueue queue;
	
	@Before public void setUp(){
		queue = new ChangeQueue(new ServerFile("path"));
	}
	
	@Test public void testEnqueueChange(){
		CoedFileChange chg1 = new CoedFileChange(new TextModification(1,10,"abcdefghij"), new Date());
		queue.enQueueChange(chg1);
		assertTrue(queue.getChangeAt(0).equals(chg1.getMod()));
	}
	
	@Test public void testDequeueChange(){
		CoedFileChange chg1 = new CoedFileChange(new TextModification(1,10,"abcdefghij"), new Date());
		queue.enQueueChange(chg1);
		int index = queue.getTopIndex(); //get index of the change
		assertTrue(queue.deQueueChange().equals(chg1));
		assertEquals(index,queue.getTopIndex()+1);
	}
	
	@Test (expected=ArrayIndexOutOfBoundsException.class) //when trying to acces a change out of range
	public void testGetChangeAt(){
		CoedFileChange chg1 = new CoedFileChange(new TextModification(1,10,"abcdefghij"), new Date());
		queue.enQueueChange(chg1);
		assertTrue(queue.getChangeAt(0).equals(chg1.getMod()));
		assertNull(queue.getChangeAt(10));
	}
	
	@Test public void testGetTopIndex(){
		assertEquals(queue.getTopIndex(),0);
		CoedFileChange chg1 = new CoedFileChange(new TextModification(1,10,"abcdefghij"), new Date());
		queue.enQueueChange(chg1);
		queue.enQueueChange(chg1);
		assertEquals(queue.getTopIndex(),2);
		queue.deQueueChange();
		queue.deQueueChange();
		assertEquals(queue.getTopIndex(),0);
		//test if does not go beyound zero
		queue.deQueueChange();
		assertEquals(queue.getTopIndex(),0);
	}

}
