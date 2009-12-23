package coed.test.collab.server;

import coed.base.data.TextModification;
import coed.base.data.TextPortion;
import coed.collab.server.CollaboratorServer;
import coed.collab.server.ServerFile;
import coed.collab.server.Session;

public class ServerLockTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CollaboratorServer server = new CollaboratorServer();
		ServerFile sf1 = new ServerFile("\\x\\o.txt");
		Session s1 = new Session(null, null); //not interested in connection part yet
		Session s2 = new Session(null, null); //not interested in connection part yet
		s1.setUserName("user1");
		s2.setUserName("user2");
		
		try{
			sf1.changeContents("This is the initial contents of the file.");
			}catch(Exception e){}
		
		sf1.addSession(s1);
		sf1.addSession(s2);
		
		TextPortion tp1 = new TextPortion(0,5);
		TextPortion tp2 = new TextPortion(1,6);
		
		System.out.println("requesting overlapping locks by different users");
		System.out.println("first lock result:" +sf1.RequestLock(tp1, s1));
		System.out.println("second lock result:" +sf1.RequestLock(tp2, s2));
		System.out.println("requesting overlapping locks by same user");
		System.out.println("second lock result:" +sf1.RequestLock(tp2, s1));
		
		System.out.println("request lock on (10,5)");
		TextPortion tp3 = new TextPortion(10,5);
		sf1.RequestLock(tp3, s1);
		System.out.println("make a change at (9,5). thus lock should be shifted with 5");
		TextModification tm1 = new TextModification(9,5,"abcde","user1");
		sf1.addChange(tm1,s1);
		System.out.println("file's contents:"+sf1.getCurrentContents());
		System.out.println("shifted lock should be at (15,5), thus trying to lock (16,2) by other user should fail");
		sf1.setChangePointer(s2,3); //simulate that s2 has got all the changes til now
		TextPortion tp4 = new TextPortion(16,2);
		System.out.println("trying to lock (16,2). result: "+sf1.RequestLock(tp4, s2));
		
		sf1.setChangePointer(s1, 3); //simulating that s1 has all the changes
		System.out.println("releasing lock (15,5)...");
		sf1.ReleaseLock(new TextPortion(15,5), s1);
		System.out.println("trying to request (16,2). result: "+sf1.RequestLock(tp4, s2));
	}

}
