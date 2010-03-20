package coed.collab.protocol;

import coed.base.data.TextPortion;

public class ReleaseLockMsg extends CoedMessage{
	
	private Integer Id;
	private TextPortion portion;
	
	public ReleaseLockMsg(Integer Id, TextPortion portion) {
		this.Id = Id;
		this.portion = portion;
	}
		/**
	 * @param file the file to set
	 */
	public void setId(Integer Id) {
		this.Id = Id;
	}
		/**
	 * @return the file
	 */
	public Integer getId() {
		return this.Id;
	}
	
	public void setPortion(TextPortion portion){
		this.portion = portion;
	}
	
	public TextPortion getPortion(){
		return this.portion;
	}
}
