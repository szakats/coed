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
	private String userName;
	private Date time;
	
	public CoedFileChange(int lineNr, String[] text, String userName, Date time){
		this.lineNr = lineNr;
		this.text = text;
		this.userName = userName;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
}
