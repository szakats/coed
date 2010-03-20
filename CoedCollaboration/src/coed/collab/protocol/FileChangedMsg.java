package coed.collab.protocol;

public class FileChangedMsg extends CoedMessage {
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FileChangedMsg(Integer id) {
		super();
		this.id = id;
	}
}
