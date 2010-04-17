/**
 * 
 */
package coed.collab.server;

import coed.collab.protocol.FileChangedMsg;

/**
 * @author Neobi
 * Listener for determining if the file was changed or not.
 * We are not interested in the changes itself, only the fact
 * that the file was changed or not.
 */
public class FileChangedListener{
	
	private boolean changedStatus;
	private Integer id;
	private Session session;
	
	public FileChangedListener(Integer id, Session session){
		this.changedStatus = false;
		this.id = id;
		this.session = session;
	}
	
	public boolean getStatus(){
		return this.changedStatus;
	}
	
	public Integer getId(){
		return this.id;
	}
	
	public Session getSession(){
		return this.session;
	}
	
	public synchronized void setStatus(boolean status){
		this.changedStatus = status;
	}
	
	/**
	 * The update method will send a FileChanged message to the client
	 * only if this change was the first change since the last getchanges.
	 * (i.e. we do not signal at every change that the file changed, just
	 *  at the first change)
	 * @param status
	 */
	public void update(boolean status){
		if ((this.changedStatus == false) && (status == true)){
			// the first change occurred since the session got the changes. 
			//send a message.
			System.out.println("fileChangedListener sendind changed notification");
			session.getConn().send(new FileChangedMsg(getId()));
		}
		this.setStatus(status);
		//System.out.println("wow, that changed");
	}

}
