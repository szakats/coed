package coed.test.collab.server;

import coed.collab.server.CollaboratorServer;
import coed.collab.server.ServerFile;

public class NewServerFileTest {
	
	public static void main(String args[]){
		CollaboratorServer server = new CollaboratorServer();
		ServerFile sf1 = new ServerFile("\\x\\o.txt");
		try{
		server.addNewFile("\\x\\o.txt" , "first content");
		}catch (Exception ex){
			System.out.println("exception1");
		}
		
		System.out.println(server.isFileOnline("\\x\\o.txt"));
	}

}
