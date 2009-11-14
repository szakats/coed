/**
 * 
 */
package coed.collab.server.db;

/**
 * Exception thrown when connecting to database failed
 * @author Neobi
 *
 */
public class DBConnectionException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DBConnectionException(){
	}
	
	public DBConnectionException(String msg){
		super(msg);
	}

}
