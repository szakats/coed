package coed.test.collab.config;

import java.io.File;

/*import coed.base.common.CoedObject;
import coed.base.data.IFileObserver;

public class FileStructureTest {
	
	public void addChangeListener(IFileObserver fileObserver) {
		File f = new File(coll.getBasePath()+ obj.getPath());
		
		File[] listOfFiles = f.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        coll.makeCollabObject(new CoedObject(obj.getPath(),true));
	      } else if (listOfFiles[i].isDirectory()) {
	    	    recursiveAddChangeListener(fileObserver, obj.getPath()+"\\"+listOfFiles[i].getName());
	        System.out.println("Directory " + listOfFiles[i].getName());
	      }
	    }
	}
	
	public void recursiveAddChangeListener(IFileObserver fileObserver, String path){
		File f = new File(coll.getBasePath()+ path);
		
		File[] listOfFiles = f.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        coll.makeCollabObject(new CoedObject(path+"\\"+listOfFiles[i].getName(),true));
	      } else if (listOfFiles[i].isDirectory()) {
	    	    recursiveAddChangeListener(fileObserver, path+"\\"+listOfFiles[i].getName());
	        System.out.println("Directory " + listOfFiles[i].getName());
	      }
	    }

}*/

public class FileStructureTest {
	
	String basePath = "C:\\";
	
	public FileStructureTest(){
		
	}
	
	public void addChangeListener() {
	    recursiveAddChangeListener("\\mydir");
	     
	}
	
	public void recursiveAddChangeListener(String path){
		File f = new File(basePath+ path);
		
		File[] listOfFiles = f.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	       System.out.println(path+"\\"+listOfFiles[i].getName());
	      } else if (listOfFiles[i].isDirectory()) {
	    	    recursiveAddChangeListener(path+"\\"+listOfFiles[i].getName());
	    	    System.out.println("DIR   : "+path+"\\"+listOfFiles[i].getName());
	      }
	    }
	}
	
	public static void main(String args[]){
		FileStructureTest fst = new FileStructureTest();
		fst.addChangeListener();
	}
}
