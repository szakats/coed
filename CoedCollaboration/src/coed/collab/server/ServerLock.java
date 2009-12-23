/**
 * 
 */
package coed.collab.server;

import java.util.Date;


import coed.base.data.TextPortion;

/**
 * @author Neobi008
 * Class representing a lock on a portion of text. It is specified by the 
 * mod attribute (starting offset and length of locked portion)
 * and the Session gives us the user that requested the lock.
 * Date filed contains the exact time the lock was requested.
 */
public class ServerLock {
	
	private Session session;
	private TextPortion mod;
	private Date added;
	
	public ServerLock(TextPortion mod, Date added, Session session){
		this.session = session;
		this.mod = mod;
		this.added = added;
	}
	
	public int getOffset() {
		return mod.getOffset();
	}
	
	public void setOffset(int offset) {
		mod.setOffset(offset);
	}
	
	/**
	 * length is the length of the textportion that is locked
	 * @return
	 */
	public int getLength() {
		return mod.getLength();
	}
	
	public void setLength(int length){
		mod.setLength(length);
	}
	
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public TextPortion getMod() {
		return mod;
	}
	public void setMod(TextPortion mod) {
		this.mod = mod;
	}
	public Date getAdded() {
		return added;
	}
	public void setAdded(Date added) {
		this.added = added;
	}
	
	public boolean overlaps(ServerLock lock){
		boolean result = false;
		
		if ((this.mod.getOffset() < lock.getOffset()) &&
		    (this.mod.getOffset() + this.mod.getLength() > lock.getOffset()))
			result = true;
		else
			if ((this.mod.getOffset() > lock.getOffset()) &&
			    (this.mod.getOffset() < lock.getOffset()+lock.getLength()))
				result = true;
		return result;
	}
}
