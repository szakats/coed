package coed.base.data.exceptions;

import java.io.IOException;

/**
 * Generic exception for cases when some kind of resource is not found
 * in the file system
 * @author Izso
 *
 */
public class NoCoedMatchFoundException extends IOException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoCoedMatchFoundException(){}
	
	public NoCoedMatchFoundException(String msg){
		super(msg);
	}

}