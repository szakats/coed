package coed.base.util;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CoedFuture<T> implements IFuture<T> {
	private LinkedList<IFutureListener<T>> listeners;
	private T result;
	
	private static final TimeoutException timeoutException = new TimeoutException();
	
	public CoedFuture() {

	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized T get() throws InterruptedException, ExecutionException {
		this.wait();
		assert(isDone());
		
		return result;
	}

	@Override
	public synchronized T get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		this.wait(unit.toMillis(timeout));
		if(!isDone())
			throw timeoutException;
		
		return result;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized boolean isDone() {
		return result != null;
	}

	@Override
	public void addListener(IFutureListener<T> listener) {
		assert(listener != null);
		if(listeners == null)
			listeners = new LinkedList<IFutureListener<T>>();
		listeners.add(listener);
	}
	
	public synchronized void set(T result) {
		this.result = result;
		if(listeners != null)
			for(IFutureListener<T> listener : listeners)
				listener.got(result);
		
		notifyAll();
	}
}
