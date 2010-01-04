package coed.plugin.text.locks;

import coed.base.data.exceptions.InvalidConfigFileException;

public class TextLockFactory {
	public static final String OPTIMISTIC_MANAGER="optimistic";
	public static final String PROTECTIVE_MANAGER="protective";

	public static ITextLockManager getManagerFor(String type) throws InvalidConfigFileException{
		return getManagerFor(type,0);
	}
	
	public static ITextLockManager getManagerFor(String type, Integer releaseTimeout) throws InvalidConfigFileException{
		return null;
	}
	
}
