/**
 * 
 */
package coed.collab.server;

import java.util.Date;

import coed.base.data.TextModification;

/**
 * Class Containing information about a given change occured to a given file.
 * text is the textModification occured
 * time is the time when the modification occured
 * lineOffset is the starting offset of the line, where the modification occured.
 * @author Neobi
 *
 */
public class CoedFileChange {
	
	private int lineOffset;
	private TextModification text;
	private Date time;
	
	public CoedFileChange(TextModification text, Date time){
		this.lineOffset = text.getOffset();
		this.text = text;
		this.time = time;
	}

	
	public int getLineOffset() {
		return lineOffset;
	}

	public void setLineOffset(int lineOffset) {
		text.setOffset(lineOffset);
	}

	public TextModification getMod() {
		return text;
	}

	public void setMod(TextModification text) {
		this.text = text;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
}
