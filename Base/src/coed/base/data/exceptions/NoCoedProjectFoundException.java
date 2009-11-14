package coed.base.data.exceptions;

/**
 * Exception if a given project (directory) can not be found in the file system
 * @author Izso
 *
 */
public class NoCoedProjectFoundException extends NoCoedMatchFoundException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoCoedProjectFoundException(){}
	
	public NoCoedProjectFoundException(String msg){
		super(msg);
	}

}