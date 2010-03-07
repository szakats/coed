package coed.test.collab.server;

import coed.base.data.TextModification;
import coed.collab.server.CollaboratorServer;
import coed.collab.server.ServerFile;
import coed.collab.server.Session;

public class NewServerFileTest {
	
	public static void main(String args[]){
		CollaboratorServer server = new CollaboratorServer("..\\.\\config.ini");
		ServerFile sf1 = new ServerFile("\\x\\o.txt");
		Session s1 = new Session(null, null); //not interested in connection part yet
		Session s2 = new Session(null, null); //not interested in connection part yet
		TextModification tm1 = new TextModification(0,10,"abcdefghij","user1");
		TextModification tm2 = new TextModification(18,10,"jijijijiji","user2");
		TextModification tm3 = new TextModification(0,10,"xxxxxxxxxx","user1");
		
		try{
		sf1.changeContents("This is the initial contents of the file.");
		}catch(Exception e){}
		
		System.out.println("initial contents of file is:"+sf1.getCurrentContents());
		System.out.println("two changes by the same user, but no offset correction needed:");
		sf1.addSession(s1);
		sf1.addChange(tm1,s1);
		tm2.setMetaInfo("user1");
		sf1.addChange(tm2, s1);
		System.out.println("The content of the file:" + sf1.getCurrentContents());
		
		System.out.println("***************************************");
		 sf1 = new ServerFile("\\x\\o.txt");
		 tm1 = new TextModification(0,10,"abcdefghij","user1");
		 tm2 = new TextModification(18,10,"jijijijiji","user2");
		 tm3 = new TextModification(0,10,"xxxxxxxxxx","user1");
		try{
			sf1.changeContents("This is the initial contents of the file.");
			}catch(Exception e){}
			
			System.out.println("initial contents of file is:"+sf1.getCurrentContents());
		System.out.println("two changes by user1, but user2 didn't get the user1 modifications:");
		System.out.println("in this case, user2's modification will be shifted:");
		System.out.println("user2's modifications should be relative to his version, which is the version " +
				" without the user1 modifications.");
		sf1.addSession(s1);
		sf1.addSession(s2);
		sf1.addChange(tm1,s1);
		sf1.addChange(tm3,s1);
		System.out.println("Session2 changepointer is:"+sf1.getChangePointer(s2));
		tm2.setMetaInfo("user2");
		sf1.addChange(tm2,s2);
		System.out.println("The content of the file:" + sf1.getCurrentContents());
		
		System.out.println("***************************************");
		sf1 = new ServerFile("\\x\\o.txt");
		tm1 = new TextModification(0,10,"abcdefghij","user1");
		tm2 = new TextModification(18,10,"jijijijiji","user2");
		tm3 = new TextModification(0,10,"xxxxxxxxxx","user1");
		try{
			sf1.changeContents("This is the initial contents of the file.");
			}catch(Exception e){}
			
			System.out.println("initial contents of file is:"+sf1.getCurrentContents());
		System.out.println("two changes by different users, but user2 got the user1 modifications:");
		System.out.println("in this case, user2's modification will NOT be shifted:");
		System.out.println("user2 has same version as on the server, thus his modification will be " +
				"at position 18 relative to the global version");
		sf1.addSession(s1);
		sf1.addSession(s2);
		sf1.addChange(tm1,s1);
		sf1.addChange(tm3,s1);
		sf1.setChangePointer(s2,2); //already received the first two changes (change 0 and 1)
		System.out.println("Session2 changepointer is:"+sf1.getChangePointer(s2));
		sf1.addChange(tm2,s2);
		System.out.println("The content of the file:" + sf1.getCurrentContents());
	}

}
