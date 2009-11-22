package coed.base.util;

public interface IFutureErrorListener {
	
	/**
	 * Called when some exception has been caught
	 * @param e
	 */
	public void caught(Throwable e);
}
