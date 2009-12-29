package coed.collab.protocol;

public class AuthentificationMsg extends CoedMessage{

	private String userName;

	public AuthentificationMsg(String userName) {
		super();
		this.userName = userName;
	}

	public void setFileName(String userName) {
		this.userName = userName;
	}
	
	public String getUserName() {
		return userName;
	}
}
