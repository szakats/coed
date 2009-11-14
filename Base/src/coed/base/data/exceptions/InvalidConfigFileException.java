package coed.base.data.exceptions;

import java.io.IOException;

public class InvalidConfigFileException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidConfigFileException(){}
	
	public InvalidConfigFileException(String msg){
		super(msg);
	}

}
