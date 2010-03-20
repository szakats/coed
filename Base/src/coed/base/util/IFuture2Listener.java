/**
 * 
 */
package coed.base.util;

/**
 * @author szakats
 *
 */
public interface IFuture2Listener<T, U> extends IFutureErrorListener {
	public void got(T result1, U result2);
}
