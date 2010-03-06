/**
 * 
 */
package coed.collab.protocol;

/**
 * @author neobi008
 *
 */
public class AuthenticationReplyMsg extends CoedMessage{
	
	private boolean result;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthenticationReplyMsg(boolean result){
		this.result = result;
	}

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
}
