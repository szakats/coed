/**
 * 
 */
package coed.base.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author szakats
 *
 */
public class CoedFuture2<T,U> implements IFuture2<T, U> {
	CoedFuture<Pair<T,U>> future;
	
	class CoedFuture2Listener implements IFutureListener<Pair<T, U>> {
		IFuture2Listener<T, U> lis;
		
		public CoedFuture2Listener(IFuture2Listener<T, U> lis) {
			super();
			this.lis = lis;
		}

		@Override
		public void got(Pair<T,U> result) {
			lis.got(result.getFirst(), result.getSecond());
		}

		@Override
		public void caught(Throwable e) {
			lis.caught(e);
		}
	}
	
	public CoedFuture2() {
		future = new CoedFuture<Pair<T,U>>();
	}
	
	public CoedFuture2(T result1, U result2) {
		future = new CoedFuture<Pair<T,U>>(new Pair<T,U>(result1, result2));
	}
	
	public CoedFuture2(Throwable e) {
		future = new CoedFuture<Pair<T,U>>(e);
	}

	@Override
	public void addErrorListener(IFutureErrorListener listener) {
		future.addErrorListener(listener);
	}

	@Override
	public void addListener(IFuture2Listener<T, U> listener) {
		future.addListener(new CoedFuture2Listener(listener));
	}
	
	public synchronized void set(T result1, U result2) {
		future.set(new Pair<T,U>(result1, result2));
	}
	
	public synchronized void throwEx(Throwable e) {
		future.throwEx(e);
	}
	
	@Override
	public synchronized void chain(IFuture2<T, U> future) {
		addListener(new IFuture2Listener<T, U>() {
			@Override
			public void got(T result1, U result2) {
				set(result1, result2);
			}
			@Override
			public void caught(Throwable e) {
				throwEx(e);
			}
		});
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return future.cancel(mayInterruptIfRunning);
	}

	@Override
	public synchronized Pair<T,U> get() throws InterruptedException, ExecutionException {
		return future.get();
	}

	/**
	 * Waits if necessary for at most the given time for the computation to complete, 
	 * and then retrieves its result, if available. 
	 * @throws timeout - the maximum time to wait
	 * @throws unit - the time unit of the timeout argument 
	 * @throws CancellationException - if the computation was cancelled 
	 * @throws ExecutionException - if the computation threw an exception 
	 * @throws InterruptedException - if the current thread was interrupted while waiting 
	 * @throws TimeoutException - if the wait timed out
	 * @return the computed result 
	 */
	@Override
	public synchronized Pair<T,U> get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		return future.get(timeout, unit);
	}

	@Override
	public boolean isCancelled() {
		return future.isCancelled();
	}

	@Override
	public synchronized boolean isDone() {
		return future.isDone();
	}
}
