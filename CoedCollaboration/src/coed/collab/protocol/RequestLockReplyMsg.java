package coed.collab.protocol;

public class RequestLockReplyMsg extends CoedMessage{
	private boolean granted;
	private Integer Id;
	
	public RequestLockReplyMsg(Integer Id, boolean granted){
		this.granted = granted;
		this.Id = Id;
	}
	
	public boolean getResult(){
		return this.granted;
	}
	
	public void setResult(boolean granted){
		this.granted = granted;
	}
	
	public Integer getId(){
		return this.Id;
	}
	
	public void setId(Integer Id){
		this.Id = Id;
	}


}
