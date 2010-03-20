/**
 * 
 */
package coed.base.comm;

import coed.base.data.ICoedFile;
import coed.base.data.IVersionedFilePart;

/**
 * @author szakats
 *
 */
public interface ICoedVersionerPart extends ICoedVersioner {
	   /**
	    * Make a new a IVersioned and set it as a child of the given ICoedObject.
	    * The new object will be created in accordance with the path in its parent.
	    * @param obj the parent of the object to be created
	    * @return the new versioned object
	    */
	   public IVersionedFilePart makeVersionedFile(ICoedFile obj);
}
