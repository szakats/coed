package coed.base.data.exceptions;

/**
 * Exception thrown when an unidentifiable versioner type has been detected
 * @author Neobi
 *
 */

public class UnknownVersionerTypeException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public UnknownVersionerTypeException() {}
	
	public UnknownVersionerTypeException(String msg){
		super(msg);
	}
}
