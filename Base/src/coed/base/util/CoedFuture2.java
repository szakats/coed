/**
 * 
 */
package coed.base.util;

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
}
