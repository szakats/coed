package coed.test.collab.server;

import java.util.Date;

import coed.base.data.TextModification;
import coed.collab.server.ChangeQueue;
import coed.collab.server.CoedFileChange;
import coed.collab.server.ServerFile;

public class ChangeQueueTest {
	
	public static void main (String args[]){
		ChangeQueue chq = new ChangeQueue(new ServerFile("\\home\\blabla"));
		
		chq.enQueueChange(new CoedFileChange(new TextModification(1,10,"abcdefghij"), new Date()));
		System.out.println(chq.getTopIndex());
		chq.enQueueChange(new CoedFileChange(new TextModification(3,10,"jijijijiji"), new Date()));
		System.out.println(chq.getTopIndex());
		for (int i=0; i<chq.getTopIndex(); i++)
			System.out.println(chq.getChangeAt(i).getText());
			
		
	}

}
