package coed.collab.protocol;

public class GetUserListMsg extends CoedMessage{
	private Integer Id;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public GetUserListMsg(Integer id) {
		super();
		Id = id;
	}

}
