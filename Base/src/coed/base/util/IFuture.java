package coed.base.util;

import java.util.concurrent.Future;

public interface IFuture<T> extends Future<T> {
	/**
	 * Add a listener to the future that will be called when the result is set
	 * @param listener
	 */
	public void add(IFutureListener<T> listener);
	
	/**
	 * Get the value for this future from the specified future.
	 * @param future
	 */
	public void chain(IFuture<T> future);
}
