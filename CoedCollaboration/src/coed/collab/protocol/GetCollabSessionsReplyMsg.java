/**
 * 
 */
package coed.collab.protocol;

import java.util.HashMap;
import java.util.Map;

import coed.base.util.ArrayMap;

/**
 * @author szakats
 *
 */
public class GetCollabSessionsReplyMsg extends CoedMessage {
	private Map<Integer, String> sessions;
	
	public Map<Integer, String> getSessions() {
		return sessions;
	}
	
	public void setSessions(Map<Integer, String> sessions) {
		this.sessions = sessions;
	}
	
	public void addSession(Integer id, String path) {
		if(sessions == null)
			sessions = new HashMap<Integer, String>();
		sessions.put(id, path);
	}
}
