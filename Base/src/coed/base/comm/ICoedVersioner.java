package coed.base.comm;

import coed.base.data.ICoedObject;
import coed.base.data.IVersionedObject;

/**
 * TODO: description
 * @author Neobi
 * 
 */
public interface ICoedVersioner {
	
	public final static String STATIC = "static";
	public final static String GIT = "git";
	
   
   /**
	* TODO: javadoc
	* @return
   */
   public String getType();
   
   
   /**
    * 
    * @param obj
    * @return
    */
   public IVersionedObject makeVersionedObject(ICoedObject obj);
}