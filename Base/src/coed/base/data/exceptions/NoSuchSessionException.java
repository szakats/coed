/**
 * 
 */
package coed.base.data.exceptions;

/**
 * @author szakats
 *
 */
public class NoSuchSessionException extends Exception {
	public NoSuchSessionException(){}
	
	public NoSuchSessionException(String msg){
		super(msg);
	}
}
