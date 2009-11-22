package coed.base.data.exceptions;

/**
 * Exception for all cases where a connection to a remote peer
 * is required but not present
 * @author szakats
 */
public class NotConnectedException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotConnectedException(){}
	
	public NotConnectedException(String msg){
		super(msg);
	}

}