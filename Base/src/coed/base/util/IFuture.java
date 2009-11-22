package coed.base.util;

import java.util.concurrent.Future;

public interface IFuture<T> extends Future<T> {
	/**
	 * Add a listener to the future that will be called either
	 * when the result is set or when an error is caught
	 * @param listener
	 */
	public void addListener(IFutureListener<T> listener);
	
	/**
	 * Get the value for this future from the specified future.
	 * @param future
	 */
	public void chain(IFuture<T> future);
	
	/**
	 * Add a listeners to the future that will be called only
	 * when an error is caught
	 * @param listener
	 */
	public void addErrorListener(IFutureErrorListener listener);
}
