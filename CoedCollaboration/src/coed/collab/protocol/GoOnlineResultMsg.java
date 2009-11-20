package coed.collab.protocol;

/**
 * This message is sent:
 * - by the server as a reply to the GoOnlineMsg
 * @author szakats
 */
public class GoOnlineResultMsg extends CoedMessage {
	private boolean alreadyOnline;

	public GoOnlineResultMsg(boolean alreadyOnline) {
		super();
		this.alreadyOnline = alreadyOnline;
	}

	/**
	 * @param alreadyOnline the alreadyOnline to set
	 */
	public void setAlreadyOnline(boolean alreadyOnline) {
		this.alreadyOnline = alreadyOnline;
	}

	/**
	 * @return the alreadyOnline
	 */
	public boolean isAlreadyOnline() {
		return alreadyOnline;
	}
}
