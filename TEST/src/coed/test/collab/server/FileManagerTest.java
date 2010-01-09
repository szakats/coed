/**
 * 
 */
package coed.test.collab.server;

/**
 * JUnit 4.x test
 * @author Neobi
 *
 */

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import coed.collab.server.FileManager;
import coed.collab.server.ServerFile;
import coed.collab.server.Session;

public class FileManagerTest {
	private FileManager fm;
	
	@Before public void setUp(){
		fm = new FileManager();
	}
	
	@Test public void testAddFile(){
		ServerFile sf1 = new ServerFile("path1");
		
		fm.addFile(sf1);
		assertEquals(fm.getAllLiveFiles().length,1);
		
		//now if we try to add the same file again,
		//it won't add. thus the number of files will
		//remain unchanged (1)
		
		fm.addFile(sf1);
		assertEquals(fm.getAllLiveFiles().length,1);
	}
	
	@Test public void testRemoveFile(){
		ServerFile sf1 = new ServerFile("path1");
		
		//try to remove a file that is not in the manager.
		//thus, the number of files should be unchanged.
		fm.removeFile("path1");
		assertEquals(fm.getAllLiveFiles().length,0);
		
		//try to remove a file that is in the manager. 
		//nr of files should decrement by 1
		fm.addFile(sf1);
		assertEquals(fm.getAllLiveFiles().length,1);
		
		fm.removeFile("path1");
		assertEquals(fm.getAllLiveFiles().length,0);
	}
	
	@Test public void testGetAllLiveFiles(){
		ServerFile sf1 = new ServerFile("path1");
		ServerFile sf2 = new ServerFile("path2");
		//if there is no file in the manager,
		//method should return a 0 sized array
		
		assertEquals(fm.getAllLiveFiles().length,0);
		
		//if we add now one file, one file should be returned
		fm.addFile(sf1);
		assertEquals(fm.getAllLiveFiles().length,1);
		fm.addFile(sf2);
		assertEquals(fm.getAllLiveFiles().length,2);
		assertTrue(fm.getAllLiveFiles()[0].equals(sf2));
		assertTrue(fm.getAllLiveFiles()[1].equals(sf1));
		
		//after a successfull removal, the size should decrement
		fm.removeFile("path2");
		assertEquals(fm.getAllLiveFiles().length,1);
	}
	
	@Test public void testContainsFile(){
		ServerFile sf1 = new ServerFile("path1");
		//checking for a file that is not in the manager should return false
		assertEquals(fm.containsFile("path1"),false);
		
		//now add the file, and the checking should return true
		fm.addFile(sf1);
		assertEquals(fm.containsFile("path1"),true);
	}
	
	@Test public void testGetFile(){
		
		ServerFile sf1 = new ServerFile("path1");
		//if the searched file is not in the manager, it should return null
		assertNull(fm.getFile("path1"));
		
		//if file is in the manager, it should be returned
		fm.addFile(sf1);
		assertEquals(fm.getFile("path1"),sf1);
	}

	@Test public void testRemoveSessionFromFile(){
		ServerFile sf1 = new ServerFile("path1");
		Session s1 = new Session(null,null);
		Session s2 = new Session(null,null);
		//if file not in the manager, no action is taken
		
		//if file is in the manager, remove the specified session
		fm.addFile(sf1);
		sf1.addSession(s1);
		assertEquals(sf1.getNrOfSessions(),1);
		
		//now if we try to remove a session that is not 
		//registered to this file, nothing happens
		fm.removeSessionFromFile(sf1, s2);
		assertEquals(sf1.getNrOfSessions(),1);
		
		//if we try to remove a session that is really registered
		//the number of sessions should be decremented
		sf1.addSession(s2);
		fm.removeSessionFromFile(sf1,s2);
		assertEquals(sf1.getNrOfSessions(),1);
		
		//test if automatically removes the file when there are 0 sessions on it
		
		fm.removeSessionFromFile(sf1, s1);
		assertEquals(sf1.getNrOfSessions(),0);
		assertEquals(fm.getAllLiveFiles().length,0);
		assertEquals(fm.containsFile(sf1.getPath()),false);
	}
}
