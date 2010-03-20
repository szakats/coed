package coed.base.comm;

import coed.base.data.ICoedFile;
import coed.base.data.IVersionedFilePart;
import coed.base.util.IFuture;

/**
 * ICoedVersioner  
 * 
 */
public interface ICoedVersioner {
	
	public final static String STATIC = "static";
	public final static String GIT = "git";
	
   
   /**
	* Get the type of the versioner.
	* @return the type
   */
   public String getType();
}