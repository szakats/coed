package coed.base.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class FileUtil {
	
	/**
	 * Write a serializable object to a file
	 * @param obj the object to write
	 * @param filename the file to write it to
	 * @return true iff successful
	 */
	public static boolean writeObjectToFile(Serializable obj, String filename) {
	    //serialize the object

	    try {
	        //use buffering
	        OutputStream file = new FileOutputStream( filename );
	        OutputStream buffer = new BufferedOutputStream( file );
	        ObjectOutput output = new ObjectOutputStream( buffer );
	        try {
	        	output.writeObject(obj);
	        } finally {
	        	output.close();
	        }
	    } catch(IOException ex) {
	    	ex.printStackTrace();
	    	return false;  
	    }
	    
	    return true;
	}

	/**
	 * Read a serialized object from a file
	 * @param filename the file to read from
	 * @return the object read from the file or null if an error occurred
	 */
	public static Object readObjectFromFile(String filename) {
		//deserialize the the file
      
		try {
			//use buffering
			InputStream file = new FileInputStream( filename );
			InputStream buffer = new BufferedInputStream( file );
			ObjectInput input = new ObjectInputStream ( buffer );
			try {
				//display its data
				return input.readObject();
			} finally {
				input.close();
			}
		} catch(ClassNotFoundException ex){
			ex.printStackTrace();
		} catch(IOException ex){
			ex.printStackTrace();
		}
		return null;
    }
}
