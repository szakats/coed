package coed.collab.protocol;

public class GetUserListReplyMsg extends CoedMessage{
	private String[] userList;
	private Integer Id;
	
	public GetUserListReplyMsg (Integer Id, String[] userList){
		this.userList = userList;
		this.Id = Id;
	}
	
	public void setUserList(String[] userList){
		this.userList = userList;
	}
	
	public String[] getUserList(){
		return this.userList;
	}
	
	public Integer getId(){
		return this.Id;
	}
	
	public void setId(Integer Id){
		this.Id = Id;
	}

}
