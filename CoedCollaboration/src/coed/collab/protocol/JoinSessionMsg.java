/**
 * 
 */
package coed.collab.protocol;

/**
 * @author szakats
 *
 */
public class JoinSessionMsg extends CoedMessage {
	public Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public JoinSessionMsg(Integer id) {
		super();
		this.id = id;
	}
	
}
