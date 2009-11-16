package coed.test.collab.config;

import java.util.Date;

import coed.collab.server.ChangeStack;
import coed.collab.server.CoedFileChange;

public class ChangeStackTest {
	
	public ChangeStackTest(){
		
	}

	public static void main (String args[]){
		ChangeStack chs = new ChangeStack(10);
		String[] s1 = new String[2];
		chs.pushChange(new CoedFileChange(10, s1, 1, new Date()));
		chs.pushChange(new CoedFileChange(11, s1, 2, new Date()));
		System.out.println(chs.getPosition(1));
		System.out.println(chs.popChange().getSessionID());
		System.out.println(chs.popChange().getSessionID());
		//System.out.println(chs.popChange().getSessionID());
	}
}
