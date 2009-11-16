/**
 * 
 */
package coed.collab.server;

import java.util.Date;

/**
 * Class Containing information about a given change occured to a given file.
 * @author Neobi
 *
 */
public class CoedFileChange {
	
	private int lineNr;
	private String[] text;
	private int sessionID;
	private Date time;
	
	public CoedFileChange(int lineNr, String[] text, int sessionID, Date time){
		this.lineNr = lineNr;
		this.text = text;
		this.sessionID = sessionID;
		this.time = time;
	}

	public int getLineNr() {
		return lineNr;
	}

	public void setLineNr(int lineNr) {
		this.lineNr = lineNr;
	}

	public String[] getText() {
		return text;
	}

	public void setText(String[] text) {
		this.text = text;
	}

	public int getSessionID() {
		return sessionID;
	}

	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
