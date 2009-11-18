package coed.test.collab.config;

import java.util.Date;

import coed.collab.server.ChangeQueue;
import coed.collab.server.CoedFileChange;

public class ChangeStackTest {
	
	public ChangeStackTest(){
		
	}

	public static void main (String args[]){
		ChangeQueue chs = new ChangeQueue();
		String[] s1 = new String[2];
		chs.enQueueChange(new CoedFileChange(10, s1, "user1", new Date()));
		chs.enQueueChange(new CoedFileChange(11, s1, "user2", new Date()));
		System.out.println(chs.getPosition("user2"));
		System.out.println(chs.deQueueChange().getUserName());
		System.out.println(chs.deQueueChange().getUserName());
		//System.out.println(chs.popChange().getSessionID());
	}
}
