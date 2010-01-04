package coed.collab.protocol;

public class GetUserListReplyMsg extends CoedMessage{
	private String[] userList;
	private String file;
	
	public GetUserListReplyMsg (String file, String[] userList){
		this.userList = userList;
		this.file = file;
	}
	
	public void setUserList(String[] userList){
		this.userList = userList;
	}
	
	public String[] getUserList(){
		return this.userList;
	}
	
	public String getFile(){
		return this.file;
	}
	
	public void setFile(String file){
		this.file = file;
	}

}
