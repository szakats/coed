package coed.test.collab.config;

import coed.collab.server.FileManager;
import coed.collab.server.ServerFile;

public class FileManagerTest {
	
	public FileManagerTest(){
		
	}
	
	public static void main(String args[]){
		
		FileManager fm = new FileManager();
		
		ServerFile[] files = fm.getAllLiveFiles();
		System.out.println(files.length); 
		
		fm.addFile(new ServerFile("abc"));
		fm.addFile(new ServerFile("def"));
		fm.addFile(new ServerFile("ghi"));
		files = fm.getAllLiveFiles();
		
		for (int i=0; i<files.length; i++)
			System.out.println(files[i].getPath());
		fm.removeFile("def");
		System.out.println("After Removal of def");
		files = fm.getAllLiveFiles();
		for (int i=0; i<files.length; i++)
			System.out.println(files[i].getPath());
		
	}

}
