/**
 * 
 */
package coed.base.comm;

/**
 * @author neobi008
 *
 */
public interface IAllSessionsListener {
	
	public void sessionAdded(Integer id, String path);
	public void sessionRemoved(Integer id, String path);

}
