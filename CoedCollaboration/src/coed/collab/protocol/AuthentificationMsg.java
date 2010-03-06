package coed.collab.protocol;

public class AuthentificationMsg extends CoedMessage{

	private String userName;
	private String password;

	public AuthentificationMsg(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	public void setFileName(String userName) {
		this.userName = userName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public String getPassword(){
		return this.password;
	}
}
