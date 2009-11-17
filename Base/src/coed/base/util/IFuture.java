package coed.base.util;

import java.util.concurrent.Future;

public interface IFuture<T> extends Future<T> {
	public void add(IFutureListener<T> listener);
}
