package coed.base.comm;

import coed.base.data.ICoedObject;
import coed.base.data.IVersionedObject;

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
   
   /**
    * Make a new a IVersioned and set it as a child of the given ICoedObject.
    * The new object will be created in accordance with the path in its parent.
    * @param obj the parent of the object to be created
    * @return the new versioned object
    */
   public IVersionedObject makeVersionedObject(ICoedObject obj);
}