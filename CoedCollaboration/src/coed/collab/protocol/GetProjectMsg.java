package coed.collab.protocol;

public class GetProjectMsg extends CoedMessage {
	private String name;
	
	public GetProjectMsg(String name) {
		this.setName(name);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
