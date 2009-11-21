package coed.base.data.exceptions;

/**
 * Exception for cases when a collaborator method is called that requires
 * a connection to the server but that is not currently established.
 * @author szakats
 *
 */
public class NotConnectedException extends NoCoedMatchFoundException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotConnectedException(){}
	
	public NotConnectedException(String msg){
		super(msg);
	}

}