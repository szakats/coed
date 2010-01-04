package coed.collab.protocol;

public class RequestLockReplyMsg extends CoedMessage{
	private boolean granted;
	
	public RequestLockReplyMsg(boolean granted){
		this.granted = granted;
	}
	
	public boolean getResult(){
		return this.granted;
	}
	
	public void setResult(boolean granted){
		this.granted = granted;
	}

}
