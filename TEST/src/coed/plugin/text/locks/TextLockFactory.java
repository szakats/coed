package coed.plugin.text.locks;

import coed.base.data.exceptions.InvalidConfigFileException;

public class TextLockFactory {
	public static final String BASIC_MANAGER="basic";
	public static final String PROTECTIVE_MANAGER="protective";

	public static ITextLockManager getManagerFor(String type) throws InvalidConfigFileException{
		return getManagerFor(type,0);
	}
	
	public static ITextLockManager getManagerFor(String type, Integer releaseDelay) throws InvalidConfigFileException{
		if (type==BASIC_MANAGER){
			return new BasicTextLockManager(releaseDelay);
		} else if (type==PROTECTIVE_MANAGER){
			return new ProtectiveTextLockManager(releaseDelay);
		} else {
			//sorry. something is not clear...
			throw new InvalidConfigFileException();
		}
	}
	
}
