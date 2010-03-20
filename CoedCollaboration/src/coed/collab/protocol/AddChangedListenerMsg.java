package coed.collab.protocol;

public class AddChangedListenerMsg extends CoedMessage {
	private Integer Id;

	public AddChangedListenerMsg(Integer id) {
		super();
		Id = id;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}
	
}
