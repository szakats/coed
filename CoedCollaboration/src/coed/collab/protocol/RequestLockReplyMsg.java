package coed.collab.protocol;

public class RequestLockReplyMsg extends CoedMessage{
	private boolean granted;
	private String file;
	
	public RequestLockReplyMsg(String file, boolean granted){
		this.granted = granted;
		this.file = file;
	}
	
	public boolean getResult(){
		return this.granted;
	}
	
	public void setResult(boolean granted){
		this.granted = granted;
	}
	
	public String getFile(){
		return this.file;
	}
	
	public void setFile(String file){
		this.file = file;
	}


}
