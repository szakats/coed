package coed.collab.protocol;

public class GetProjectResponseMsg extends CoedMessage {
	private boolean success;
	
	public GetProjectResponseMsg(boolean success) {
		this.setSuccess(true);
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}
}
