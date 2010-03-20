/**
 * 
 */
package coed.collab.protocol;

/**
 * @author szakats
 *
 */
public class JoinReplyMsg extends CoedMessage {
	private String contents;

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public JoinReplyMsg(String contents) {
		super();
		this.contents = contents;
	}
}
