/**
 * 
 */
package coed.base.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author szakats
 *
 */
public interface IFuture2<T, U> extends Future<Pair<T, U>> {
	/**
	 * Add a listener to the future that will be called either
	 * when the result is set or when an error is caught
	 * @param listener
	 */
	public void addListener(IFuture2Listener<T, U> listener);
	
	/**
	 * Get the value for this future from the specified future.
	 * @param future
	 */
	public void chain(IFuture2<T, U> future);
	
	/**
	 * Add a listeners to the future that will be called only
	 * when an error is caught
	 * @param listener
	 */
	public void addErrorListener(IFutureErrorListener listener);
}
