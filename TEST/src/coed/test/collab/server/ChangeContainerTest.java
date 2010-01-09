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
import coed.collab.server.ChangeContainer;
import coed.collab.server.CoedFileChange;


public class ChangeContainerTest {
	private ChangeContainer<CoedFileChange> cont;
	
	@Before public void setUp(){
		cont = new ChangeContainer<CoedFileChange>();
	}
	
	@Test public void testAdd(){
		CoedFileChange chg1 = new CoedFileChange(new TextModification(10,3,"abc"),new Date());
		cont.add(0,chg1);
		assertTrue(cont.elementAt(0).equals(chg1));
	}
	
	@Test (expected=ArrayIndexOutOfBoundsException.class) 
	public void testRemove(){
		assertNull(cont.remove(10));
		CoedFileChange chg1 = new CoedFileChange(new TextModification(10,3,"abc"),new Date());
		cont.add(0,chg1);
		assertTrue(cont.remove(0).equals(chg1));
	}
	
	@Test (expected=ArrayIndexOutOfBoundsException.class)
	public void testElementAt(){
		assertNull(cont.elementAt(10));
		CoedFileChange chg1 = new CoedFileChange(new TextModification(10,3,"abc"),new Date());
		cont.add(1,chg1);
		assertTrue(cont.elementAt(1).equals(chg1));
	}
	
	@Test
	public void testSize(){
		assertEquals(cont.size(),0);
		CoedFileChange chg1 = new CoedFileChange(new TextModification(10,3,"abc"),new Date());
		cont.add(0,chg1);
		assertEquals(cont.size(),1);
	}

}
