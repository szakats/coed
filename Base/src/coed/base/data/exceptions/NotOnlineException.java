package coed.base.data.exceptions;

public class NotOnlineException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotOnlineException(){}
	
	public NotOnlineException(String msg){
		super(msg);
	}

}