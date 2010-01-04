package coed.collab.protocol;

public class GetUserListMsg extends CoedMessage{
	private String file;
	
	public GetUserListMsg (String file){
		this.file = file;
	}
	
	public String getFile(){
		return this.file;
	}
	
	public void setFile(String file){
		this.file = file;
	}

}
