package coed.base.data.exceptions;

/**
 * Exception for cases when the file associated with the CoedFile
 * description cannot be found, or does not exist
 * @author Izso
 *
 */
public class NoCoedFileFoundException extends NoCoedMatchFoundException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoCoedFileFoundException(){}
	
	public NoCoedFileFoundException(String msg){
		super(msg);
	}

}