package coed.base.util;

public interface IFutureListener<T> extends IFutureErrorListener {
	public void got(T result);
}
