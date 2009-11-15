package coed.base.common;

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