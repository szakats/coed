package coed.base.data.exceptions;

/**
 * Exception for cases when a collaborator method is called that requires
 * a connection to the server but that is not currently established.
 * @author szakats
 *
 */
public class NotConnectedToServerException extends NoCoedMatchFoundException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotConnectedToServerException(){}
	
	public NotConnectedToServerException(String msg){
		super(msg);
	}

}