package coed.base.data;

import java.io.File;

import coed.base.data.exceptions.NoCoedFileFoundException;
import coed.base.data.exceptions.NoCoedProjectFoundException;

public class FileManager {
		
	public static CoedFile getCoedFileFor(String path) throws NoCoedFileFoundException {
		return null;
	
	}
	
	public static CoedProject getCoedProjectFor(String path) throws NoCoedProjectFoundException{
		return null;
	
	}
	
	public static File getFileFor(CoedFile file){
		return null;
	}
	
	public static void updateLocal(CoedFile file){
	
	}
	
	public static void updateLocal(CoedProject proj){
	
	}
	
	public static void deleteLocal(CoedFile file) throws NoCoedFileFoundException{
	
	}
	
	public static void deleteLocal(CoedProject project) throws NoCoedProjectFoundException{
	
	}
	
}