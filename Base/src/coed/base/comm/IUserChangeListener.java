/**
 * 
 */
package coed.base.comm;

/**
 * @author neobi008
 *
 */
public interface IUserChangeListener {
	
	public void userAdded(String name, Integer sessionId);
	public void userRemoved(String name, Integer sessionId);
}
